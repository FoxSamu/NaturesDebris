/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 20 - 2019
 * Author: rgsw
 */

package modernity.client.render.block;

import com.google.common.collect.Maps;
import modernity.api.block.IFluidOverlayBlock;
import modernity.api.block.fluid.ICustomRenderFluid;
import modernity.api.block.fluid.IGaseousFluid;
import modernity.common.fluid.MurkyWaterFluid;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;

import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Renders fluids that implement {@link ICustomRenderFluid}. This renders gaseous fluids upside down and handles custom
 * overlay-causing blocks (see {@link IFluidOverlayBlock}).
 */
@OnlyIn( Dist.CLIENT )
public class CustomFluidRenderer implements ISelectiveResourceReloadListener {
    private boolean updateAtlasSprites = true;

    public static final ResourceLocation LOCATION_LAVA_STILL = new ResourceLocation( "block/lava_still" );
    public static final ResourceLocation LOCATION_WATER_STILL = new ResourceLocation( "block/water_still" );
    public static final ResourceLocation LOCATION_LAVA_FLOW = new ResourceLocation( "block/lava_flow" );
    public static final ResourceLocation LOCATION_WATER_FLOW = new ResourceLocation( "block/water_flow" );
    public static final ResourceLocation LOCATION_WATER_OVERLAY = new ResourceLocation( "block/water_overlay" );

    private HashMap<Fluid, TextureAtlasSprite[]> customSprites = Maps.newHashMap();
    private TextureAtlasSprite[] lavaSprites = new TextureAtlasSprite[ 2 ];
    private TextureAtlasSprite[] waterSprites = new TextureAtlasSprite[ 2 ];
    private TextureAtlasSprite overlaySprite;

    public CustomFluidRenderer() {
    }

    protected void updateSprites() {
        AtlasTexture map = Minecraft.getInstance().getTextureMap();

        this.customSprites = Maps.newHashMap();
        this.lavaSprites = new TextureAtlasSprite[ 2 ];
        this.waterSprites = new TextureAtlasSprite[ 2 ];

        this.lavaSprites[ 0 ] = map.getSprite( LOCATION_LAVA_STILL );
        this.lavaSprites[ 1 ] = map.getSprite( LOCATION_LAVA_FLOW );

        this.waterSprites[ 0 ] = map.getSprite( LOCATION_WATER_STILL );
        this.waterSprites[ 1 ] = map.getSprite( LOCATION_WATER_FLOW );

        this.overlaySprite = map.getSprite( LOCATION_WATER_OVERLAY );

        // Collect sprites of custom fluids
        Registry<Fluid> fluids = Registry.FLUID;
        for( Fluid fluid : fluids.stream().collect( Collectors.toSet() ) ) {
            if( fluid instanceof ICustomRenderFluid ) {
                ICustomRenderFluid crFluid = (ICustomRenderFluid) fluid;

                TextureAtlasSprite still = map.getSprite( crFluid.getStill() );
                TextureAtlasSprite flow = map.getSprite( crFluid.getFlowing() );

                ResourceLocation overlayLoc = crFluid.getOverlay();
                TextureAtlasSprite overlay = null;
                if( overlayLoc != null ) {
                    overlay = map.getSprite( overlayLoc );
                }

                this.customSprites.put( fluid, new TextureAtlasSprite[] {
                    still, flow, overlay
                } );
            }
        }
        updateAtlasSprites = false;
    }

    @Override
    public void onResourceManagerReload( IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate ) {
        if( resourcePredicate.test( VanillaResourceType.TEXTURES ) ) {
            updateAtlasSprites = true;
        }
    }

    private static boolean isAdjacentFluidSameAs( IBlockReader world, BlockPos pos, Direction side, IFluidState state ) {
        BlockPos offset = pos.offset( side );
        IFluidState otherState = world.getFluidState( offset );
        return areEquivalent( otherState.getFluid(), state.getFluid() );
    }

    private static boolean shouldRenderSideForHeight( IBlockReader world, BlockPos pos, Direction face, float height, boolean gas ) {
        BlockPos offset = pos.offset( face );
        BlockState state = world.getBlockState( offset );
        if( state.isSolid() ) {
            VoxelShape shape1 = gas ?
                                VoxelShapes.create( 0, 1 - height, 0, 1, 1, 1 ) :
                                VoxelShapes.create( 0, 0, 0, 1, height, 1 );
            VoxelShape shape2 = state.getRenderShape( world, offset );
            return VoxelShapes.isCubeSideCovered( shape1, shape2, face );
        } else {
            return false;
        }
    }

    /**
     * Checks if fluids are equivalent, taking the ModernizedWater-Water combination as an exceptional case: these are
     * always equivalent (they're both water, not?).
     */
    private static boolean areEquivalent( Fluid fluidA, Fluid fluidB ) {
        if( fluidA instanceof WaterFluid && fluidB instanceof MurkyWaterFluid ) {
            return true;
        } else if( fluidB instanceof WaterFluid && fluidA instanceof MurkyWaterFluid ) {
            return true;
        } else {
            return fluidA.isEquivalentTo( fluidB );
        }
    }

    /**
     * Renders the specified fluid in the buffer builder
     */
    public boolean render( IEnviromentBlockReader world, BlockPos pos, BufferBuilder buffer, IFluidState state ) {
        if( updateAtlasSprites ) {
            updateSprites();
        }
        boolean isLava = state.isTagged( FluidTags.LAVA );
        if( Minecraft.getInstance().world == null ) {
            isLava = state.getFluid() instanceof LavaFluid;
        }
        boolean custom = false; // MAYBE: Is this still needed?

        boolean gas = state.getFluid() instanceof IGaseousFluid;
        Direction down = gas ? Direction.UP : Direction.DOWN;
        Direction up = gas ? Direction.DOWN : Direction.UP;
        int fall = gas ? - 1 : 1;

        TextureAtlasSprite overlayTex = this.overlaySprite;

        // Obtain sprites and color
        TextureAtlasSprite[] fluidTex = isLava ? this.lavaSprites : this.waterSprites;
        int color = isLava ? 0xffffff : BiomeColors.getWaterColor( world, pos );

        if( state.getFluid() instanceof ICustomRenderFluid ) {
            fluidTex = this.customSprites.get( state.getFluid() );

            // No custom sprites cached, take them in cache now
            if( fluidTex == null ) {
                AtlasTexture texturemap = Minecraft.getInstance().getTextureMap();
                Fluid fluid = state.getFluid();
                ICustomRenderFluid crFluid = (ICustomRenderFluid) fluid;

                TextureAtlasSprite still = texturemap.getSprite( crFluid.getStill() );
                TextureAtlasSprite flow = texturemap.getSprite( crFluid.getFlowing() );
                TextureAtlasSprite overlay = null;

                ResourceLocation overlayLoc = crFluid.getOverlay();
                if( overlayLoc != null ) {
                    overlay = texturemap.getSprite( overlayLoc );
                }

                this.customSprites.put( fluid, fluidTex = new TextureAtlasSprite[] {
                    still, flow, overlay
                } );
            }
            color = ( (ICustomRenderFluid) state.getFluid() ).getColor( state, pos, world );

            if( fluidTex[ 2 ] != null ) {
                overlayTex = fluidTex[ 2 ];
                isLava = false;
            }
            custom = true;
        }

        float red = (float) ( color >> 16 & 255 ) / 255.0F;
        float green = (float) ( color >> 8 & 255 ) / 255.0F;
        float blue = (float) ( color & 255 ) / 255.0F;


        // Determine which sides should render
        boolean renderUp = ! isAdjacentFluidSameAs( world, pos, up, state );
        boolean renderDown = ! isAdjacentFluidSameAs( world, pos, down, state ) && ! shouldRenderSideForHeight( world, pos, down, 0.8888889F, gas );
        boolean renderNorth = ! isAdjacentFluidSameAs( world, pos, Direction.NORTH, state );
        boolean renderSouth = ! isAdjacentFluidSameAs( world, pos, Direction.SOUTH, state );
        boolean renderWest = ! isAdjacentFluidSameAs( world, pos, Direction.WEST, state );
        boolean renderEast = ! isAdjacentFluidSameAs( world, pos, Direction.EAST, state );

        if( ! renderUp && ! renderDown && ! renderEast && ! renderWest && ! renderNorth && ! renderSouth ) {
            // Nothing has to be rendered
            return false;
        } else {
            boolean somethingRendered = false;

            // Determine the height of each corner
            float nwHeight = this.getFluidHeight( world, pos, state.getFluid(), state, fall );
            float swHeight = this.getFluidHeight( world, pos.south(), state.getFluid(), state, fall );
            float seHeight = this.getFluidHeight( world, pos.east().south(), state.getFluid(), state, fall );
            float neHeight = this.getFluidHeight( world, pos.east(), state.getFluid(), state, fall );

            double dx = pos.getX();
            double dy = pos.getY();
            double dz = pos.getZ();

            // Render up
            if( renderUp && ! shouldRenderSideForHeight( world, pos, up, Math.min( Math.min( nwHeight, swHeight ), Math.min( seHeight, neHeight ) ), gas ) ) {
                somethingRendered = true;

                // Stop z-fighting
                nwHeight -= 0.001F;
                swHeight -= 0.001F;
                seHeight -= 0.001F;
                neHeight -= 0.001F;


                Vec3d flowDirection = state.getFlow( world, pos );
                float texU1;
                float texU2;
                float texU3;
                float texU4;
                float texV1;
                float texV2;
                float texV3;
                float texV4;
                if( flowDirection.x == 0.0D && flowDirection.z == 0.0D ) {
                    // Not flowing, use still texture
                    TextureAtlasSprite texture = fluidTex[ 0 ];
                    texU1 = texture.getInterpolatedU( 0.0D );
                    texV1 = texture.getInterpolatedV( 0.0D );
                    texU2 = texU1;
                    texV2 = texture.getInterpolatedV( 16.0D );
                    texU3 = texture.getInterpolatedU( 16.0D );
                    texV3 = texV2;
                    texU4 = texU3;
                    texV4 = texV1;
                } else {
                    // Flowing, compute texture coords from flow texture
                    TextureAtlasSprite texture = fluidTex[ 1 ];
                    float dir = (float) MathHelper.atan2( flowDirection.z, flowDirection.x ) - (float) Math.PI / 2F;
                    float sine = MathHelper.sin( dir ) * 0.25F;
                    float cosine = MathHelper.cos( dir ) * 0.25F;
                    texU1 = texture.getInterpolatedU( 8.0F + ( - cosine - sine ) * 16.0F );
                    texV1 = texture.getInterpolatedV( 8.0F + ( - cosine + sine ) * 16.0F );
                    texU2 = texture.getInterpolatedU( 8.0F + ( - cosine + sine ) * 16.0F );
                    texV2 = texture.getInterpolatedV( 8.0F + ( cosine + sine ) * 16.0F );
                    texU3 = texture.getInterpolatedU( 8.0F + ( cosine + sine ) * 16.0F );
                    texV3 = texture.getInterpolatedV( 8.0F + ( cosine - sine ) * 16.0F );
                    texU4 = texture.getInterpolatedU( 8.0F + ( cosine - sine ) * 16.0F );
                    texV4 = texture.getInterpolatedV( 8.0F + ( - cosine - sine ) * 16.0F );
                }

                // Compute light
                int light = this.getCombinedLightUpMax( world, pos );
                int lightU = light >> 16 & '\uffff';
                int lightV = light & '\uffff';

                float r = 1.0F * red;
                float g = 1.0F * green;
                float b = 1.0F * blue;

                if( gas ) {
                    buffer.pos( dx + 1.0D, dy + ( 1 - neHeight ), dz + 0.0D ).color( r, g, b, 1.0F ).tex( texU4, texV4 ).lightmap( lightU, lightV ).endVertex();
                    buffer.pos( dx + 1.0D, dy + ( 1 - seHeight ), dz + 1.0D ).color( r, g, b, 1.0F ).tex( texU3, texV3 ).lightmap( lightU, lightV ).endVertex();
                    buffer.pos( dx + 0.0D, dy + ( 1 - swHeight ), dz + 1.0D ).color( r, g, b, 1.0F ).tex( texU2, texV2 ).lightmap( lightU, lightV ).endVertex();
                    buffer.pos( dx + 0.0D, dy + ( 1 - nwHeight ), dz + 0.0D ).color( r, g, b, 1.0F ).tex( texU1, texV1 ).lightmap( lightU, lightV ).endVertex();
                } else {
                    buffer.pos( dx + 0.0D, dy + nwHeight, dz + 0.0D ).color( r, g, b, 1.0F ).tex( texU1, texV1 ).lightmap( lightU, lightV ).endVertex();
                    buffer.pos( dx + 0.0D, dy + swHeight, dz + 1.0D ).color( r, g, b, 1.0F ).tex( texU2, texV2 ).lightmap( lightU, lightV ).endVertex();
                    buffer.pos( dx + 1.0D, dy + seHeight, dz + 1.0D ).color( r, g, b, 1.0F ).tex( texU3, texV3 ).lightmap( lightU, lightV ).endVertex();
                    buffer.pos( dx + 1.0D, dy + neHeight, dz + 0.0D ).color( r, g, b, 1.0F ).tex( texU4, texV4 ).lightmap( lightU, lightV ).endVertex();
                }

                // Render surface visible underwater when not below solid block
                if( state.shouldRenderSides( world, pos.up( fall ) ) ) {
                    if( gas ) {
                        buffer.pos( dx + 0.0D, dy + ( 1 - swHeight ), dz + 1.0D ).color( r, g, b, 1.0F ).tex( texU2, texV2 ).lightmap( lightU, lightV ).endVertex();
                        buffer.pos( dx + 1.0D, dy + ( 1 - seHeight ), dz + 1.0D ).color( r, g, b, 1.0F ).tex( texU3, texV3 ).lightmap( lightU, lightV ).endVertex();
                        buffer.pos( dx + 1.0D, dy + ( 1 - neHeight ), dz + 0.0D ).color( r, g, b, 1.0F ).tex( texU4, texV4 ).lightmap( lightU, lightV ).endVertex();
                        buffer.pos( dx + 0.0D, dy + ( 1 - nwHeight ), dz + 0.0D ).color( r, g, b, 1.0F ).tex( texU1, texV1 ).lightmap( lightU, lightV ).endVertex();
                    } else {
                        buffer.pos( dx + 0.0D, dy + nwHeight, dz + 0.0D ).color( r, g, b, 1.0F ).tex( texU1, texV1 ).lightmap( lightU, lightV ).endVertex();
                        buffer.pos( dx + 1.0D, dy + neHeight, dz + 0.0D ).color( r, g, b, 1.0F ).tex( texU4, texV4 ).lightmap( lightU, lightV ).endVertex();
                        buffer.pos( dx + 1.0D, dy + seHeight, dz + 1.0D ).color( r, g, b, 1.0F ).tex( texU3, texV3 ).lightmap( lightU, lightV ).endVertex();
                        buffer.pos( dx + 0.0D, dy + swHeight, dz + 1.0D ).color( r, g, b, 1.0F ).tex( texU2, texV2 ).lightmap( lightU, lightV ).endVertex();
                    }
                }
            }

            if( renderDown ) {
                somethingRendered = true;

                // Compute UV coords
                float minU = fluidTex[ 0 ].getMinU();
                float maxU = fluidTex[ 0 ].getMaxU();
                float minV = fluidTex[ 0 ].getMinV();
                float maxV = fluidTex[ 0 ].getMaxV();

                // Compute light
                int light = this.getCombinedLightUpMax( world, pos.down( fall ) );
                int lightU = light >> 16 & 0xffff;
                int lightV = light & 0xffff;

                float r = 0.5F * red;
                float g = 0.5F * green;
                float b = 0.5F * blue;

                if( gas ) {
                    buffer.pos( dx + 1.0D, dy + 1, dz + 1.0D ).color( r, g, b, 1.0F ).tex( maxU, maxV ).lightmap( lightU, lightV ).endVertex();
                    buffer.pos( dx + 1.0D, dy + 1, dz + 0.0D ).color( r, g, b, 1.0F ).tex( maxU, minV ).lightmap( lightU, lightV ).endVertex();
                    buffer.pos( dx + 0.0D, dy + 1, dz + 0.0D ).color( r, g, b, 1.0F ).tex( minU, minV ).lightmap( lightU, lightV ).endVertex();
                    buffer.pos( dx + 0.0D, dy + 1, dz + 1.0D ).color( r, g, b, 1.0F ).tex( minU, maxV ).lightmap( lightU, lightV ).endVertex();
                } else {
                    buffer.pos( dx + 0.0D, dy + 0, dz + 1.0D ).color( r, g, b, 1.0F ).tex( minU, maxV ).lightmap( lightU, lightV ).endVertex();
                    buffer.pos( dx + 0.0D, dy + 0, dz + 0.0D ).color( r, g, b, 1.0F ).tex( minU, minV ).lightmap( lightU, lightV ).endVertex();
                    buffer.pos( dx + 1.0D, dy + 0, dz + 0.0D ).color( r, g, b, 1.0F ).tex( maxU, minV ).lightmap( lightU, lightV ).endVertex();
                    buffer.pos( dx + 1.0D, dy + 0, dz + 1.0D ).color( r, g, b, 1.0F ).tex( maxU, maxV ).lightmap( lightU, lightV ).endVertex();
                }
            }

            // Render sides
            for( int intSide = 0; intSide < 4; ++ intSide ) {
                // Compute some attributes for this side
                float leftHeight;
                float rightHeight;
                double minX;
                double minZ;
                double maxX;
                double maxZ;
                Direction side;
                boolean shouldRender;
                if( intSide == 0 ) {
                    // North
                    leftHeight = nwHeight;
                    rightHeight = neHeight;
                    minX = dx;
                    maxX = dx + 1;
                    minZ = dz + 0.001;
                    maxZ = dz + 0.001;
                    side = Direction.NORTH;
                    shouldRender = renderNorth;
                } else if( intSide == 1 ) {
                    // South
                    leftHeight = seHeight;
                    rightHeight = swHeight;
                    minX = dx + 1.0;
                    maxX = dx;
                    minZ = dz + 1.0 - 0.001;
                    maxZ = dz + 1.0 - 0.001;
                    side = Direction.SOUTH;
                    shouldRender = renderSouth;
                } else if( intSide == 2 ) {
                    // West
                    leftHeight = swHeight;
                    rightHeight = nwHeight;
                    minX = dx + 0.001;
                    maxX = dx + 0.001;
                    minZ = dz + 1.0;
                    maxZ = dz;
                    side = Direction.WEST;
                    shouldRender = renderWest;
                } else {
                    // East
                    leftHeight = neHeight;
                    rightHeight = seHeight;
                    minX = dx + 1.0 - 0.001;
                    maxX = dx + 1.0 - 0.001;
                    minZ = dz;
                    maxZ = dz + 1.0;
                    side = Direction.EAST;
                    shouldRender = renderEast;
                }

                // Render this side
                if( shouldRender && ! shouldRenderSideForHeight( world, pos, side, Math.max( leftHeight, rightHeight ), gas ) ) {
                    somethingRendered = true;

                    // Determine the side texture seen from inside the fluid
                    BlockPos offsetPos = pos.offset( side );
                    TextureAtlasSprite texture = fluidTex[ 1 ];
                    if( ! isLava ) {
                        BlockState blockstate = world.getBlockState( offsetPos );
                        if( blockstate.getBlock() == Blocks.GLASS || blockstate.getBlock() instanceof StainedGlassBlock || blockstate.getBlock() instanceof IFluidOverlayBlock ) {
                            texture = overlayTex;
                        }
                    }

                    // Compute UV coords
                    float minU = texture.getInterpolatedU( 0.0D );
                    float maxU = texture.getInterpolatedU( 8.0D );
                    float leftV = texture.getInterpolatedV( ( 1.0F - leftHeight ) * 16.0F * 0.5F );
                    float rightV = texture.getInterpolatedV( ( 1.0F - rightHeight ) * 16.0F * 0.5F );
                    float maxV = texture.getInterpolatedV( 8.0D );

                    // Compute light
                    int light = this.getCombinedLightUpMax( world, offsetPos );
                    int lightU = light >> 16 & '\uffff';
                    int lightV = light & '\uffff';

                    float brightness = intSide < 2 ? 0.8F : 0.6F;
                    float r = 1.0F * brightness * red;
                    float g = 1.0F * brightness * green;
                    float b = 1.0F * brightness * blue;


                    if( gas ) {
                        buffer.pos( minX, dy + 1, minZ ).color( r, g, b, 1.0F ).tex( minU, maxV ).lightmap( lightU, lightV ).endVertex();
                        buffer.pos( maxX, dy + 1, maxZ ).color( r, g, b, 1.0F ).tex( maxU, maxV ).lightmap( lightU, lightV ).endVertex();
                        buffer.pos( maxX, dy + ( 1 - rightHeight ), maxZ ).color( r, g, b, 1.0F ).tex( maxU, rightV ).lightmap( lightU, lightV ).endVertex();
                        buffer.pos( minX, dy + ( 1 - leftHeight ), minZ ).color( r, g, b, 1.0F ).tex( minU, leftV ).lightmap( lightU, lightV ).endVertex();
                    } else {
                        buffer.pos( minX, dy + leftHeight, minZ ).color( r, g, b, 1.0F ).tex( minU, leftV ).lightmap( lightU, lightV ).endVertex();
                        buffer.pos( maxX, dy + rightHeight, maxZ ).color( r, g, b, 1.0F ).tex( maxU, rightV ).lightmap( lightU, lightV ).endVertex();
                        buffer.pos( maxX, dy, maxZ ).color( r, g, b, 1.0F ).tex( maxU, maxV ).lightmap( lightU, lightV ).endVertex();
                        buffer.pos( minX, dy, minZ ).color( r, g, b, 1.0F ).tex( minU, maxV ).lightmap( lightU, lightV ).endVertex();
                    }

                    // Block overlay
                    if( texture != this.overlaySprite ) {
                        if( gas ) {
                            buffer.pos( minX, dy + ( 1 - leftHeight ), minZ ).color( r, g, b, 1.0F ).tex( minU, leftV ).lightmap( lightU, lightV ).endVertex();
                            buffer.pos( maxX, dy + ( 1 - rightHeight ), maxZ ).color( r, g, b, 1.0F ).tex( maxU, rightV ).lightmap( lightU, lightV ).endVertex();
                            buffer.pos( maxX, dy + 1, maxZ ).color( r, g, b, 1.0F ).tex( maxU, maxV ).lightmap( lightU, lightV ).endVertex();
                            buffer.pos( minX, dy + 1, minZ ).color( r, g, b, 1.0F ).tex( minU, maxV ).lightmap( lightU, lightV ).endVertex();
                        } else {
                            buffer.pos( minX, dy, minZ ).color( r, g, b, 1.0F ).tex( minU, maxV ).lightmap( lightU, lightV ).endVertex();
                            buffer.pos( maxX, dy, maxZ ).color( r, g, b, 1.0F ).tex( maxU, maxV ).lightmap( lightU, lightV ).endVertex();
                            buffer.pos( maxX, dy + rightHeight, maxZ ).color( r, g, b, 1.0F ).tex( maxU, rightV ).lightmap( lightU, lightV ).endVertex();
                            buffer.pos( minX, dy + leftHeight, minZ ).color( r, g, b, 1.0F ).tex( minU, leftV ).lightmap( lightU, lightV ).endVertex();
                        }
                    }
                }
            }
            return somethingRendered;
        }
    }

    private int getCombinedLightUpMax( IEnviromentBlockReader reader, BlockPos pos ) {
        int myLight = reader.getCombinedLight( pos, 0 );
        int uplight = reader.getCombinedLight( pos.up(), 0 );
        int myLightU = myLight & 255;
        int upLightU = uplight & 255;
        int myLightV = myLight >> 16 & 255;
        int upLightV = uplight >> 16 & 255;
        return ( myLightU > upLightU ? myLightU : upLightU ) | ( myLightV > upLightV ? myLightV : upLightV ) << 16;
    }

    /**
     * Gets the fluid height at the west-north corner of the specified block pos.
     */
    private float getFluidHeight( IEnviromentBlockReader world, BlockPos pos, Fluid fluid, IFluidState fluidState, int fall ) {
        if( Minecraft.getInstance().world == null ) {
            return fluidState.func_223408_f();
        }
        int weight = 0;
        float height = 0.0F;

        int w = 10;
        if( fluid instanceof ICustomRenderFluid ) {
            w = ( (ICustomRenderFluid) fluid ).getSourceSlopeWeight();
        }

        for( int side = 0; side < 4; ++ side ) {
            BlockPos offPos = pos.add( - ( side & 1 ), 0, - ( side >> 1 & 1 ) );
            if( world.getFluidState( offPos.up( fall ) ).getFluid().isEquivalentTo( fluid ) ) {
                return 1.0F;
            }

            IFluidState state = world.getFluidState( offPos );
            if( areEquivalent( state.getFluid(), fluid ) ) {
                if( state.isSource() ) {
                    if( w < 0 ) {
                        return state.func_223408_f();
                    } else {
                        height += state.func_223408_f() * w;
                        weight += w;
                    }
                } else {
                    height += state.func_223408_f();
                    ++ weight;
                }
            } else if( ! world.getBlockState( offPos ).getMaterial().isSolid() ) {
                ++ weight;
            }
        }

        return height / (float) weight;
    }
}
