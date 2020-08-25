/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.itemold;

import net.minecraft.item.Food;

public final class MDFoods {
    public static final Food MURK_ROOTS = new Food.Builder().hunger(3).saturation(0.6F).build();
    public static final Food MURK_RICE = new Food.Builder().hunger(1).saturation(0.3F).fastToEat().build();

    private MDFoods() {
    }
}
