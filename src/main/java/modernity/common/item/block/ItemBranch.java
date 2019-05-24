package modernity.common.item.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemBlock;

import modernity.common.block.base.BlockBranch;

public class ItemBranch extends ItemBlock {
    private final BlockBranch branch;

    public ItemBranch( BlockBranch block, Properties builder ) {
        super( block, builder );
        branch = block;
    }

    @Override
    protected boolean placeBlock( BlockItemUseContext ctx, IBlockState state ) {
        boolean sneaking = false;
        if( ctx.getPlayer() != null ) sneaking = ctx.getPlayer().isSneaking();
        return branch.place( ctx.getFace(), ctx.getWorld(), ctx.getPos(), state, ! sneaking );
    }
}
