package modernity.common.block.base;

import modernity.common.event.MDBlockEvents;
import modernity.common.item.MDItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;

public class PortalCornerBlock extends Block {
    private static final VoxelShape SLAB_SHAPE = makeCuboidShape( 0, 0, 0, 16, 8, 16 );
    private static final VoxelShape EYE_SHAPE = makeCuboidShape( 5, 8, 5, 11, 10, 11 );

    private static final VoxelShape COMBINED_SHAPE = VoxelShapes.or( SLAB_SHAPE, EYE_SHAPE );


    public static final EnumProperty<State> STATE = EnumProperty.create( "state", State.class );

    public PortalCornerBlock( Properties props ) {
        super( props );
        setDefaultState( getDefaultState().with( STATE, State.INACTIVE ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( STATE );
    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context ) {
        return state.get( STATE ).getShape();
    }

    @Override
    public int getLightValue( BlockState state, IEnviromentBlockReader world, BlockPos pos ) {
        return state.get( STATE ).getLightValue();
    }

    @Override
    public boolean onBlockActivated( BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit ) {
        if( state.get( STATE ) == State.INACTIVE ) {
            ItemStack held = player.getHeldItem( hand );
            if( held.getItem() == MDItems.EYE_OF_THE_CURSE ) {
                if( ! player.abilities.isCreativeMode ) {
                    held.shrink( 1 );
                }
                world.setBlockState( pos, state.with( STATE, State.EYE ) );
                MDBlockEvents.PLACE_EYE.play( world, pos );
                return true;
            }
        }
        if( state.get( STATE ) == State.EXHAUSTED ) {
            world.setBlockState( pos, state.with( STATE, State.INACTIVE ) );
            MDBlockEvents.BREAK_EYE.play( world, pos );
            return true;
        }
        return false;
    }

    @Override
    public boolean isSolid( BlockState state ) {
        return false;
    }

    public enum State implements IStringSerializable {
        INACTIVE( "inactive", SLAB_SHAPE, 0 ),
        EYE( "eye", COMBINED_SHAPE, 0 ),
        ACTIVE( "active", COMBINED_SHAPE, 8 ),
        EXHAUSTED( "exhausted", COMBINED_SHAPE, 0 );

        private final String name;
        private final VoxelShape shape;
        private final int lightValue;

        State( String name, VoxelShape shape, int lightValue ) {
            this.name = name;
            this.shape = shape;
            this.lightValue = lightValue;
        }

        @Override
        public String getName() {
            return name;
        }

        public VoxelShape getShape() {
            return shape;
        }

        public int getLightValue() {
            return lightValue;
        }
    }
}
