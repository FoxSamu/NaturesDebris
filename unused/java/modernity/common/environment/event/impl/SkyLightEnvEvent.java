/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.environment.event.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import modernity.generic.util.ColorUtil;
import modernity.generic.util.Functions;
import modernity.generic.util.Ticks;
import modernity.common.command.argument.EnumArgumentType;
import modernity.common.environment.event.*;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Random;

public class SkyLightEnvEvent extends EffectingEnvEvent {
    private Color color = Color.RED;

    public SkyLightEnvEvent( EnvironmentEventManager manager ) {
        super( MDEnvEvents.SKYLIGHT, manager );
    }

    public Color getColor() {
        return color;
    }

    public void setColor( Color color ) {
        this.color = color;
    }

    @Override
    protected int computeMaxTimeForPhase( Phase phase ) {
        switch( phase ) {
            case INACTIVE: return rand.nextInt( 40 * Ticks.MINUTES ) + 80 * Ticks.MINUTES;
            case WAITING: return - 1;
            case ACTIVE: return rand.nextInt( 20 * Ticks.MINUTES ) + 20 * Ticks.MINUTES;
            case COOLDOWN: return 0;
        }
        return 0;
    }

    @Override
    protected boolean canGoActive() {
        return rand.nextInt( 2000 ) == 0;
    }

    @Override
    protected void onStart() {
        color = Color.random( rand );
    }

    @Override
    public void write( CompoundNBT nbt ) {
        super.write( nbt );
        nbt.putByte( "color", (byte) color.ordinal() );
    }

    @Override
    public void read( CompoundNBT nbt ) {
        super.read( nbt );
        color = Color.fromInt( nbt.getByte( "color" ) );
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
                                .then(
                                    Commands.argument( "color", EnumArgumentType.enumerator( Color.class ) )
                                            .executes( ctx -> {
                                                Phase phase = ctx.getArgument( "phase", Phase.class );
                                                SkyLightEnvEvent ev = getFromCommand( ctx, type );
                                                ev.setPhase( phase, ctx.getArgument( "duration", Integer.class ) );
                                                ev.setColor( ctx.getArgument( "color", Color.class ) );
                                                return 0;
                                            } )
                                )
                    )
                    .then(
                        Commands.argument( "color", EnumArgumentType.enumerator( Color.class ) )
                                .executes( ctx -> {
                                    Phase phase = ctx.getArgument( "phase", Phase.class );
                                    SkyLightEnvEvent ev = getFromCommand( ctx, type );
                                    ev.setPhase( phase );
                                    ev.setColor( ctx.getArgument( "color", Color.class ) );
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

    public enum Color implements IStringSerializable {
        GREEN( 0x16ff01, "green" ),
        BLUE( 0x0441ff, "blue" ),
        YELLOW( 0xfff910, "yellow" ),
        INDIGO( 0x2101f9, "indigo" ),
        ORANGE( 0xf99802, "orange" ),
        WHITE( 0xfffedf, "white" ),
        RED( 0xff1002, "red" ),
        MAGENTA( 0xfa01ea, "magenta" );

        public final float r;
        public final float g;
        public final float b;

        private final String name;

        Color( int rgb, String name ) {
            this.r = ColorUtil.redf( rgb );
            this.g = ColorUtil.greenf( rgb );
            this.b = ColorUtil.bluef( rgb );
            this.name = name;
        }

        public static Color random( Random rand ) {
            return values()[ rand.nextInt( values().length ) ];
        }

        public static Color fromInt( int ordinal ) {
            return values()[ ordinal ];
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
