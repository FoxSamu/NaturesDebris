/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 14 - 2020
 * Author: rgsw
 */

package modernity.client.render.environment;

import com.mojang.blaze3d.platform.GlStateManager;
import modernity.api.dimension.IEnvironmentDimension;
import modernity.api.reflect.FieldAccessor;
import modernity.client.environment.EnvironmentRenderingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import net.redgalaxy.util.MathUtil;
import net.rgsw.noise.FractalPerlin3D;
import net.rgsw.noise.INoise3D;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Supplier;

public class SurfaceSkyRenderer implements IRenderHandler {
    private static final FieldAccessor<GameRenderer, FogRenderer> fogRendererField = new FieldAccessor<>( GameRenderer.class, "field_205003_A" );

    private static final int TWILIGHT_SEGMENTS = 20; // MAYBE: Use setting for this.

    private static final ResourceLocation STAR = new ResourceLocation( "modernity:textures/environment/stars.png" );
    private static final ResourceLocation METEORITE = new ResourceLocation( "modernity:textures/environment/meteorite.png" );

    private static final ResourceLocation FOG = new ResourceLocation( "modernity:textures/environment/fog.png" );

    private static final ResourceLocation[] MOONS = {
        new ResourceLocation( "modernity:textures/environment/moon0.png" ),
        new ResourceLocation( "modernity:textures/environment/moon1.png" ),
        new ResourceLocation( "modernity:textures/environment/moon2.png" ),
        new ResourceLocation( "modernity:textures/environment/moon3.png" ),
        new ResourceLocation( "modernity:textures/environment/moon4.png" ),
        new ResourceLocation( "modernity:textures/environment/moon5.png" ),
        new ResourceLocation( "modernity:textures/environment/moon6.png" ),
        new ResourceLocation( "modernity:textures/environment/moon7.png" )
    };

    private static final Vec3d UP = new Vec3d( 0, 1, 0 );
    private static final Random RANDOM = new Random();

    private final ArrayList<Meteorite> meteorites = new ArrayList<>();
    private final ArrayList<Cloud> clouds = new ArrayList<>();
    private final ArrayList<Light> lights = new ArrayList<>();
    private VertexBuffer starVBO;
    private int starList = - 1;

    private float[] twilightHeights = new float[ TWILIGHT_SEGMENTS ];
    private float[] twilightOffsets = new float[ TWILIGHT_SEGMENTS ];
    private float[] twilightBrightnesses = new float[ TWILIGHT_SEGMENTS ];
    private INoise3D twilightNoise;

    private int ticks;
    private float partialTicks;
    private ClientWorld world;
    private Minecraft mc = Minecraft.getInstance();

    private final int moonStarRotationShift;
    private final int moonStarRotationAngle;

    private final Supplier<FogRenderer> fogRenderer = fogRendererField.forInstance( mc.gameRenderer );

    public SurfaceSkyRenderer( long seed ) {
        Random rand = new Random( seed * 372775239494235L );
        twilightNoise = new FractalPerlin3D( rand.nextInt(), 3 );

        moonStarRotationShift = rand.nextInt( 101 ) - 50;
        moonStarRotationAngle = rand.nextInt( 360 );

        generateStars( rand );
    }


    @Override
    public void render( int tick, float pt, ClientWorld cw, Minecraft mc ) {
        partialTicks = pt;
        world = cw;

        if( tick != ticks ) {
            ticks = tick;
            tick();
        }

        render();
    }

    private void generateStars( Random rand ) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();

        if( starList >= 0 ) {
            GLAllocation.deleteDisplayLists( starList );
            starList = - 1;
        }

        starList = GLAllocation.generateDisplayLists( 1 );
        GlStateManager.pushMatrix();
        GlStateManager.newList( starList, 4864 );
        buildStars( buff, rand );
        tess.draw();
        GlStateManager.endList();
        GlStateManager.popMatrix();
    }

    private void buildStars( BufferBuilder buff, Random rand ) {
        buff.begin( 7, DefaultVertexFormats.POSITION_TEX );

        for( int star = 0; star < 3600; ++ star ) {
            double x = rand.nextDouble() * 2 - 1;
            double y = rand.nextDouble() * 2 - 1;
            double z = rand.nextDouble() * 2 - 1;

            double size = 0.25 + rand.nextDouble() * 0.08;
            if( rand.nextInt( 3 ) == 0 ) {
                size *= 1 + rand.nextDouble() * 2;
            }

            double distance = x * x + y * y + z * z;

            if( distance < 1 && distance > 0.01 ) {
                distance = 1 / Math.sqrt( distance );

                x *= distance;
                y *= distance;
                z *= distance;

                double px = x * 100;
                double py = y * 100;
                double pz = z * 100;

                double direction = Math.atan2( x, z );
                double theta = Math.atan2( Math.sqrt( x * x + z * z ), y );

                double dirX = Math.sin( direction );
                double dirY = Math.sin( theta );
                double dirZ = Math.cos( direction );

                double dirXmultiplier = Math.cos( theta );

                double rotation = rand.nextDouble() * Math.PI * 2;

                double rotSin = Math.sin( rotation );
                double rotCos = Math.cos( rotation );

                int blue = rand.nextInt( 3 ) == 0 ? 1 : 0;
                int alpha = rand.nextInt( 4 );

                for( int vtx = 0; vtx < 4; ++ vtx ) {
                    double locU = ( vtx & 2 ) - 1;
                    double locV = ( vtx + 1 & 2 ) - 1;

                    double texU = ( locU + 1 ) / 2;
                    double texV = ( locV + 1 ) / 2;

                    double posU = ( locU * rotCos - locV * rotSin ) * size;
                    double posV = ( locV * rotCos + locU * rotSin ) * size;
                    double posW = - posU * dirXmultiplier;

                    double posY = posU * dirY;
                    double posX = posW * dirX - posV * dirZ;
                    double posZ = posV * dirX + posW * dirZ;
                    buff.pos( px + posX, py + posY, pz + posZ )
                        .tex( texU * 0.25 + 0.25 * alpha, texV * 0.5 + 0.5 * blue )
                        .endVertex();
                }
            }
        }
    }

    private void tick() {
        tickClouds();
        tickLights();
        tickMeteorites();
        tickTwilight();
    }

    private void tickClouds() {
        if( RANDOM.nextDouble() < EnvironmentRenderingManager.SKY.cloudAmount ) {
            int amount = RANDOM.nextInt( 4 ) + 2;

            double x = ( RANDOM.nextBoolean() ? - 1 : 1 ) * RANDOM.nextFloat() * 0.8 + 0.2;
            double y = ( RANDOM.nextBoolean() ? - 1 : 1 ) * RANDOM.nextFloat() * 0.8 + 0.2;
            double z = ( RANDOM.nextBoolean() ? - 1 : 1 ) * RANDOM.nextFloat() * 0.8 + 0.2;

            double alpha = RANDOM.nextFloat() * 0.05 + 0.04;
            double colorRed = ( 0.8 + RANDOM.nextFloat() * 0.2 ) * 0.3;
            double colorGreen = ( 0.7 + RANDOM.nextFloat() * 0.3 ) * 0.3;
            double colorBlue = ( 0.9 + RANDOM.nextFloat() * 0.1 ) * 0.3;

            int time = RANDOM.nextInt( 200 ) + 400;

            for( int i = 0; i < amount; i++ ) {
                Cloud cloud = new Cloud();

                cloud.x = x + RANDOM.nextFloat() * 2 - 1;
                cloud.y = y + RANDOM.nextFloat() * 2 - 1;
                cloud.z = z + RANDOM.nextFloat() * 2 - 1;
                cloud.xv = 0.0002 * ( RANDOM.nextFloat() * 2 - 1 );
                cloud.yv = 0.0002 * ( RANDOM.nextFloat() * 2 - 1 );
                cloud.zv = 0.0002 * ( RANDOM.nextFloat() * 2 - 1 );

                cloud.r = (float) colorRed;
                cloud.g = (float) colorGreen;
                cloud.b = (float) colorBlue;
                cloud.a = (float) alpha;

                cloud.age = - RANDOM.nextInt( 100 );
                cloud.maxAge = time + RANDOM.nextInt( 100 );

                cloud.type = RANDOM.nextInt( 8 );

                cloud.scale = 40 + RANDOM.nextInt( 60 );

                clouds.add( cloud );
            }
        }


        Iterator<Cloud> cloudIterator = clouds.iterator();
        while( cloudIterator.hasNext() ) {
            Cloud cloud = cloudIterator.next();

            cloud.x += cloud.xv;
            cloud.y += cloud.yv;
            cloud.z += cloud.zv;
            cloud.age++;

            if( cloud.age > cloud.maxAge ) {
                cloudIterator.remove();
            }
        }
    }

    private void tickLights() {
        if( RANDOM.nextDouble() < 0.032 && lights.size() < 20 ) {
            int amount = RANDOM.nextInt( 2 ) + 1;

            double x = ( RANDOM.nextBoolean() ? - 1 : 1 ) * RANDOM.nextFloat();
            double y = RANDOM.nextFloat() * 0.4 + 0.6;
            double z = ( RANDOM.nextBoolean() ? - 1 : 1 ) * RANDOM.nextFloat();

            int time = RANDOM.nextInt( 200 ) + 400;

            for( int i = 0; i < amount; i++ ) {
                Light light = new Light();

                light.x = x + RANDOM.nextFloat() * 2 - 1;
                light.y = y + RANDOM.nextFloat() * 2 - 1;
                light.z = z + RANDOM.nextFloat() * 2 - 1;
                light.xv = 0.0002 * ( RANDOM.nextFloat() * 2 - 1 );
                light.yv = 0.0002 * ( RANDOM.nextFloat() * 2 - 1 );
                light.zv = 0.0002 * ( RANDOM.nextFloat() * 2 - 1 );

                light.age = - RANDOM.nextInt( 100 );
                light.maxAge = time + RANDOM.nextInt( 100 );

                light.type = RANDOM.nextInt( 8 );

                light.scale = 40 + RANDOM.nextInt( 40 );
                lights.add( light );
            }
        }


        Iterator<Light> lightIterator = lights.iterator();
        while( lightIterator.hasNext() ) {
            Light light = lightIterator.next();

            light.x += light.xv;
            light.y += light.yv;
            light.z += light.zv;
            light.age += 0.3;

            if( light.age > light.maxAge ) {
                lightIterator.remove();
            }
        }
    }

    private void tickMeteorites() {
        double meteoriteChance = EnvironmentRenderingManager.SKY.meteoriteAmount;

        if( RANDOM.nextDouble() < meteoriteChance ) {
            float pitch = RANDOM.nextFloat() * 360;
            float yaw = RANDOM.nextFloat() * 360;

            float off = RANDOM.nextFloat() * 50 + 50;

            float len = 96 + ( RANDOM.nextFloat() * 20 - 10 );

            int time = RANDOM.nextInt( 10 ) + 5;

            float speed = RANDOM.nextFloat() + 0.5F;

            Vec3d loc = Vec3d.fromPitchYaw( pitch, yaw ).scale( off );
            Vec3d horiz = loc.crossProduct( UP ).normalize();
            Vec3d vert = loc.crossProduct( horiz ).normalize();
            Vec3d vel = horiz.scale( 8 * speed ).add( vert.scale( ( RANDOM.nextFloat() * 16 - 8 ) * speed ) );

            double colorRed = 0.8 + RANDOM.nextDouble() * 0.2;
            double colorGreen = 0.7 + RANDOM.nextDouble() * 0.3;
            double colorBlue = 0.9 + RANDOM.nextDouble() * 0.1;
            double colorAlpha = 0.5F + RANDOM.nextFloat() * 0.5F;

            if( RANDOM.nextInt( 3 ) == 0 ) {
                colorRed *= 0.3;
                colorGreen *= 0.3;
            }


            Meteorite meteorite = new Meteorite();

            meteorite.x = loc.x;
            meteorite.y = loc.y;
            meteorite.z = loc.z;

            meteorite.xv = vel.x;
            meteorite.yv = vel.y;
            meteorite.zv = vel.z;

            meteorite.r = (float) colorRed;
            meteorite.g = (float) colorGreen;
            meteorite.b = (float) colorBlue;
            meteorite.a = (float) colorAlpha;

            meteorite.len = len;
            meteorite.scale = MathUtil.lerp( 0.1, 0.3, RANDOM.nextDouble() );

            meteorite.maxAge = time;

            meteorites.add( meteorite );
        }


        Iterator<Meteorite> meteoriteIterator = meteorites.iterator();
        while( meteoriteIterator.hasNext() ) {
            Meteorite meteorite = meteoriteIterator.next();
            meteorite.x += meteorite.xv;
            meteorite.y += meteorite.yv;
            meteorite.z += meteorite.zv;
            meteorite.age++;

            if( meteorite.age > meteorite.maxAge ) {
                meteoriteIterator.remove();
            }
        }
    }

    private void tickTwilight() {
        float d = (float) Math.PI * 2F / TWILIGHT_SEGMENTS;
        for( int i = 0; i < TWILIGHT_SEGMENTS; i++ ) {
            float sin = MathHelper.sin( i * d ) * 3.188572838F;
            float cos = MathHelper.cos( i * d ) * 3.188572838F;

            double rand = EnvironmentRenderingManager.SKY.twilightHeightRandom;
            double hgt = EnvironmentRenderingManager.SKY.twilightHeight;

            double height = twilightNoise.generate( sin, ticks / 800F, cos ) * rand + hgt;
            double off = twilightNoise.generate( sin + 10, ticks / 800F, cos ) * d / 1.3;
            double color = ( twilightNoise.generate( sin + 10, ticks / 800F, cos + 10 ) / 2 + 0.5 ) * 0.6 + 0.4;

            twilightHeights[ i ] = (float) height;
            twilightOffsets[ i ] = (float) off;
            twilightBrightnesses[ i ] = (float) color;
        }
    }

    private void render() {
        preRender();
        renderBackground();
        renderBacklight();
        renderStars();
        renderTwilight();
        renderMeteorites();
        renderSatellite();
        renderClouds();
        renderSkylight();
        postRender();
    }

    private void preRender() {

        if( world.dimension instanceof IEnvironmentDimension ) {
            ( (IEnvironmentDimension) world.dimension ).updateSky( EnvironmentRenderingManager.SKY );
        }

        fogRenderer.get().setupFog( mc.gameRenderer.getActiveRenderInfo(), 0, partialTicks );

        GlStateManager.disableBlend();
        GlStateManager.disableAlphaTest();
        GlStateManager.disableDepthTest();
        GlStateManager.blendFunc( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE );
        GlStateManager.disableCull();
        GlStateManager.depthMask( false );
    }

    private void postRender() {
        GlStateManager.blendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE );
        GlStateManager.depthMask( true );
        GlStateManager.enableAlphaTest();
        GlStateManager.enableCull();
        GlStateManager.enableFog();
        GlStateManager.enableDepthTest();
    }

    private void renderStars() {

        float brightness = EnvironmentRenderingManager.SKY.starBrightness;

        GlStateManager.enableBlend();
        GlStateManager.blendFunc( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE );

        if( brightness > 0 ) {
            Minecraft.getInstance().getTextureManager().bindTexture( STAR );
            float moonRot = EnvironmentRenderingManager.SKY.moonRotation;

            GlStateManager.pushMatrix();
            GlStateManager.rotatef( moonStarRotationAngle, 0, 1, 0 );
            GlStateManager.rotatef( moonStarRotationShift, 0, 0, 1 );
            GlStateManager.rotatef( moonRot * 360, 1, 0, 0 );
            GlStateManager.color4f( 1, 1, 1, brightness );

            GlStateManager.callList( starList );

            GlStateManager.color4f( 1, 1, 1, 1 );
            GlStateManager.popMatrix();
        }
    }

    private void renderBackground() {

        float r = EnvironmentRenderingManager.FOG.color[ 0 ];
        float g = EnvironmentRenderingManager.FOG.color[ 1 ];
        float b = EnvironmentRenderingManager.FOG.color[ 2 ];

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();
        GlStateManager.disableTexture();
        buff.begin( GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR );
        buff.pos( - 100, - 100, - 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( 100, - 100, - 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( 100, - 100, 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( - 100, - 100, 100 ).color( r, g, b, 1 ).endVertex();

        buff.pos( - 100, 100, - 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( 100, 100, - 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( 100, 100, 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( - 100, 100, 100 ).color( r, g, b, 1 ).endVertex();

        buff.pos( - 100, - 100, - 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( - 100, 100, - 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( - 100, 100, 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( - 100, - 100, 100 ).color( r, g, b, 1 ).endVertex();

        buff.pos( 100, - 100, - 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( 100, 100, - 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( 100, 100, 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( 100, - 100, 100 ).color( r, g, b, 1 ).endVertex();

        buff.pos( - 100, - 100, - 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( - 100, 100, - 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( 100, 100, - 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( 100, - 100, - 100 ).color( r, g, b, 1 ).endVertex();

        buff.pos( - 100, - 100, 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( - 100, 100, 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( 100, 100, 100 ).color( r, g, b, 1 ).endVertex();
        buff.pos( 100, - 100, 100 ).color( r, g, b, 1 ).endVertex();
        tess.draw();
        GlStateManager.enableTexture();
    }

    private void renderTwilight() {
        float brightness = EnvironmentRenderingManager.SKY.twilightBrightness;
        if( brightness <= 0 ) return;

        GlStateManager.enableBlend();
        GlStateManager.enableColorMaterial();
        GlStateManager.shadeModel( GL11.GL_SMOOTH );

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();
        GlStateManager.disableTexture();

        float r = EnvironmentRenderingManager.SKY.twilightColor[ 0 ];
        float g = EnvironmentRenderingManager.SKY.twilightColor[ 1 ];
        float b = EnvironmentRenderingManager.SKY.twilightColor[ 2 ];

        buff.begin( GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR );
        for( int i = 0; i < TWILIGHT_SEGMENTS; i++ ) {
            int n = i + 1;
            int j = n == TWILIGHT_SEGMENTS ? 0 : n;

            float height1 = twilightHeights[ i ];
            float color1 = twilightBrightnesses[ i ] * brightness;
            float offset1 = twilightOffsets[ i ];
            float height2 = twilightHeights[ j ];
            float color2 = twilightBrightnesses[ j ] * brightness;
            float offset2 = twilightOffsets[ j ];

            float angle1 = (float) i / TWILIGHT_SEGMENTS * (float) Math.PI * 2 + offset1;
            float angle2 = (float) n / TWILIGHT_SEGMENTS * (float) Math.PI * 2 + offset2;

            float x1 = MathHelper.sin( angle1 ) * 20;
            float x2 = MathHelper.sin( angle2 ) * 20;
            float z1 = MathHelper.cos( angle1 ) * 20;
            float z2 = MathHelper.cos( angle2 ) * 20;

            // @formatter:off
            buff.pos( x1, height1 / 2, z1 ).color( r, g, b, color1 / 1.5F ).endVertex();
            buff.pos( x2, height2 / 2, z2 ).color( r, g, b, color2 / 1.5F ).endVertex();
            buff.pos( x2, 0,           z2 ).color( r, g, b, color2        ).endVertex();
            buff.pos( x1, 0,           z1 ).color( r, g, b, color1        ).endVertex();

            buff.pos( x1, height1,     z1 ).color( r, g, b, 0             ).endVertex();
            buff.pos( x2, height2,     z2 ).color( r, g, b, 0             ).endVertex();
            buff.pos( x2, height2 / 2, z2 ).color( r, g, b, color2 / 1.5F ).endVertex();
            buff.pos( x1, height1 / 2, z1 ).color( r, g, b, color1 / 1.5F ).endVertex();

            buff.pos( x1, - height1,     z1 ).color( r, g, b, 0             ).endVertex();
            buff.pos( x2, - height2,     z2 ).color( r, g, b, 0             ).endVertex();
            buff.pos( x2, - height2 / 2, z2 ).color( r, g, b, color2 / 1.5F ).endVertex();
            buff.pos( x1, - height1 / 2, z1 ).color( r, g, b, color1 / 1.5F ).endVertex();

            buff.pos( x1, - height1 / 2, z1 ).color( r, g, b, color1 / 1.5F ).endVertex();
            buff.pos( x2, - height2 / 2, z2 ).color( r, g, b, color2 / 1.5F ).endVertex();
            buff.pos( x2,   0,           z2 ).color( r, g, b, color2        ).endVertex();
            buff.pos( x1,   0,           z1 ).color( r, g, b, color1        ).endVertex();

            // @formatter:on
        }
        tess.draw();
        GlStateManager.shadeModel( GL11.GL_FLAT );
        GlStateManager.disableColorMaterial();
        GlStateManager.enableTexture();
    }

    private void renderBacklight() {
        float brightness = EnvironmentRenderingManager.SKY.backlightBrightness;
        if( brightness > 0 ) {
            GlStateManager.disableTexture();
            GlStateManager.enableFog();

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            float far = mc.gameRenderer.getFarPlaneDistance() * 2;
            float skyR = EnvironmentRenderingManager.SKY.backlightColor[ 0 ];
            float skyG = EnvironmentRenderingManager.SKY.backlightColor[ 1 ];
            float skyB = EnvironmentRenderingManager.SKY.backlightColor[ 2 ];

            buffer.begin( 7, DefaultVertexFormats.POSITION_COLOR );
            buffer.pos( - far, 30, far ).color( skyR, skyG, skyB, brightness ).endVertex();
            buffer.pos( far, 30, far ).color( skyR, skyG, skyB, brightness ).endVertex();
            buffer.pos( far, 30, - far ).color( skyR, skyG, skyB, brightness ).endVertex();
            buffer.pos( - far, 30, - far ).color( skyR, skyG, skyB, brightness ).endVertex();
            tessellator.draw();

            GlStateManager.disableFog();
            GlStateManager.enableTexture();
        }
    }

    private void renderMeteorites() {
        if( meteorites.isEmpty() ) return;
        float brightness = EnvironmentRenderingManager.SKY.starBrightness;
        if( brightness <= 0 ) return;

        GlStateManager.color4f( 1, 1, 1, brightness );
        Minecraft.getInstance().getTextureManager().bindTexture( METEORITE );

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();

        buff.begin( GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR );
        for( Meteorite mtr : meteorites ) {
            double len = mtr.len;
            double a = 1;

            if( mtr.age <= 3 ) {
                len = MathUtil.lerp( 0, len, mtr.age / 3D );
                a = mtr.age / 3D;
            }
            if( mtr.age >= mtr.maxAge - 3 ) {
                len = MathUtil.lerp( 0, len, ( mtr.maxAge - mtr.age ) / 3D );
                a = ( mtr.maxAge - mtr.age ) / 3D;
            }

            Vec3d mdir = new Vec3d( - mtr.xv, - mtr.yv, - mtr.zv ).normalize().scale( len ).scale( ( 0.31 + mtr.scale ) / 2 );

            Vec3d horiz = mdir.crossProduct( UP ).normalize().scale( mtr.scale );
            Vec3d vert = mdir.crossProduct( horiz ).normalize().scale( mtr.scale );

            Vec3d pos1 = new Vec3d( mtr.x, mtr.y, mtr.z );
            Vec3d pos2 = pos1.add( mdir );


            float starRed = EnvironmentRenderingManager.SKY.starColor[ 0 ];
            float starGreen = EnvironmentRenderingManager.SKY.starColor[ 1 ];
            float starBlue = EnvironmentRenderingManager.SKY.starColor[ 2 ];
            float starBrightness = EnvironmentRenderingManager.SKY.starBrightness;


            Vec3d p1 = pos1.subtract( vert );
            Vec3d p2 = pos1.add( vert );
            Vec3d p3 = pos2.add( vert );
            Vec3d p4 = pos2.subtract( vert );

            buff.pos( p1.x, p1.y, p1.z ).tex( 0, 0 ).color(
                mtr.r * starRed,
                mtr.g * starGreen,
                mtr.b * starBlue,
                mtr.a * (float) a * starBrightness
            ).endVertex();
            buff.pos( p2.x, p2.y, p2.z ).tex( 0, 1 ).color(
                mtr.r * starRed,
                mtr.g * starGreen,
                mtr.b * starBlue,
                mtr.a * (float) a * starBrightness
            ).endVertex();
            buff.pos( p3.x, p3.y, p3.z ).tex( 1, 1 ).color(
                mtr.r * starRed,
                mtr.g * starGreen,
                mtr.b * starBlue,
                mtr.a * (float) a * starBrightness
            ).endVertex();
            buff.pos( p4.x, p4.y, p4.z ).tex( 1, 0 ).color(
                mtr.r * starRed,
                mtr.g * starGreen,
                mtr.b * starBlue,
                mtr.a * (float) a * starBrightness
            ).endVertex();


            Vec3d p5 = pos1.subtract( horiz );
            Vec3d p6 = pos1.add( horiz );
            Vec3d p7 = pos2.add( horiz );
            Vec3d p8 = pos2.subtract( horiz );

            buff.pos( p5.x, p5.y, p5.z ).tex( 0, 0 ).color(
                mtr.r * starRed,
                mtr.g * starGreen,
                mtr.b * starBlue,
                mtr.a * (float) a * starBrightness
            ).endVertex();
            buff.pos( p6.x, p6.y, p6.z ).tex( 0, 1 ).color(
                mtr.r * starRed,
                mtr.g * starGreen,
                mtr.b * starBlue,
                mtr.a * (float) a * starBrightness
            ).endVertex();
            buff.pos( p7.x, p7.y, p7.z ).tex( 1, 1 ).color(
                mtr.r * starRed,
                mtr.g * starGreen,
                mtr.b * starBlue,
                mtr.a * (float) a * starBrightness
            ).endVertex();
            buff.pos( p8.x, p8.y, p8.z ).tex( 1, 0 ).color(
                mtr.r * starRed,
                mtr.g * starGreen,
                mtr.b * starBlue,
                mtr.a * (float) a * starBrightness
            ).endVertex();
        }

        tess.draw();
    }

    private void renderClouds() {
        GlStateManager.blendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE
        );

        if( clouds.isEmpty() ) return;
        float brightness = EnvironmentRenderingManager.SKY.cloudBrightness;
        if( brightness <= 0 ) return;

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();

        buff.begin( GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR );
        mc.getTextureManager().bindTexture( FOG );

        for( Cloud cld : clouds ) {
            double x = cld.x;
            double y = cld.y;
            double z = cld.z;
            double size = cld.scale;
            double colorRed = cld.r;
            double colorGreen = cld.g;
            double colorBlue = cld.b;
            double colorAlpha = cld.a;
            double distance = x * x + y * y + z * z;

            if( distance > 0.01 && cld.age >= 0 ) {

                double alpha = 1;
                if( cld.age < 70 ) {
                    alpha = Math.max( cld.age / 70D, 0 );
                }
                if( cld.age > cld.maxAge - 70 ) {
                    alpha = Math.max( ( cld.maxAge - cld.age ) / 70D, 0 );
                }
                distance = 1 / Math.sqrt( distance );
                x *= distance;
                y *= distance;
                z *= distance;
                double px = x * 100;
                double py = y * 100;
                double pz = z * 100;

                double direction = Math.atan2( x, z );
                double theta = Math.atan2( Math.sqrt( x * x + z * z ), y );

                double dirX = Math.sin( direction );
                double dirY = Math.sin( theta );
                double dirZ = Math.cos( direction );

                double dirXmultiplier = Math.cos( theta );

                float cloudRed = EnvironmentRenderingManager.SKY.cloudColor[ 0 ];
                float cloudGreen = EnvironmentRenderingManager.SKY.cloudColor[ 1 ];
                float cloudBlue = EnvironmentRenderingManager.SKY.cloudColor[ 2 ];

                for( int vtx = 0; vtx < 4; ++ vtx ) {
                    double locU = ( vtx & 2 ) - 1;
                    double locV = ( vtx + 1 & 2 ) - 1;

                    int spriteU = cld.type & 3;
                    int spriteV = cld.type / 4;

                    double sprU = spriteU / 4D;
                    double sprV = spriteV / 2D;

                    double texU = sprU + ( locU + 1 ) / 8;
                    double texV = sprV + ( locV + 1 ) / 4;

                    double posU = locU * size;
                    double posV = locV * size;
                    double posW = - posU * dirXmultiplier;

                    double posY = posU * dirY;
                    double posX = posW * dirX - posV * dirZ;
                    double posZ = posV * dirX + posW * dirZ;
                    buff.pos( px + posX, py + posY, pz + posZ )
                        .tex( texU, texV )
                        .color(
                            (float) colorRed * cloudRed,
                            (float) colorGreen * cloudGreen,
                            (float) colorBlue * cloudBlue,
                            (float) colorAlpha * (float) alpha * brightness
                        )
                        .endVertex();
                }

            }
        }

        tess.draw();
    }

    private void renderSkylight() {
        if( lights.isEmpty() ) return;
        float brightness = EnvironmentRenderingManager.SKY.skylightBrightness;
        if( brightness <= 0 ) return;

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();

        Minecraft.getInstance().getTextureManager().bindTexture( FOG );

        buff.begin( GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR );

        for( Light lgt : lights ) {
            double x = lgt.x;
            double y = lgt.y;
            double z = lgt.z;
            double size = lgt.scale;
            double colorRed = EnvironmentRenderingManager.SKY.skylightColor[ 0 ];
            double colorGreen = EnvironmentRenderingManager.SKY.skylightColor[ 1 ];
            double colorBlue = EnvironmentRenderingManager.SKY.skylightColor[ 2 ];

            double a = y > 0 ? y * 0.3 : 0;
            double distance = x * x + y * y + z * z;

            if( distance > 0.01D && lgt.age >= 0 ) {
                double alpha = 1;
                if( lgt.age < 70 ) {
                    alpha = lgt.age / 70F;
                }
                if( lgt.age > lgt.maxAge - 70 ) {
                    alpha = ( lgt.maxAge - lgt.age ) / 70F;
                }
                if( lgt.age > lgt.maxAge ) {
                    alpha = 0;
                }

                distance = 1 / Math.sqrt( distance );

                x *= distance;
                y *= distance;
                z *= distance;
                double px = x * 100;
                double py = y * 100;
                double pz = z * 100;

                double direction = Math.atan2( x, z );
                double theta = Math.atan2( Math.sqrt( x * x + z * z ), y );

                double dirX = Math.sin( direction );
                double dirY = Math.sin( theta );
                double dirZ = Math.cos( direction );

                double dirXmultiplier = Math.cos( theta );

                for( int vtx = 0; vtx < 4; ++ vtx ) {
                    double locU = ( vtx & 2 ) - 1;
                    double locV = ( vtx + 1 & 2 ) - 1;

                    int spriteU = lgt.type & 3;
                    int spriteV = lgt.type / 4;

                    double sprU = spriteU / 4D;
                    double sprV = spriteV / 2D;

                    double texU = sprU + ( locU + 1 ) / 8;
                    double texV = sprV + ( locV + 1 ) / 4;

                    double posU = locU * size;
                    double posV = locV * size;
                    double posW = - posU * dirXmultiplier;

                    double posY = posU * dirY;
                    double posX = posW * dirX - posV * dirZ;
                    double posZ = posV * dirX + posW * dirZ;
                    buff.pos( px + posX, py + posY, pz + posZ )
                        .tex( texU, texV )
                        .color(
                            (float) colorRed,
                            (float) colorGreen,
                            (float) colorBlue,
                            (float) alpha * brightness * (float) a
                        )
                        .endVertex();
                }
            }
        }

        tess.draw();
    }

    private void renderSatellite() {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();

        if( EnvironmentRenderingManager.SKY.moonBrightness > 0 ) {
            int moonPhase = EnvironmentRenderingManager.SKY.moonPhase;
            float moonRot = EnvironmentRenderingManager.SKY.moonRotation;

            Minecraft.getInstance().getTextureManager().bindTexture( MOONS[ moonPhase ] );

            GlStateManager.pushMatrix();
            GlStateManager.rotatef( moonStarRotationAngle, 0, 1, 0 );
            GlStateManager.rotatef( moonStarRotationShift, 0, 0, 1 );
            GlStateManager.rotatef( moonRot * 360, 1, 0, 0 );

            float mr = EnvironmentRenderingManager.SKY.moonColor[ 0 ];
            float mg = EnvironmentRenderingManager.SKY.moonColor[ 1 ];
            float mb = EnvironmentRenderingManager.SKY.moonColor[ 2 ];
            float ma = EnvironmentRenderingManager.SKY.moonBrightness;

            buff.begin( GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR );

            buff.pos( - 20, - 40, - 20 ).tex( 0, 0 ).color( mr, mg, mb, ma ).endVertex();
            buff.pos( 20, - 40, - 20 ).tex( 1, 0 ).color( mr, mg, mb, ma ).endVertex();
            buff.pos( 20, - 40, 20 ).tex( 1, 1 ).color( mr, mg, mb, ma ).endVertex();
            buff.pos( - 20, - 40, 20 ).tex( 0, 1 ).color( mr, mg, mb, ma ).endVertex();

            tess.draw();

            GlStateManager.popMatrix();
        }
    }

    private static final class Meteorite {
        double x;
        double y;
        double z;
        double xv;
        double yv;
        double zv;
        double len;
        double scale;
        float r;
        float g;
        float b;
        float a;
        int age;
        int maxAge;
    }

    private static final class Cloud {
        double x;
        double y;
        double z;
        double xv;
        double yv;
        double zv;
        double scale;
        float r;
        float g;
        float b;
        float a;
        int age;
        int maxAge;
        int type;
    }

    private static final class Light {
        double x;
        double y;
        double z;
        double xv;
        double yv;
        double zv;
        double scale;
        double age;
        int maxAge;
        int type;
    }
}