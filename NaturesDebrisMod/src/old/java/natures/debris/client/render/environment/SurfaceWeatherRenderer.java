/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.render.environment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.IRenderHandler;

import java.util.Random;

// TODO Re-evaluate
public class SurfaceWeatherRenderer implements IRenderHandler {
    public static final ResourceLocation RAIN_TEXTURES = new ResourceLocation("modernity:textures/environment/rain.png");
    public static final ResourceLocation HAIL_TEXTURES = new ResourceLocation("modernity:textures/environment/hail.png");
    public static final ResourceLocation DRIZZLE_TEXTURES = new ResourceLocation("modernity:textures/environment/drizzle.png");
    public static final ResourceLocation SHOWERS_TEXTURES = new ResourceLocation("modernity:textures/environment/showers.png");
    public static final ResourceLocation WET_SNOW_TEXTURES = new ResourceLocation("modernity:textures/environment/wet_snow.png");
    public static final ResourceLocation SNOW_TEXTURES = new ResourceLocation("modernity:textures/environment/snow.png");
    public static final ResourceLocation HEAVY_SNOW_TEXTURES = new ResourceLocation("modernity:textures/environment/heavy_snow.png");

    private final float[] rainXCoords = new float[1024];
    private final float[] rainZCoords = new float[1024];
    Random random = new Random();

    public SurfaceWeatherRenderer() {
        for (int z = 0; z < 32; ++z) {
            for (int x = 0; x < 32; ++x) {
                float lx = x - 16;
                float lz = z - 16;
                float distance = MathHelper.sqrt(lx * lx + lz * lz);
                rainXCoords[z << 5 | x] = -lz / distance * 0.5F; // Perpendicular vector: [-z, x]
                rainZCoords[z << 5 | x] = lx / distance * 0.5F;
            }
        }
    }

    @Override
    public void render(int currentTick, float partialTicks, ClientWorld world, Minecraft mc) {
//
//        if( world.dimension instanceof IEnvironmentDimension ) {
//            ( (IEnvironmentDimension) world.dimension )
//                .updatePrecipitation( EnvironmentRenderingManager.PRECIPITATION );
//        }
//
//
//        int level = EnvironmentRenderingManager.PRECIPITATION.level;
//        float precipitationStrength = EnvironmentRenderingManager.PRECIPITATION.strength;
//
//        float[] uv = new float[ 2 ];
//
//        if( level > 0 && precipitationStrength > 0 ) {
//            mc.gameRenderer.enableLightmap();
//            Entity entity = mc.getRenderViewEntity();
//            if( entity == null ) return;
//
//            ActiveRenderInfo renderInfo = mc.gameRenderer.getActiveRenderInfo();
//            Vec3d pv = renderInfo.getProjectedView();
//
//            int blockX = MathHelper.floor( pv.x );
//            int blockY = MathHelper.floor( pv.y );
//            int blockZ = MathHelper.floor( pv.z );
//
//            Tessellator tess = Tessellator.getInstance();
//            BufferBuilder buff = tess.getBuffer();
//
//            GlStateManager.disableCull();
//            GlStateManager.normal3f( 0, 1, 0 );
//            GlStateManager.enableBlend();
//            GlStateManager.depthMask( true );
//            GlStateManager.blendFuncSeparate( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0 );
//            GlStateManager.alphaFunc( 516, 0.1F );
//
//            int ypos = MathHelper.floor( pv.y );
//            int renderDistance = 5;
//
//            if( mc.gameSettings.fancyGraphics ) {
//                renderDistance = 10;
//            }
//
//            ResourceLocation texture = null;
//            buff.setTranslation( - pv.x, - pv.y, - pv.z );
//            GlStateManager.color4f( 1, 1, 1, 1 );
//            MovingBlockPos mpos = new MovingBlockPos();
//
//            for( int z = blockZ - renderDistance; z <= blockZ + renderDistance; ++ z ) {
//                for( int x = blockX - renderDistance; x <= blockX + renderDistance; ++ x ) {
//                    int rainCoordsIndex = ( z - blockZ + 16 ) * 32 + x - blockX + 16;
//
//                    float rainX = rainXCoords[ rainCoordsIndex ];
//                    float rainZ = rainZCoords[ rainCoordsIndex ];
//
//                    mpos.setPos( x, 0, z );
//                    Biome vbiome = world.getBiome( mpos );
//                    if( ! ( vbiome instanceof ModernityBiome ) ) return;
//                    ModernityBiome biome = (ModernityBiome) vbiome;
//                    IPrecipitationFunction func = biome.getPrecipitationFunction();
//
//                    IPrecipitation prec = func.computePrecipitation( level );
//                    ResourceLocation precTexture = prec.getTexture();
//
//                    if( prec.shouldRender() ) {
//                        int precipitationHeight = prec.getHeight( world, mpos.getX(), mpos.getZ() );
//                        int minY = Math.max( blockY - renderDistance, precipitationHeight );
//                        int maxY = Math.max( blockY + renderDistance, precipitationHeight );
//
//                        int y = precipitationHeight < ypos
//                                ? ypos
//                                : precipitationHeight;
//
//                        if( minY != maxY ) {
//                            random.setSeed( x * x * 3121 + x * 45238971 ^ z * z * 418711 + z * 13761 );
//                            // Swap textures if necessary
//                            // '!=' is much faster than '!equals', we just have to implement everything the right way
//                            if( texture != precTexture ) {
//                                if( texture != null ) {
//                                    tess.draw();
//                                }
//
//                                texture = precTexture;
//                                mc.getTextureManager().bindTexture( texture );
//                                buff.begin( 7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP );
//                            }
//
//                            prec.computeUVOffset( uv, currentTick, partialTicks, random, x, z );
//
//                            mpos.setPos( x, y, z );
//
//                            // Compute lighting
//                            int light = world.getCombinedLight( mpos, 0 );
//                            int lightU = light >> 16 & 65535;
//                            int lightV = light & 65535;
//
//                            // Compute color
//                            float xloc = x + 0.5F - (float) pv.x;
//                            float zloc = z + 0.5F - (float) pv.z;
//
//                            float distSq = ( xloc * xloc + zloc * zloc ) / ( renderDistance * renderDistance );
//                            float alpha = ( Math.max( 0, 1 - distSq ) * 0.5F + 0.5F ) * precipitationStrength;
//
//                            int color = prec.getColor( world, mpos.getX(), mpos.getZ() );
//                            float r = ( color >>> 16 & 0xff ) / 255F;
//                            float g = ( color >>> 8 & 0xff ) / 255F;
//                            float b = ( color & 0xff ) / 255F;
//                            float a = prec.getAlpha( world, mpos.getX(), mpos.getZ() ) * alpha;
//
//                            // Draw precipitation
//                            buff.pos( x - rainX + 0.5, maxY, z - rainZ + 0.5 ).tex( 0 + uv[ 0 ], minY * 0.25 + uv[ 1 ] ).color( r, g, b, a ).lightmap( lightU, lightV ).endVertex();
//                            buff.pos( x + rainX + 0.5, maxY, z + rainZ + 0.5 ).tex( 1 + uv[ 0 ], minY * 0.25 + uv[ 1 ] ).color( r, g, b, a ).lightmap( lightU, lightV ).endVertex();
//                            buff.pos( x + rainX + 0.5, minY, z + rainZ + 0.5 ).tex( 1 + uv[ 0 ], maxY * 0.25 + uv[ 1 ] ).color( r, g, b, a ).lightmap( lightU, lightV ).endVertex();
//                            buff.pos( x - rainX + 0.5, minY, z - rainZ + 0.5 ).tex( 0 + uv[ 0 ], maxY * 0.25 + uv[ 1 ] ).color( r, g, b, a ).lightmap( lightU, lightV ).endVertex();
//                        }
//                    }
//                }
//            }
//
//            if( texture != null ) {
//                tess.draw();
//            }
//
//            buff.setTranslation( 0, 0, 0 );
//            GlStateManager.enableCull();
//            GlStateManager.disableBlend();
//            GlStateManager.alphaFunc( 516, 0.1F );
//            mc.gameRenderer.disableLightmap();
//        }
    }
}
