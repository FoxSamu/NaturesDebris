/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 2 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.ToolType;

public class DigableFallBlock extends FallBlock {

    public DigableFallBlock( int dustColor, Block.Properties properties ) {
        super( dustColor, properties );
    }

    @Override
    public boolean isToolEffective( BlockState state, ToolType tool ) {
        return tool == ToolType.SHOVEL;
    }
}
