/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.sound;

import natures.debris.common.registryold.RegistryEventHandler;
import natures.debris.common.registryold.RegistryHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public final class MDSoundEvents {
    private static final RegistryHandler<SoundEvent> ENTRIES = new RegistryHandler<>("natures/debris", true);

    public static final SoundEvent BLOCK_SHADE_BLUE_BREAK = register("block.shade_blue.break");
    public static final SoundEvent BLOCK_SHADE_BLUE_PLACE = register("block.shade_blue.place");
    public static final SoundEvent BLOCK_SHADE_BLUE_TELEPORT = register("block.shade_blue.teleport");

    public static final SoundEvent BLOCK_MUD_BREAK = register("block.mud.break");
    public static final SoundEvent BLOCK_MUD_PLACE = register("block.mud.place");
    public static final SoundEvent BLOCK_MUD_STEP = register("block.mud.step");
    public static final SoundEvent BLOCK_MUD_HIT = register("block.mud.hit");
    public static final SoundEvent BLOCK_MUD_FALL = register("block.mud.fall");

    public static final SoundEvent BLOCK_ASPHALT_BREAK = register("block.asphalt.break");
    public static final SoundEvent BLOCK_ASPHALT_PLACE = register("block.asphalt.place");
    public static final SoundEvent BLOCK_ASPHALT_STEP = register("block.asphalt.step");
    public static final SoundEvent BLOCK_ASPHALT_HIT = register("block.asphalt.hit");
    public static final SoundEvent BLOCK_ASPHALT_FALL = register("block.asphalt.fall");

    public static final SoundEvent BLOCK_CRYSTAL_BREAK = register("block.crystal.break");
    public static final SoundEvent BLOCK_CRYSTAL_PLACE = register("block.crystal.place");
    public static final SoundEvent BLOCK_CRYSTAL_STEP = register("block.crystal.step");
    public static final SoundEvent BLOCK_CRYSTAL_HIT = register("block.crystal.hit");
    public static final SoundEvent BLOCK_CRYSTAL_FALL = register("block.crystal.fall");

    private MDSoundEvents() {
    }

    private static SoundEvent register(String name) {
        SoundEvent event = new SoundEvent(new ResourceLocation("natures/debris", name));
        return ENTRIES.register(name, event);
    }

    public static void setup(RegistryEventHandler handler) {
        handler.addHandler(SoundEvent.class, ENTRIES);
    }
}
