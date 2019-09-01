/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.item.base;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;

import modernity.api.block.fluid.IAluminiumBucketTakeable;
import modernity.common.item.MDItems;

public class ItemAluminiumBucket extends ItemBucketBase {
    public ItemAluminiumBucket( String id, Fluid fluid, Properties properties ) {
        super( id, fluid, MDItems.ALUMINIUM_BUCKET, ItemAluminiumBucket::createItem, properties );
    }

    private static Item createItem( Fluid fluid ) {
        if( fluid instanceof IAluminiumBucketTakeable ) {
            return ( (IAluminiumBucketTakeable) fluid ).getFilledAluminiumBucket();
        }
        return null;
    }
}
