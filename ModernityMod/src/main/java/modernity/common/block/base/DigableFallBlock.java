/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.block.base;

import net.minecraft.block.BlockState;
import net.minecraftforge.common.ToolType;

/**
 * Describes a falling block that can be efficiently dug by a shovel
 */
public class DigableFallBlock extends FallBlock {

    public DigableFallBlock( int dustColor, Properties properties ) {
        super( dustColor, properties );
    }

    @Override
    public boolean isToolEffective( BlockState state, ToolType tool ) {
        return tool == ToolType.SHOVEL;
    }
}
