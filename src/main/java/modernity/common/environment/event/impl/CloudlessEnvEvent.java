/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 18 - 2019
 * Author: rgsw
 */

package modernity.common.environment.event.impl;

import modernity.api.util.Ticks;
import modernity.common.environment.event.EnvironmentEventManager;
import modernity.common.environment.event.MDEnvEvents;
import modernity.common.environment.event.ScheduledEnvEvent;
import net.minecraft.nbt.CompoundNBT;

public class CloudlessEnvEvent extends ScheduledEnvEvent {
    private float cloudAmount;
    private float effect;

    public CloudlessEnvEvent( EnvironmentEventManager manager ) {
        super( MDEnvEvents.CLOUDLESS, manager );
    }

    public float getCloudAmount() {
        return cloudAmount;
    }

    public float getEffect() {
        return effect;
    }

    @Override
    protected int computeMaxTimeForPhase( Phase phase ) {
        switch( phase ) {
            case INACTIVE: return rand.nextInt( 20 * Ticks.MINUTES ) + 40 * Ticks.MINUTES;
            case WAITING: return getManager().getByType( MDEnvEvents.CLOUDS ).isActive() ? 0 : - 1;
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
        getManager().getByType( MDEnvEvents.CLOUDS ).setActive( false );
        cloudAmount = rand.nextFloat() * 0.01F;
    }

    @Override
    public void write( CompoundNBT nbt ) {
        super.write( nbt );
        nbt.putFloat( "cloudAmount", cloudAmount );
        nbt.putFloat( "effect", effect );
    }

    @Override
    public void read( CompoundNBT nbt ) {
        super.read( nbt );
        cloudAmount = nbt.getFloat( "cloudAmount" );
        effect = nbt.getFloat( "effect" );
    }

    @Override
    public void tick() {
        super.tick();
        if( isActive() ) {
            effect += 0.002;
            if( effect > 1 ) {
                effect = 1;
            }
        } else {
            effect -= 0.002;
            if( effect < 0 ) {
                effect = 0;
            }
        }
    }
}
