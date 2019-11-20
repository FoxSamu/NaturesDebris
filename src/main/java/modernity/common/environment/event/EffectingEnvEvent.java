/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.environment.event;

import net.minecraft.nbt.CompoundNBT;

public abstract class EffectingEnvEvent extends ScheduledEnvEvent {
    private float effect;

    public EffectingEnvEvent( EnvironmentEventType type, EnvironmentEventManager manager ) {
        super( type, manager );
    }

    public float getEffect() {
        return effect;
    }

    @Override
    public void setEnabled( boolean enabled ) {
        super.setEnabled( enabled );
    }

    @Override
    public void write( CompoundNBT nbt ) {
        super.write( nbt );
        nbt.putFloat( "effect", effect );
    }

    @Override
    public void read( CompoundNBT nbt ) {
        super.read( nbt );
        effect = nbt.getFloat( "effect" );
    }

    protected float fadeSpeed() {
        return 0.002F;
    }

    protected void updateEffect() {
        if( isActive() ) {
            effect += fadeSpeed();
            if( effect > 1 ) {
                effect = 1;
            }
        } else {
            effect -= fadeSpeed();
            if( effect < 0 ) {
                effect = 0;
            }
        }
    }

    @Override
    protected void onDisable() {
        effect = 0;
    }

    @Override
    public void tick() {
        super.tick();
        updateEffect();
    }
}
