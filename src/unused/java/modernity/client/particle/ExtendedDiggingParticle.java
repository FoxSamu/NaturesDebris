/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 18 - 2020
 * Author: rgsw
 */

package modernity.client.particle;

import modernity.api.block.ICustomColoredParticlesBlock;
import modernity.api.util.ColorUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn( Dist.CLIENT )
@SuppressWarnings( "deprecation" )
public class ExtendedDiggingParticle extends SpriteTexturedParticle {
    private final BlockState sourceState;
    private final Block sourceBlock;
    private BlockPos sourcePos;
    private final float texU;
    private final float texV;

    public ExtendedDiggingParticle( World world, double x, double y, double z, double xv, double yv, double zv, BlockState state ) {
        super( world, x, y, z, xv, yv, zv );
        setSprite(
            Minecraft.getInstance()
                     .getBlockRendererDispatcher()
                     .getBlockModelShapes()
                     .getTexture( state )
        );

        this.sourceState = state;
        this.sourceBlock = state.getBlock();

        this.particleGravity = 1;

        this.particleRed = 0.6F;
        this.particleGreen = 0.6F;
        this.particleBlue = 0.6F;

        this.particleScale /= 2;

        this.texU = rand.nextFloat() * 3;
        this.texV = rand.nextFloat() * 3;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.TERRAIN_SHEET;
    }

    public ExtendedDiggingParticle setBlockPos( BlockPos pos ) {
        updateSprite( pos );
        sourcePos = pos;

        if( sourceBlock instanceof ICustomColoredParticlesBlock ) {

            ICustomColoredParticlesBlock ccpb = (ICustomColoredParticlesBlock) sourceBlock;
            multiplyColor( ccpb.getColor( world, pos, sourceState ) );

        } else if( sourceBlock != Blocks.GRASS_BLOCK && sourceBlock != Blocks.CAULDRON ) {
            // Fix colored cauldron particles vanilla bug
            multiplyColor( pos );
        }
        return this;
    }

    public ExtendedDiggingParticle init() {
        sourcePos = new BlockPos( posX, posY, posZ );

        if( sourceBlock instanceof ICustomColoredParticlesBlock ) {

            ICustomColoredParticlesBlock ccpb = (ICustomColoredParticlesBlock) sourceBlock;
            multiplyColor( ccpb.getColor( world, sourcePos, sourceState ) );

        } else if( sourceBlock != Blocks.GRASS_BLOCK && sourceBlock != Blocks.CAULDRON ) {
            // Fix colored cauldron particles vanilla bug
            multiplyColor( sourcePos );
        }
        return this;
    }

    private ExtendedDiggingParticle updateSprite( BlockPos pos ) {
        if( pos != null )
            setSprite(
                Minecraft.getInstance()
                         .getBlockRendererDispatcher()
                         .getBlockModelShapes()
                         .getTexture( sourceState, world, pos )
            );
        return this;
    }

    protected void multiplyColor( @Nullable BlockPos pos ) {
        int col = Minecraft.getInstance()
                           .getBlockColors()
                           .getColor( sourceState, world, pos, 0 );

        multiplyColor( col );
    }

    protected void multiplyColor( int col ) {
        particleRed *= ColorUtil.redf( col );
        particleGreen *= ColorUtil.greenf( col );
        particleBlue *= ColorUtil.bluef( col );
    }

    @Override
    protected float getMinU() {
        return sprite.getInterpolatedU( ( texU + 1 ) * 4 );
    }

    @Override
    protected float getMaxU() {
        return sprite.getInterpolatedU( texU * 4 );
    }

    @Override
    protected float getMinV() {
        return sprite.getInterpolatedV( texV * 4 );
    }

    @Override
    protected float getMaxV() {
        return sprite.getInterpolatedV( ( texV + 1 ) * 4 );
    }

    @Override
    public int getBrightnessForRender( float partialTick ) {
        int parentBrightness = super.getBrightnessForRender( partialTick );
        int myBrightness = 0;
        if( world.isBlockLoaded( sourcePos ) ) {
            myBrightness = world.getCombinedLight( sourcePos, 0 );
        }

        return parentBrightness == 0 ? myBrightness : parentBrightness;
    }

    public static void addBlockDestroyEffects( ParticleManager manager, World world, BlockPos pos, BlockState state ) {
        VoxelShape shape = state.getShape( world, pos );

        shape.forEachBox( ( minX, minY, minZ, maxX, maxY, maxZ ) -> {
            double xRange = Math.min( 1, maxX - minX );
            double yRange = Math.min( 1, maxY - minY );
            double zRange = Math.min( 1, maxZ - minZ );
            int xCount = Math.max( 2, MathHelper.ceil( xRange * 4 ) );
            int yCount = Math.max( 2, MathHelper.ceil( yRange * 4 ) );
            int zCount = Math.max( 2, MathHelper.ceil( zRange * 4 ) );

            for( int xi = 0; xi < xCount; ++ xi ) {
                for( int yi = 0; yi < yCount; ++ yi ) {
                    for( int zi = 0; zi < zCount; ++ zi ) {
                        double lx = ( xi + 0.5 ) / xCount;
                        double ly = ( yi + 0.5 ) / yCount;
                        double lz = ( zi + 0.5 ) / zCount;

                        double gx = lx * xRange + minX;
                        double gy = ly * yRange + minY;
                        double gz = lz * zRange + minZ;

                        manager.addEffect(
                            new ExtendedDiggingParticle(
                                world,
                                pos.getX() + gx,
                                pos.getY() + gy,
                                pos.getZ() + gz,
                                lx - 0.5,
                                ly - 0.5,
                                lz - 0.5,
                                state
                            ).setBlockPos( pos )
                        );
                    }
                }
            }

        } );
    }

    public static void addBlockHitEffects( ParticleManager manager, World world, BlockPos pos, Direction side ) {
        BlockState state = world.getBlockState( pos );
        if( state.getRenderType() != BlockRenderType.INVISIBLE ) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            AxisAlignedBB box = state.getShape( world, pos ).getBoundingBox();

            double px = x + world.rand.nextDouble() * ( box.maxX - box.minX - 0.2 ) + 0.1 + box.minX;
            double py = y + world.rand.nextDouble() * ( box.maxY - box.minY - 0.2 ) + 0.1 + box.minY;
            double pz = z + world.rand.nextDouble() * ( box.maxZ - box.minZ - 0.2 ) + 0.1 + box.minZ;

            if( side == Direction.DOWN ) {
                py = y + box.minY - 0.1;
            }

            if( side == Direction.UP ) {
                py = y + box.maxY + 0.1;
            }

            if( side == Direction.NORTH ) {
                pz = z + box.minZ - 0.1;
            }

            if( side == Direction.SOUTH ) {
                pz = z + box.maxZ + 0.1;
            }

            if( side == Direction.WEST ) {
                px = x + box.minX - 0.1;
            }

            if( side == Direction.EAST ) {
                px = x + box.maxX + 0.1;
            }

            manager.addEffect(
                new ExtendedDiggingParticle( world, px, py, pz, 0, 0, 0, state )
                    .setBlockPos( pos )
                    .multiplyVelocity( 0.2F )
                    .multipleParticleScaleBy( 0.6F )
            );
        }
    }

    @OnlyIn( Dist.CLIENT )
    public static class Factory implements IParticleFactory<BlockParticleData> {
        @Override
        public Particle makeParticle( BlockParticleData type, World world, double x, double y, double z, double xv, double yv, double zv ) {
            BlockState state = type.getBlockState();
            return ! state.isAir() && state.getBlock() != Blocks.MOVING_PISTON
                   ? new ExtendedDiggingParticle( world, x, y, z, xv, yv, zv, state )
                         .init()
                         .updateSprite( type.getPos() )
                   : null;
        }
    }
}