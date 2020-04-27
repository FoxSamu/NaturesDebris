/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
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
