/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.base;

import net.minecraft.block.BlockState;
import net.minecraftforge.common.ToolType;

/**
 * Describes a falling block that can be efficiently dug by a shovel
 */
public class DigableFallBlock extends FallBlock {

    public DigableFallBlock(int dustColor, Properties properties) {
        super(dustColor, properties);
    }

    @Override
    public boolean isToolEffective(BlockState state, ToolType tool) {
        return tool == ToolType.SHOVEL;
    }
}
