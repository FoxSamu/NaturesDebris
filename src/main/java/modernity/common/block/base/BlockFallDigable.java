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

@SuppressWarnings( "deprecation" )
public class BlockFallDigable extends BlockFall {
    public BlockFallDigable( String id, int dustColor, Properties properties, Item.Properties itemProps ) {
        super( id, dustColor, properties, itemProps );
    }

    public BlockFallDigable( String id, int dustColor, Properties properties ) {
        super( id, dustColor, properties );
    }

    @Override
    public boolean isToolEffective( IBlockState state, ToolType tool ) {
        return tool == ToolType.SHOVEL;
    }
}
