/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 17 - 2019
 */

package modernity.common.block.base;

import net.minecraft.item.Item;

public class BlockOre extends BlockBase {

    public BlockOre( String id, Properties properties, Item.Properties itemProps ) {
        super( id, properties, itemProps );
    }

    public BlockOre( String id, Properties properties ) {
        super( id, properties );
    }

    public static class Salt extends BlockOre {
        public Salt( String id, Properties properties, Item.Properties itemProps ) {
            super( id, properties, itemProps );
        }

        public Salt( String id, Properties properties ) {
            super( id, properties );
        }
    }
}
