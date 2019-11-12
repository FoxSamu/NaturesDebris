package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;

import javax.annotation.Nullable;

public class HorizontalPortalFrameBlock extends AbstractPortalFrameBlock {
    public static final EnumProperty<Direction.Axis> DIRECTION = EnumProperty.create( "axis", Direction.Axis.class, Direction.Axis::isHorizontal );

    public HorizontalPortalFrameBlock( Properties props ) {
        super( props );
        setDefaultState( getDefaultState().with( DIRECTION, Direction.Axis.X ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( DIRECTION );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement( BlockItemUseContext context ) {
        return getDefaultState().with( DIRECTION, context.getPlacementHorizontalFacing().getAxis() );
    }

    @Override
    public BlockState rotate( BlockState state, Rotation direction ) {
        Direction.Axis axis = state.get( DIRECTION );
        if( direction != Rotation.CLOCKWISE_180 ) {
            axis = axis == Direction.Axis.Z ? Direction.Axis.X : Direction.Axis.Z;
        }
        return state.with( DIRECTION, axis );
    }
}
