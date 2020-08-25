/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;

public class ExtSlimeBlock extends SlimeBlock {
    public ExtSlimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isStickyBlock(BlockState state) {
        return true;
    }
}
