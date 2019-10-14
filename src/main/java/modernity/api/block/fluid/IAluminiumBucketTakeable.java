/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
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
