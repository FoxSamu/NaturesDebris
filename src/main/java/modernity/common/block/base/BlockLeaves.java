package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.Tag;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IShearable;

import modernity.Modernity;
import modernity.api.block.IColoredBlock;
import modernity.api.util.ColorUtil;
import modernity.api.util.EcoBlockPos;
import modernity.client.util.MDBiomeValues;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockLeaves extends BlockBase implements IShearable {

    public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE_1_7;
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

    private final Tag<Block> logTag;

    public BlockLeaves( String id, Tag<Block> logTag, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
        this.logTag = logTag;
        setDefaultState( stateContainer.getBaseState().with( DISTANCE, 7 ).with( PERSISTENT, false ) );
    }

    public BlockLeaves( String id, Tag<Block> logTag, Properties properties ) {
        super( id, properties );
        this.logTag = logTag;
        setDefaultState( stateContainer.getBaseState().with( DISTANCE, 7 ).with( PERSISTENT, false ) );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean ticksRandomly( IBlockState state ) {
        return state.get( DISTANCE ) == 7 && ! state.get( PERSISTENT );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void randomTick( IBlockState state, World world, BlockPos pos, Random random ) {
        if( ! state.get( PERSISTENT ) && state.get( DISTANCE ) == 7 ) {
            state.dropBlockAsItem( world, pos, 0 );
            world.removeBlock( pos );
        }
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void tick( IBlockState state, World world, BlockPos pos, Random random ) {
        world.setBlockState( pos, updateDistance( state, world, pos ), 2 | 4 );
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public int getOpacity( IBlockState state, IBlockReader world, BlockPos pos ) {
        return 1;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public IBlockState updatePostPlacement( IBlockState state, EnumFacing facing, IBlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos ) {
        int dist = getDistance( facingState ) + 1;
        if( dist != 1 || state.get( DISTANCE ) != dist ) {
            world.getPendingBlockTicks().scheduleTick( currentPos, this, 1 );
        }

        return state;
    }

    private IBlockState updateDistance( IBlockState state, IWorld world, BlockPos pos ) {
        int dist = 7;

        try( EcoBlockPos rpos = EcoBlockPos.retain() ) {
            for( EnumFacing facing : EnumFacing.values() ) {
                rpos.setPos( pos ).move( facing );
                dist = Math.min( dist, getDistance( world.getBlockState( rpos ) ) + 1 );
                if( dist == 1 ) {
                    break;
                }
            }
        }

        return state.with( DISTANCE, dist );
    }

    private int getDistance( IBlockState neighbor ) {
        if( logTag.contains( neighbor.getBlock() ) ) {
            return 0;
        } else {
            return neighbor.getBlock() instanceof BlockLeaves ? neighbor.get( DISTANCE ) : 7;
        }
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void animateTick( IBlockState state, World world, BlockPos pos, Random rand ) {
        if( world.isRainingAt( pos.up() ) && ! world.getBlockState( pos.down() ).isTopSolid() && rand.nextInt( 15 ) == 1 ) {
            double x = pos.getX() + rand.nextFloat();
            double y = pos.getY() - 0.05D;
            double z = pos.getZ() + rand.nextFloat();
            world.addParticle( Particles.DRIPPING_WATER, x, y, z, 0, 0, 0 );
        }

    }

    @Override
    @SuppressWarnings( "deprecation" )
    public int quantityDropped( IBlockState state, Random random ) {
        return random.nextInt( 20 ) == 0 ? 1 : 0;
    }

    @Override
    public IItemProvider getItemDropped( IBlockState state, World world, BlockPos pos, int fortune ) {
        Block block = state.getBlock();
        if( block == Blocks.OAK_LEAVES ) {
            return Blocks.OAK_SAPLING;
        } else if( block == Blocks.SPRUCE_LEAVES ) {
            return Blocks.SPRUCE_SAPLING;
        } else if( block == Blocks.BIRCH_LEAVES ) {
            return Blocks.BIRCH_SAPLING;
        } else if( block == Blocks.JUNGLE_LEAVES ) {
            return Blocks.JUNGLE_SAPLING;
        } else if( block == Blocks.ACACIA_LEAVES ) {
            return Blocks.ACACIA_SAPLING;
        } else {
            return block == Blocks.DARK_OAK_LEAVES ? Blocks.DARK_OAK_SAPLING : Blocks.OAK_SAPLING;
        }
    }

    @Override
    public void getDrops( IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune ) {
        int saplingDropChance = getSaplingDropChance( state );
        if( fortune > 0 ) {
            saplingDropChance -= 2 << fortune;
            if( saplingDropChance < 10 ) {
                saplingDropChance = 10;
            }
        }

        if( world.rand.nextInt( saplingDropChance ) == 0 ) {
            ItemStack drop = new ItemStack( getItemDropped( state, world, pos, fortune ) );
            if( ! drop.isEmpty() )
                drops.add( drop );
        }

        saplingDropChance = 200;
        if( fortune > 0 ) {
            saplingDropChance -= 10 << fortune;
            if( saplingDropChance < 40 ) {
                saplingDropChance = 40;
            }
        }

        captureDrops( true );
        this.dropApple( world, pos, state, saplingDropChance );
        drops.addAll( captureDrops( false ) );
    }

    protected void dropApple( World world, BlockPos pos, IBlockState state, int chance ) {
//        if( ( state.getBlock() == Blocks.OAK_LEAVES || state.getBlock() == Blocks.DARK_OAK_LEAVES ) && world.rand.nextInt( chance ) == 0 ) {
//            spawnAsEntity( world, pos, new ItemStack( Items.APPLE ) );
//        }
    }

    protected int getSaplingDropChance( IBlockState state ) {
        return 20;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return Modernity.proxy.fancyGraphics() ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean causesSuffocation( IBlockState state ) {
        return false;
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        builder.add( DISTANCE, PERSISTENT );
    }

    @Override
    public IBlockState getStateForPlacement( BlockItemUseContext context ) {
        return updateDistance( getDefaultState().with( PERSISTENT, true ), context.getWorld(), context.getPos() );
    }

    @Override
    public List<ItemStack> onSheared( @Nonnull ItemStack item, IWorld world, BlockPos pos, int fortune ) {
        world.setBlockState( pos, Blocks.AIR.getDefaultState(), 11 );
        return Collections.singletonList( new ItemStack( this ) );
    }

    public static class ColoredFoliage extends BlockLeaves implements IColoredBlock {

        private static final int DEFAULT_COLOR = ColorUtil.rgb( 32, 86, 49 );

        public ColoredFoliage( String id, Tag<Block> logTag, Properties properties, Item.Properties itemProps ) {
            super( id, logTag, properties, itemProps );
        }

        public ColoredFoliage( String id, Tag<Block> logTag, Properties properties ) {
            super( id, logTag, properties );
        }

        @Override
        public int colorMultiplier( IBlockState state, @Nullable IWorldReaderBase reader, @Nullable BlockPos pos, int tintIndex ) {
            return MDBiomeValues.get( reader, pos, MDBiomeValues.FOLIAGE_COLOR );
        }

        @Override
        public int colorMultiplier( ItemStack stack, int tintIndex ) {
            return DEFAULT_COLOR;
        }
    }
}
