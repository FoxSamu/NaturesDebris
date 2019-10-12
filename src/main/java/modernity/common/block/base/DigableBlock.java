package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.ToolType;

public class DigableBlock extends Block {
    public DigableBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public boolean isToolEffective( BlockState state, ToolType tool ) {
        return tool == ToolType.SHOVEL;
    }
}
