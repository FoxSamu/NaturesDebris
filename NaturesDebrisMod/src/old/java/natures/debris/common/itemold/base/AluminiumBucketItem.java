/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.itemold.base;

import natures.debris.common.itemold.MDItems;
import natures.debris.generic.block.fluid.IAluminiumBucketTakeable;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;

/**
 * Represents an aluminium bucket.
 */
public class AluminiumBucketItem extends BaseBucketItem {
    public AluminiumBucketItem(Fluid fluid, Properties properties) {
        super(fluid, MDItems.ALUMINIUM_BUCKET, AluminiumBucketItem::createItem, properties);
    }

    private static Item createItem(Fluid fluid) {
        if (fluid instanceof IAluminiumBucketTakeable) {
            return ((IAluminiumBucketTakeable) fluid).getFilledAluminiumBucket();
        }
        return null;
    }
}
