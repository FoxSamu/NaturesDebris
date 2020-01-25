/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.base;

import net.minecraft.block.BlockState;
import net.minecraftforge.common.ToolType;

/**
 * A digable variant of {@link StickyBlock}.
 */
public class DigableStickyBlock extends StickyBlock {

    public DigableStickyBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public boolean isToolEffective( BlockState state, ToolType tool ) {
        return tool == ToolType.SHOVEL;
    }
}
