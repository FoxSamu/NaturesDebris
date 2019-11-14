/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.api.block.fluid;

import net.minecraft.item.Item;

/**
 * Implementing fluids that can be taken with an aluminium bucket.
 *
 * @author RGSW
 */
@FunctionalInterface
public interface IAluminiumBucketTakeable {
    Item getFilledAluminiumBucket();
}
