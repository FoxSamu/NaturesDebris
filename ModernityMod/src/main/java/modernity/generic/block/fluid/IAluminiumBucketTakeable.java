/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.block.fluid;

import net.minecraft.item.Item;

/**
 * Implementing fluids that can be taken with an aluminium bucket.
 *
 * @author RGSW
 */
@FunctionalInterface
public interface IAluminiumBucketTakeable extends IModernityBucketTakeable {
    Item getFilledAluminiumBucket();
}
