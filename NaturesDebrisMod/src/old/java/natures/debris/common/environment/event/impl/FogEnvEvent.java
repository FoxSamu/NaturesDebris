/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.environment.event.impl;

import natures.debris.common.environment.event.EffectingEnvEvent;
import natures.debris.common.environment.event.EnvironmentEventManager;
import natures.debris.common.environment.event.MDEnvEvents;
import natures.debris.generic.util.Ticks;
import net.minecraft.nbt.CompoundNBT;

public class FogEnvEvent extends EffectingEnvEvent {
    private float density;

    public FogEnvEvent(EnvironmentEventManager manager) {
        super(MDEnvEvents.FOG, manager);
    }

    public float getDensity() {
        return density;
    }

    @Override
    protected int computeMaxTimeForPhase(Phase phase) {
        switch (phase) {
            case INACTIVE: return rand.nextInt(20 * Ticks.MINUTES) + 40 * Ticks.MINUTES;
            case WAITING: return -1;
            case ACTIVE: return rand.nextInt(6 * Ticks.MINUTES) + 10 * Ticks.MINUTES;
            case COOLDOWN: return 0;
        }
        return 0;
    }

    @Override
    protected boolean canGoActive() {
        return rand.nextInt(1000) == 0;
    }

    @Override
    protected void onStart() {
        density = rand.nextFloat() * 0.03F + 0.005F;
        if (rand.nextInt(6) == 0) {
            density += rand.nextFloat() * 0.02F;
        }
    }

    @Override
    public void write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.putFloat("density", density);
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        density = nbt.getFloat("density");
    }
}
