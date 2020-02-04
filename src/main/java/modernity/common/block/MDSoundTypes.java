/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 04 - 2020
 * Author: rgsw
 */

package modernity.common.block;

import modernity.common.sound.MDSoundEvents;
import net.minecraft.block.SoundType;
import net.minecraft.util.SoundEvents;

public final class MDSoundTypes {
    public static final SoundType SHADE_BLUE = new SoundType(
        1, 1,
        MDSoundEvents.BLOCK_SHADE_BLUE_BREAK,
        SoundEvents.BLOCK_GRASS_STEP,
        MDSoundEvents.BLOCK_SHADE_BLUE_PLACE,
        SoundEvents.BLOCK_GRASS_HIT,
        SoundEvents.BLOCK_GRASS_FALL
    );

    private MDSoundTypes() {
    }
}
