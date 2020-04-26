/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 22 - 2020
 * Author: rgsw
 */

package modernity.common.block.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;

public class ExtSlimeBlock extends SlimeBlock {
    public ExtSlimeBlock( Properties properties ) {
        super( properties );
    }

    @Override
    public boolean isStickyBlock( BlockState state ) {
        return true;
    }
}
