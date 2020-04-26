/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.capability;

import modernity.common.area.core.ServerWorldAreaManager;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WorldAreaCapability {
    private ServerWorld world;
    private ServerWorldAreaManager manager;

    public WorldAreaCapability() {
    }

    public WorldAreaCapability( ServerWorld world ) {
        setWorld( world );
    }

    public WorldAreaCapability setWorld( ServerWorld world ) {
        if( this.world == world ) return this;
        this.world = world;
        this.manager = new ServerWorldAreaManager( world );
        return this;
    }

    public ServerWorldAreaManager getManager() {
        return manager;
    }

    public static class Provider implements ICapabilityProvider {
        private final ServerWorld world;

        public Provider( World world ) {
            this.world = (ServerWorld) world;
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability( @Nonnull Capability<T> cap, @Nullable Direction side ) {
            return MDCapabilities.worldAreas().orEmpty( cap, LazyOptional.of( () -> new WorldAreaCapability( world ) ) );
        }
    }
}
