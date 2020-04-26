/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.capability;

import modernity.common.ModernityOld;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public final class MDCapabilities {

    private static Capability<WorldAreaCapability> worldAreas;
    public static final ResourceLocation WORLD_AREAS_RES = ModernityOld.res( "world_areas" );

    @CapabilityInject( WorldAreaCapability.class )
    public static void worldAreas( Capability<WorldAreaCapability> cap ) {
        worldAreas = cap;
    }

    public static Capability<WorldAreaCapability> worldAreas() {
        return worldAreas;
    }




    private MDCapabilities() {
    }

    public static void register() {
        CapabilityManager.INSTANCE.register( WorldAreaCapability.class, new NoStorage<>(), WorldAreaCapability::new );
    }

    public static class NoStorage<C> implements Capability.IStorage<C> {
        @Nullable
        @Override
        public INBT writeNBT( Capability<C> capability, C instance, Direction side ) {
            return null;
        }

        @Override
        public void readNBT( Capability<C> capability, C instance, Direction side, INBT nbt ) {
        }
    }
}
