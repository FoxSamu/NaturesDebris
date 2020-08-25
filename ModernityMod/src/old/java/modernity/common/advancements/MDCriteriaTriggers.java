/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.advancements;

import net.minecraft.advancements.criterion.PositionTrigger;
import net.minecraft.util.ResourceLocation;

import static net.minecraft.advancements.CriteriaTriggers.*;

public final class MDCriteriaTriggers {
    public static final PositionTrigger IN_CAVE = register(new PositionTrigger(new ResourceLocation("modernity:in_cave")));
    public static final BreakBlockTrigger BREAK_BLOCK = register(new BreakBlockTrigger());

    private MDCriteriaTriggers() {
    }

    // Loads class if not loaded
    public static void init() {
    }
}
