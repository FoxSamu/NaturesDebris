/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 24 - 2020
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

    public static final SoundType MUD = new SoundType(
        1.2F, 1,
        MDSoundEvents.BLOCK_MUD_BREAK,
        MDSoundEvents.BLOCK_MUD_STEP,
        MDSoundEvents.BLOCK_MUD_PLACE,
        MDSoundEvents.BLOCK_MUD_HIT,
        MDSoundEvents.BLOCK_MUD_FALL
    );

    private MDSoundTypes() {
    }
}
