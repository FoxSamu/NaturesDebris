/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 2 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;

public class BlockDigable extends BlockBase {
    public BlockDigable( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
    }

    public BlockDigable( String id, Properties properties ) {
        super( id, properties );
    }

    @Override
    public boolean isToolEffective( IBlockState state, ToolType tool ) {
        return tool == ToolType.SHOVEL;
    }
}
