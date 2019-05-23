package modernity.client.particle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class MDParticleManager extends ParticleManager {
    private final Random rand = new Random();

    public MDParticleManager( World world, TextureManager renderer ) {
        super( world, renderer );

        this.registerFactory( Particles.BLOCK, new ParticleDiggingModernity.Factory() );
    }

    @Override
    public void addBlockDestroyEffects( BlockPos pos, IBlockState state ) {
        if( ! state.isAir( this.world, pos ) ) {
            VoxelShape voxelshape = state.getShape( this.world, pos );
            voxelshape.forEachBox( ( p_199284_3_, p_199284_5_, p_199284_7_, p_199284_9_, p_199284_11_, p_199284_13_ ) -> {
                double d1 = Math.min( 1.0D, p_199284_9_ - p_199284_3_ );
                double d2 = Math.min( 1.0D, p_199284_11_ - p_199284_5_ );
                double d3 = Math.min( 1.0D, p_199284_13_ - p_199284_7_ );
                int i = Math.max( 2, MathHelper.ceil( d1 / 0.25D ) );
                int j = Math.max( 2, MathHelper.ceil( d2 / 0.25D ) );
                int k = Math.max( 2, MathHelper.ceil( d3 / 0.25D ) );

                for( int l = 0; l < i; ++ l ) {
                    for( int i1 = 0; i1 < j; ++ i1 ) {
                        for( int j1 = 0; j1 < k; ++ j1 ) {
                            double d4 = ( (double) l + 0.5D ) / (double) i;
                            double d5 = ( (double) i1 + 0.5D ) / (double) j;
                            double d6 = ( (double) j1 + 0.5D ) / (double) k;
                            double d7 = d4 * d1 + p_199284_3_;
                            double d8 = d5 * d2 + p_199284_5_;
                            double d9 = d6 * d3 + p_199284_7_;
                            this.addEffect( new ParticleDiggingModernity( this.world, (double) pos.getX() + d7, (double) pos.getY() + d8, (double) pos.getZ() + d9, d4 - 0.5D, d5 - 0.5D, d6 - 0.5D, state ).setBlockPos( pos ) );
                        }
                    }
                }

            } );
        }
    }

    public void addBlockHitEffects( BlockPos pos, EnumFacing side ) {
        IBlockState iblockstate = this.world.getBlockState( pos );
        if( iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE ) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            AxisAlignedBB axisalignedbb = iblockstate.getShape( this.world, pos ).getBoundingBox();
            double d0 = (double) i + this.rand.nextDouble() * ( axisalignedbb.maxX - axisalignedbb.minX - (double) 0.2F ) + (double) 0.1F + axisalignedbb.minX;
            double d1 = (double) j + this.rand.nextDouble() * ( axisalignedbb.maxY - axisalignedbb.minY - (double) 0.2F ) + (double) 0.1F + axisalignedbb.minY;
            double d2 = (double) k + this.rand.nextDouble() * ( axisalignedbb.maxZ - axisalignedbb.minZ - (double) 0.2F ) + (double) 0.1F + axisalignedbb.minZ;
            if( side == EnumFacing.DOWN ) {
                d1 = (double) j + axisalignedbb.minY - (double) 0.1F;
            }

            if( side == EnumFacing.UP ) {
                d1 = (double) j + axisalignedbb.maxY + (double) 0.1F;
            }

            if( side == EnumFacing.NORTH ) {
                d2 = (double) k + axisalignedbb.minZ - (double) 0.1F;
            }

            if( side == EnumFacing.SOUTH ) {
                d2 = (double) k + axisalignedbb.maxZ + (double) 0.1F;
            }

            if( side == EnumFacing.WEST ) {
                d0 = (double) i + axisalignedbb.minX - (double) 0.1F;
            }

            if( side == EnumFacing.EAST ) {
                d0 = (double) i + axisalignedbb.maxX + (double) 0.1F;
            }

            this.addEffect( new ParticleDiggingModernity( this.world, d0, d1, d2, 0.0D, 0.0D, 0.0D, iblockstate ).setBlockPos( pos ).multiplyVelocity( 0.2F ).multipleParticleScaleBy( 0.6F ) );
        }
    }

    @OnlyIn( Dist.CLIENT )
    public static class Factory implements IParticleFactory<BlockParticleData> {
        public Particle makeParticle( BlockParticleData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed ) {
            IBlockState iblockstate = typeIn.getBlockState();
            return ! iblockstate.isAir() && iblockstate.getBlock() != Blocks.MOVING_PISTON ? new ParticleDiggingModernity( worldIn, x, y, z, xSpeed, ySpeed, zSpeed, iblockstate ).init() : null;
        }
    }
}
