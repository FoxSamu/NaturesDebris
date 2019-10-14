package modernity.common.block.base;

import modernity.api.block.IColoredBlock;
import modernity.api.reflect.FieldAccessor;
import modernity.api.util.ColorUtil;
import modernity.client.ModernityClient;
import modernity.common.block.MDBlocks;
import modernity.common.particle.MDParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Describes any variant of dirt blocks
 */
public class DirtBlock extends DigableBlock {
    // Used to get nextStepDistance on entities to calculate when humus particles are pushed...
    private static final FieldAccessor<Entity, Float> nextStepDistanceField = new FieldAccessor<>( Entity.class, "field_70150_b" );

    public static final Type TYPE_DIRT = new Type( false, false, BlockRenderLayer.SOLID );
    public static final Type TYPE_GRASS = new Type( true, true, BlockRenderLayer.CUTOUT_MIPPED );
    public static final Type TYPE_HUMUS = new Type( false, true, false, BlockRenderLayer.SOLID );
    public static final Type TYPE_PODZOL = new Type( false, true, false, BlockRenderLayer.SOLID );

    // TODO: only use this property on grass blocks, create a custom subclass
    public static final BooleanProperty SNOWY = BooleanProperty.create( "snowy" );

    public final Type type;

    public DirtBlock( Type type, Properties properties ) {
        super( properties.tickRandomly() );
        this.type = type;

        setDefaultState( getStateContainer().getBaseState().with( SNOWY, false ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( SNOWY );
    }

    /**
     * Checks if this block can remain at a specific position (whether it has enough light to stay)
     */
    private static boolean canRemainAt( IEnviromentBlockReader world, BlockPos pos ) {
        BlockPos up = pos.up();
        return world.getLightValue( up ) >= 4 || world.getBlockState( up ).getOpacity( world, up ) < world.getMaxLightLevel();
    }

    /**
     * Checks if this block can spread to a specific position (whether it has enough light to stay)
     */
    private static boolean canGrowAt( IEnviromentBlockReader world, BlockPos pos ) {
        BlockPos up = pos.up();
        return world.getLightValue( up ) >= 4 && world.getBlockState( up ).getOpacity( world, up ) < world.getMaxLightLevel() && ! world.getFluidState( up ).isTagged( FluidTags.WATER );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        if( ! type.canBeSnowy ) return state;
        if( facing != Direction.UP ) {
            return super.updatePostPlacement( state, facing, facingState, world, currentPos, facingPos );
        } else {
            Block block = facingState.getBlock();
            return state.with( SNOWY, block == Blocks.SNOW_BLOCK || block == Blocks.SNOW );
        }
    }

    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        if( ! type.canBeSnowy ) return this.getDefaultState();
        Block block = context.getWorld().getBlockState( context.getPos().up() ).getBlock();
        return this.getDefaultState().with( SNOWY, block == Blocks.SNOW_BLOCK || block == Blocks.SNOW );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void tick( BlockState state, World world, BlockPos pos, Random random ) {
        if( ! world.isRemote ) {
            if( ! world.isAreaLoaded( pos, 3 ) )
                return;
            if( ! canRemainAt( world, pos ) && type.canDecay ) {
                // Do decaying
                world.setBlockState( pos, MDBlocks.DARK_DIRT.getDefaultState() );
            } else if( type.canSpread ) {
                // Do spreading
                if( world.getLight( pos.up() ) >= 9 ) {
                    for( int i = 0; i < 4; ++ i ) {
                        BlockPos growPos = pos.add( random.nextInt( 3 ) - 1, random.nextInt( 5 ) - 3, random.nextInt( 3 ) - 1 );
                        if( ! world.isBlockPresent( growPos ) ) {
                            return;
                        }

                        if( world.getBlockState( growPos ).getBlock() == MDBlocks.DARK_DIRT && canGrowAt( world, growPos ) ) {
                            world.setBlockState( growPos, this.getDefaultState() );
                        }
                    }
                }

            }
        }
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void onFallenUpon( World world, BlockPos pos, Entity entity, float fallDistance ) {
        super.onFallenUpon( world, pos, entity, fallDistance );
        if( type != TYPE_HUMUS ) return;

        // Humus falling particles
        if( fallDistance > 0.1 ) {
            int amount = Math.min( (int) ( fallDistance * 10 ), 30 );
            if( amount > 0 ) {
                Random rand = world.rand;
                for( int i = 0; i < amount; i++ ) {
                    double mx = entity.getMotion().x * 0.2 + ( rand.nextDouble() * 0.3 - 0.15 );
                    double mz = entity.getMotion().z * 0.2 + ( rand.nextDouble() * 0.3 - 0.15 );
                    double my = rand.nextDouble() * 0.2;
                    double x = rand.nextDouble() * 0.6 - 0.3 + entity.posX;
                    double y = pos.getY() + 1.05;
                    double z = rand.nextDouble() * 0.6 - 0.3 + entity.posZ;

                    world.addParticle( MDParticleTypes.HUMUS, x, y, z, mx, my, mz );
                }
            }
        }
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void onEntityWalk( World world, BlockPos pos, Entity entity ) {
        if( type != TYPE_HUMUS ) return;

        // Humus walking particles
        if( ! entity.isInWater() ) {
            // Get entity.nextStepDistance via reflection
            double nsd = nextStepDistanceField.get( entity );
            double d = nsd - entity.distanceWalkedOnStepModified;
            double s = entity.getMotion().lengthSquared();

            if( d < 0.15 && s > 0.01 * 0.01 ) {
                Random rand = world.rand;
                for( int i = 0; i < 12; i++ ) {
                    double x = rand.nextDouble() * 0.6 - 0.3 + entity.posX;
                    double y = pos.getY() + 1.05;
                    double z = rand.nextDouble() * 0.6 - 0.3 + entity.posZ;
                    double mx = entity.getMotion().x * 1.05 + ( rand.nextDouble() * 0.15 - 0.075 );
                    double mz = entity.getMotion().z * 1.05 + ( rand.nextDouble() * 0.15 - 0.075 );
                    double my = rand.nextDouble() * 0.1;


                    world.addParticle( MDParticleTypes.HUMUS, x, y, z, mx, my, mz );
                }
            }
        }
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return type.renderLayer;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean isSolid( BlockState state ) {
        return true; // Make sure we're solid to enable face culling on grass (which has no solid block layer)
    }

    /**
     * A type of dirt
     */
    public static final class Type {
        public final boolean canSpread;
        public final boolean canDecay;
        public final boolean canBeSnowy;
        public final BlockRenderLayer renderLayer;

        public Type( boolean canSpread, boolean canBeSnowy, BlockRenderLayer renderLayer ) {
            this( canSpread, canSpread, canBeSnowy, renderLayer );
        }

        public Type( boolean canSpread, boolean canDecay, boolean canBeSnowy, BlockRenderLayer renderLayer ) {
            this.canSpread = canSpread;
            this.canDecay = canDecay;
            this.canBeSnowy = canBeSnowy;
            this.renderLayer = renderLayer;
        }
    }

    /**
     * The grass-colored block
     */
    // TODO: Move snowy property to this class
    public static class ColoredGrass extends DirtBlock implements IColoredBlock {
        protected static final int GRASS_ITEM_COLOR = ColorUtil.rgb( 0, 109, 38 );

        public ColoredGrass( Type type, Properties properties ) {
            super( type, properties );
        }

        @OnlyIn( Dist.CLIENT )
        @Override
        public int colorMultiplier( BlockState state, @Nullable IEnviromentBlockReader reader, @Nullable BlockPos pos, int tintIndex ) {
            return ModernityClient.get().getGrassColors().getColor( reader, pos );
        }

        @OnlyIn( Dist.CLIENT )
        @Override
        public int colorMultiplier( ItemStack stack, int tintIndex ) {
            return ModernityClient.get().getGrassColors().getItemColor();
        }

        @Override
        public boolean addDestroyEffects( BlockState state, World world, BlockPos pos, ParticleManager manager ) {
            return false;
        }
    }
}
