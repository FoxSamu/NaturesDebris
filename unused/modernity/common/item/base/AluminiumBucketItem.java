/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.item.base;

import modernity.generic.block.fluid.IAluminiumBucketTakeable;
import modernity.common.item.MDItems;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;

/**
 * Represents an aluminium bucket.
 */
public class AluminiumBucketItem extends BaseBucketItem {
    public AluminiumBucketItem( Fluid fluid, Properties properties ) {
        super( fluid, MDItems.ALUMINIUM_BUCKET, AluminiumBucketItem::createItem, properties );
    }

    private static Item createItem( Fluid fluid ) {
        if( fluid instanceof IAluminiumBucketTakeable ) {
            return ( (IAluminiumBucketTakeable) fluid ).getFilledAluminiumBucket();
        }
        return null;
    }
}
