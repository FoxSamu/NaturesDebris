/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.environment.event;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import modernity.common.command.argument.EnumArgumentType;
import modernity.generic.util.Functions;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Random;

/**
 * An environment event that has an always-repeating schedule:
 * <ol>
 * <li>{@linkplain Phase#INACTIVE Be inactive for a while}</li>
 * <li>{@linkplain Phase#WAITING Wait for something to happen and if nothing happened after a while, restart
 * schedule}</li>
 * <li>{@linkplain Phase#ACTIVE Be active for a while}</li>
 * <li>{@linkplain Phase#COOLDOWN Do some after effects and restart schedule}</li>
 * </ol>
 * Implementations can define how long an event is in a specific phase and can jump between phases if necessary.
 */
public abstract class ScheduledEnvEvent extends EnvironmentEvent {
    protected static final String TK_STATUS = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:event.schedule.status" ) );

    private Phase phase;
    private int timeInPhase;
    private int maxTimeInPhase;
    protected final Random rand;

    public ScheduledEnvEvent( EnvironmentEventType type, EnvironmentEventManager manager ) {
        super( type, manager );
        if( manager == null ) { // Setting up commands, no Random needed as commands are not random
            rand = null;
            return;
        }
        rand = new Random( manager.getWorld().rand.nextLong() );
        setPhase( Phase.INACTIVE );
    }

    /**
     * Updates the {@linkplain Phase phase} of this event. This resets the {@linkplain #getTimeInPhase() phase tick
     * counter} and {@linkplain #computeMaxTimeForPhase(Phase) recomputes the phase time}.
     *
     * @param phase The new phase.
     */
    public void setPhase( Phase phase ) {
        setPhase( phase, computeMaxTimeForPhase( phase ) );
    }

    /**
     * Updates the {@linkplain Phase phase} of this event. This resets the {@linkplain #getTimeInPhase() phase tick
     * counter} and set the phase time to the specified amount of ticks.
     *
     * @param phase The new phase.
     * @param time  The time this phase should last.
     */
    public void setPhase( Phase phase, int time ) {
        Validate.notNull( phase, "Phase must not be null" );
        this.phase = phase;

        timeInPhase = 0;
        maxTimeInPhase = time;

        super.setActive( phase == Phase.ACTIVE );

        onChangePhase();

        if( phase == Phase.INACTIVE ) {
            onRestartSchedule();
        }
    }

    /**
     * Returns the current {@linkplain Phase phase} of this event.
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Returns the amount of ticks since the last {@linkplain #setPhase(Phase) phase change}.
     */
    public int getTimeInPhase() {
        return timeInPhase;
    }

    /**
     * Sets the amount of ticks since the last {@linkplain #setPhase(Phase) phase change}.
     */
    public void setTimeInPhase( int timeInPhase ) {
        this.timeInPhase = timeInPhase;
    }

    /**
     * Returns the maximum amount of ticks this event may be in it's current phase.
     */
    public int getMaxTimeInPhase() {
        return maxTimeInPhase;
    }

    /**
     * Sets the maximum amount of ticks this event may be in it's current phase.
     */
    public void setMaxTimeInPhase( int maxTimeInPhase ) {
        this.maxTimeInPhase = maxTimeInPhase;
    }

    /**
     * Switches to the next phase in the schedule of this event.
     *
     * @see #setPhase(Phase)
     */
    public void nextPhase() {
        int o = phase.ordinal() + 1;
        if( o == Phase.values().length ) o = 0;

        setPhase( Phase.values()[ o ] );
    }

    /**
     * Restarts the schedule of this event by setting it's phase to {@linkplain Phase#INACTIVE <code>INACTIVE</code>}.
     */
    public void restartSchedule() {
        setPhase( Phase.INACTIVE );
    }

    /**
     * Computes the maximum time this event lasts in a specific phase. Use {@link #rand} for randomizing this value.
     */
    protected abstract int computeMaxTimeForPhase( Phase phase );

    /**
     * Checks if this event can go active during the {@linkplain Phase#WAITING <code>WAITING</code>} phase.
     */
    protected abstract boolean canGoActive();

    /**
     * Called on phase change.
     */
    protected void onChangePhase() {

    }

    /**
     * Called when the event's schedule starts all over again.
     */
    protected void onRestartSchedule() {

    }

    /**
     * Returns true when this event is in it's {@linkplain Phase#COOLDOWN <code>COOLDOWN</code>} phase, so that
     * renderers can play post-event effects.
     */
    protected boolean isCoolingDown() {
        return phase == Phase.COOLDOWN;
    }

    @Override
    public void setActive( boolean active ) {
        if( ! active && phase == Phase.ACTIVE ) {
            nextPhase();
        } else if( active && phase != Phase.ACTIVE ) {
            setPhase( Phase.ACTIVE );
        }
    }

    @Override
    public boolean isActive() {
        return phase == Phase.ACTIVE;
    }

    @Override
    public void tick() {
        timeInPhase++;
        if( maxTimeInPhase >= 0 && timeInPhase >= maxTimeInPhase ) {
            if( phase == Phase.WAITING ) {
                restartSchedule();
            } else {
                nextPhase();
            }
        }
        if( phase == Phase.WAITING ) {
            if( canGoActive() ) {
                nextPhase();
            }
        }
    }

    @Override
    public void write( CompoundNBT nbt ) {
        nbt.putByte( "phase", (byte) phase.ordinal() );
        nbt.putInt( "ticks", timeInPhase );
        nbt.putInt( "max", maxTimeInPhase );
    }

    @Override
    public void read( CompoundNBT nbt ) {
        phase = Phase.values()[ nbt.getByte( "phase" ) ];
        timeInPhase = nbt.getInt( "ticks" );
        maxTimeInPhase = nbt.getInt( "max" );
    }

    @Override
    protected void onDisable() {
        setPhase( Phase.INACTIVE );
    }

    public static void buildCommand( ArrayList<ArgumentBuilder<CommandSource, ?>> list, EnvironmentEventType type ) {
        list.add(
            Commands.literal( "restart" )
                    .requires( src -> src.hasPermissionLevel( 2 ) )
                    .executes( ctx -> {
                        ScheduledEnvEvent ev = getFromCommand( ctx, type );
                        ev.restartSchedule();
                        return 0;
                    } )
        );
        list.add(
            Commands.literal( "next" )
                    .requires( src -> src.hasPermissionLevel( 2 ) )
                    .executes( ctx -> {
                        ScheduledEnvEvent ev = getFromCommand( ctx, type );
                        ev.nextPhase();
                        return 0;
                    } )
                    .then(
                        Commands.argument( "count", IntegerArgumentType.integer( 1, 4 ) )
                                .executes( ctx -> {
                                    ScheduledEnvEvent ev = getFromCommand( ctx, type );
                                    int t = ctx.getArgument( "count", Integer.class );
                                    for( int i = 0; i < t; i++ ) ev.nextPhase();
                                    return 0;
                                } )
                    )
        );
        LiteralArgumentBuilder<CommandSource> setCMD
            = Commands.literal( "set" )
                      .requires( src -> src.hasPermissionLevel( 2 ) );

        setCMD.then(
            Commands.argument( "phase", EnumArgumentType.enumerator( Phase.class ) )
                    .executes( ctx -> {
                        Phase phase = ctx.getArgument( "phase", Phase.class );
                        ScheduledEnvEvent ev = getFromCommand( ctx, type );
                        ev.setPhase( phase );
                        return 0;
                    } )
                    .then(
                        Commands.argument( "duration", IntegerArgumentType.integer( 0 ) )
                                .executes( ctx -> {
                                    Phase phase = ctx.getArgument( "phase", Phase.class );
                                    ScheduledEnvEvent ev = getFromCommand( ctx, type );
                                    ev.setPhase( phase, ctx.getArgument( "duration", Integer.class ) );
                                    return 0;
                                } )
                    )
        );

        list.add( setCMD );

        list.add(
            Commands.literal( "status" )
                    .executes( Functions.tryOrPrint( ctx -> {
                        CommandSource src = ctx.getSource();
                        ScheduledEnvEvent ev = getFromCommand( ctx, type );
                        Phase phase = ev.getPhase();
                        src.sendFeedback(
                            new TranslationTextComponent(
                                TK_STATUS + "." + phase.name().toLowerCase(),
                                type.getRegistryName(),
                                ev.getTimeInPhase(),
                                ev.getMaxTimeInPhase()
                            ),
                            true
                        );
                        return 0;
                    }, System.out ) )
        );
    }

    @Override
    public String debugInfo() {
        String ticks = getMaxTimeInPhase() < 0
                       ? "$a" + getTimeInPhase() + "$r"
                       : "$a" + getTimeInPhase() + "$r out of $9" + getMaxTimeInPhase() + "$r";

        return "$e" + getPhase().name() + "$r for " + ticks + " ticks";
    }

    /**
     * The 4 phases of an event.
     */
    public enum Phase implements IStringSerializable {
        /** When inactive, an event is (tada!) inactive. */
        INACTIVE,

        /** When waiting, an event is waiting for something to happen before it can go active. */
        WAITING,

        /** When active, an event is (again tada!) active. */
        ACTIVE,

        /** When cooling down, an event is inactive, but it may play some post-event effects. */
        COOLDOWN;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }
}
