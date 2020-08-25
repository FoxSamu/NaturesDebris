/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.capability;

import natures.debris.common.ModernityOld;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public final class MDCapabilities {

    public static final ResourceLocation WORLD_AREAS_RES = ModernityOld.res("world_areas");
    private static Capability<WorldAreaCapability> worldAreas;

    private MDCapabilities() {
    }

    @CapabilityInject(WorldAreaCapability.class)
    public static void worldAreas(Capability<WorldAreaCapability> cap) {
        worldAreas = cap;
    }

    public static Capability<WorldAreaCapability> worldAreas() {
        return worldAreas;
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(WorldAreaCapability.class, new NoStorage<>(), WorldAreaCapability::new);
    }

    public static class NoStorage<C> implements Capability.IStorage<C> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<C> capability, C instance, Direction side) {
            return null;
        }

        @Override
        public void readNBT(Capability<C> capability, C instance, Direction side, INBT nbt) {
        }
    }
}
