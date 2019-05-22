package modernity.api.block.fluid;

import net.minecraft.fluid.IFluidState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ICustomRenderFluid {
    @OnlyIn( Dist.CLIENT )
    ResourceLocation getStill();
    @OnlyIn( Dist.CLIENT )
    ResourceLocation getFlowing();
    @OnlyIn( Dist.CLIENT )
    ResourceLocation getOverlay();
    @OnlyIn( Dist.CLIENT )
    int getColor( IFluidState state, BlockPos pos, IBlockReader world );
}
