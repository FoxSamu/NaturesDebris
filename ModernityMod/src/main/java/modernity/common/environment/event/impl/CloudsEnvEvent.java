/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.environment.event.impl;

import modernity.common.environment.event.EffectingEnvEvent;
import modernity.common.environment.event.EnvironmentEventManager;
import modernity.common.environment.event.MDEnvEvents;
import modernity.generic.util.Ticks;
import net.minecraft.nbt.CompoundNBT;

public class CloudsEnvEvent extends EffectingEnvEvent {
    private float cloudAmount;

    public CloudsEnvEvent( EnvironmentEventManager manager ) {
        super( MDEnvEvents.CLOUDS, manager );
    }

    public float getCloudAmount() {
        return cloudAmount;
    }

    @Override
    protected int computeMaxTimeForPhase( Phase phase ) {
        switch( phase ) {
            case INACTIVE: return rand.nextInt( 20 * Ticks.MINUTES ) + 40 * Ticks.MINUTES;
            case WAITING: return getManager().getByType( MDEnvEvents.CLOUDLESS ).isActive() ? 0 : - 1;
            case ACTIVE: return rand.nextInt( 6 * Ticks.MINUTES ) + 10 * Ticks.MINUTES;
            case COOLDOWN: return 0;
        }
        return 0;
    }

    @Override
    protected boolean canGoActive() {
        return rand.nextInt( 1000 ) == 0;
    }

    @Override
    protected void onStart() {
        getManager().getByType( MDEnvEvents.CLOUDLESS ).setActive( false );
        cloudAmount = rand.nextFloat() * 0.2F + 0.1F;
    }

    @Override
    public void write( CompoundNBT nbt ) {
        super.write( nbt );
        nbt.putFloat( "cloudAmount", cloudAmount );
    }

    @Override
    public void read( CompoundNBT nbt ) {
        super.read( nbt );
        cloudAmount = nbt.getFloat( "cloudAmount" );
    }
}
