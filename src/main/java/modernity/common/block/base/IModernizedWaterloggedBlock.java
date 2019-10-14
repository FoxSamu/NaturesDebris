package modernity.common.block.base;

import modernity.common.fluid.MDFluids;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

/**
 * Handles waterlogging in Modernized Water only
 */
public interface IModernizedWaterloggedBlock extends IBucketPickupHandler, ILiquidContainer {
    @Override
    default boolean canContainFluid( IBlockReader world, BlockPos pos, BlockState state, Fluid fluid ) {
        return ! state.get( BlockStateProperties.WATERLOGGED ) && fluid == MDFluids.MODERNIZED_WATER;
    }

    @Override
    default boolean receiveFluid( IWorld world, BlockPos pos, BlockState state, IFluidState fstate ) {
        if( ! state.get( BlockStateProperties.WATERLOGGED ) && fstate.getFluid() == MDFluids.MODERNIZED_WATER ) {
            if( ! world.isRemote() ) {
                world.setBlockState( pos, state.with( BlockStateProperties.WATERLOGGED, true ), 3 );
                world.getPendingFluidTicks().scheduleTick( pos, fstate.getFluid(), fstate.getFluid().getTickRate( world ) );
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    default Fluid pickupFluid( IWorld world, BlockPos pos, BlockState state ) {
        if( state.get( BlockStateProperties.WATERLOGGED ) ) {
            world.setBlockState( pos, state.with( BlockStateProperties.WATERLOGGED, false ), 3 );
            return MDFluids.MODERNIZED_WATER;
        } else {
            return Fluids.EMPTY;
        }
    }
}
