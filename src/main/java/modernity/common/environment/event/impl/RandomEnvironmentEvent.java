package modernity.common.environment.event.impl;

import modernity.common.environment.event.EnvironmentEvent;
import modernity.common.environment.event.EnvironmentEventManager;
import modernity.common.environment.event.MDEnvEvents;
import net.minecraft.nbt.CompoundNBT;

public class RandomEnvironmentEvent extends EnvironmentEvent {
    private int updateInterval;

    public RandomEnvironmentEvent( EnvironmentEventManager manager ) {
        super( MDEnvEvents.RANDOM, manager );
    }

    @Override
    public void tick() {
        updateInterval ++;
        while( updateInterval > 200 ) {
            setActive( ! isActive() );
            updateInterval -= 200;
        }
    }

    @Override
    public void write( CompoundNBT nbt ) {
        nbt.putInt( "ui", updateInterval );
    }

    @Override
    public void read( CompoundNBT nbt ) {
        updateInterval = nbt.getInt( "ui" );
    }
}
