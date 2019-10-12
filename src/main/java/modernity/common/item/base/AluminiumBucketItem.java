/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.item.base;

import modernity.api.block.fluid.IAluminiumBucketTakeable;
import modernity.common.item.MDItems;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;

public class AluminiumBucketItem extends BaseBucketItem {
    public AluminiumBucketItem( Fluid fluid, Item.Properties properties ) {
        super( fluid, MDItems.ALUMINIUM_BUCKET, AluminiumBucketItem::createItem, properties );
    }

    private static Item createItem( Fluid fluid ) {
        if( fluid instanceof IAluminiumBucketTakeable ) {
            return ( (IAluminiumBucketTakeable) fluid ).getFilledAluminiumBucket();
        }
        return null;
    }
}
