package modernity.common.block.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStripableBranch extends BlockBranch {
    private final BlockBranch result;

    public BlockStripableBranch( String id, double thickness, BlockBranch result, Properties properties, Item.Properties itemProps ) {
        super( id, thickness, properties, itemProps );
        this.result = result;
    }

    public BlockStripableBranch( String id, double thickness, BlockBranch result, Properties properties ) {
        super( id, thickness, properties );
        this.result = result;
    }

    @Override
    public boolean onBlockActivated( IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ ) {
        if( player.getHeldItem( hand ).getItem() instanceof ItemAxe ) {
            world.setBlockState( pos, result.copy( state ) );
            world.playSound( pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1, 1, false );
            return true;
        }
        return false;
    }
}
