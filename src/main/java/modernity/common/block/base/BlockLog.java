package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class BlockLog extends BlockBase {
    public static final EnumProperty<EnumFacing.Axis> AXIS = BlockStateProperties.AXIS;

    public BlockLog( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );

        setDefaultState( stateContainer.getBaseState().with( AXIS, EnumFacing.Axis.Y ) );
    }

    public BlockLog( String id, Properties properties ) {
        super( id, properties );

        setDefaultState( stateContainer.getBaseState().with( AXIS, EnumFacing.Axis.Y ) );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, IBlockState> builder ) {
        builder.add( AXIS );
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement( BlockItemUseContext context ) {
        return getDefaultState().with( AXIS, context.getFace().getAxis() );
    }
}
