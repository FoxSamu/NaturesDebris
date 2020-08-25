/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.environment.event.impl;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import modernity.common.command.argument.EnumArgumentType;
import modernity.common.environment.event.*;
import modernity.common.environment.precipitation.EPrecipitationLevel;
import modernity.generic.util.Functions;
import modernity.generic.util.Ticks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;

public class PrecipitationEnvEvent extends EffectingEnvEvent {
    public static final String TK_STATUS_PREC = Util.makeTranslationKey("command", new ResourceLocation("modernity:event.precipitation.status"));
    private int level;
    private boolean thunderstorm;

    public PrecipitationEnvEvent(EnvironmentEventManager manager) {
        super(MDEnvEvents.PRECIPITATION, manager);
    }

    public static void buildCommand(ArrayList<ArgumentBuilder<CommandSource, ?>> list, EnvironmentEventType type) {
        list.add(
            Commands.literal("restart")
                    .requires(src -> src.hasPermissionLevel(2))
                    .executes(ctx -> {
                        ScheduledEnvEvent ev = getFromCommand(ctx, type);
                        ev.restartSchedule();
                        return 0;
                    })
        );
        list.add(
            Commands.literal("next")
                    .requires(src -> src.hasPermissionLevel(2))
                    .executes(ctx -> {
                        ScheduledEnvEvent ev = getFromCommand(ctx, type);
                        ev.nextPhase();
                        return 0;
                    })
                    .then(
                        Commands.argument("count", IntegerArgumentType.integer(1, 4))
                                .executes(ctx -> {
                                    ScheduledEnvEvent ev = getFromCommand(ctx, type);
                                    int t = ctx.getArgument("count", Integer.class);
                                    for(int i = 0; i < t; i++) ev.nextPhase();
                                    return 0;
                                })
                    )
        );
        list.add(
            Commands.literal("set")
                    .requires(src -> src.hasPermissionLevel(2))
                    .then(
                        Commands.argument("phase", EnumArgumentType.enumerator(Phase.class))
                                .executes(Functions.tryOrPrint(ctx -> {
                                    Phase phase = ctx.getArgument("phase", Phase.class);
                                    ScheduledEnvEvent ev = getFromCommand(ctx, type);
                                    ev.setPhase(phase);
                                    return 0;
                                }, System.out))
                                .then(
                                    Commands.argument("level", EnumArgumentType.enumerator(EPrecipitationLevel.class, l -> l != EPrecipitationLevel.NONE))
                                            .executes(Functions.tryOrPrint(ctx -> {
                                                Phase phase = ctx.getArgument("phase", Phase.class);
                                                PrecipitationEnvEvent ev = getFromCommand(ctx, type);
                                                ev.setPhase(phase);
                                                ev.setLevel(ctx.getArgument("level", EPrecipitationLevel.class).getLevel());
                                                return 0;
                                            }, System.out))
                                            .then(
                                                Commands.argument("thunder", BoolArgumentType.bool())
                                                        .executes(Functions.tryOrPrint(ctx -> {
                                                            Phase phase = ctx.getArgument("phase", Phase.class);
                                                            PrecipitationEnvEvent ev = getFromCommand(ctx, type);
                                                            ev.setPhase(phase);
                                                            ev.setLevel(ctx.getArgument("level", EPrecipitationLevel.class).getLevel());
                                                            ev.setThunderstorm(ctx.getArgument("thunder", Boolean.class));
                                                            return 0;
                                                        }, System.out))
                                            )
                                )
                                .then(
                                    Commands.argument("duration", IntegerArgumentType.integer(0))
                                            .executes(Functions.tryOrPrint(ctx -> {
                                                Phase phase = ctx.getArgument("phase", Phase.class);
                                                ScheduledEnvEvent ev = getFromCommand(ctx, type);
                                                ev.setPhase(phase, ctx.getArgument("duration", Integer.class));
                                                return 0;
                                            }, System.out))
                                            .then(
                                                Commands.argument("level", EnumArgumentType.enumerator(EPrecipitationLevel.class, l -> l != EPrecipitationLevel.NONE))
                                                        .executes(Functions.tryOrPrint(ctx -> {
                                                            Phase phase = ctx.getArgument("phase", Phase.class);
                                                            PrecipitationEnvEvent ev = getFromCommand(ctx, type);
                                                            ev.setPhase(phase, ctx.getArgument("duration", Integer.class));
                                                            ev.setLevel(ctx.getArgument("level", EPrecipitationLevel.class).getLevel());
                                                            return 0;
                                                        }, System.out))
                                                        .then(
                                                            Commands.argument("thunder", BoolArgumentType.bool())
                                                                    .executes(Functions.tryOrPrint(ctx -> {
                                                                        Phase phase = ctx.getArgument("phase", Phase.class);
                                                                        PrecipitationEnvEvent ev = getFromCommand(ctx, type);
                                                                        ev.setPhase(phase, ctx.getArgument("duration", Integer.class));
                                                                        ev.setLevel(ctx.getArgument("level", EPrecipitationLevel.class).getLevel());
                                                                        ev.setThunderstorm(ctx.getArgument("thunder", Boolean.class));
                                                                        return 0;
                                                                    }, System.out))
                                                        )
                                            )
                                            .then(
                                                Commands.argument("level", IntegerArgumentType.integer(1, 4))
                                                        .executes(Functions.tryOrPrint(ctx -> {
                                                            Phase phase = ctx.getArgument("phase", Phase.class);
                                                            PrecipitationEnvEvent ev = getFromCommand(ctx, type);
                                                            ev.setPhase(phase, ctx.getArgument("duration", Integer.class));
                                                            ev.setLevel(ctx.getArgument("level", Integer.class));
                                                            return 0;
                                                        }, System.out))
                                                        .then(
                                                            Commands.argument("thunder", BoolArgumentType.bool())
                                                                    .executes(Functions.tryOrPrint(ctx -> {
                                                                        Phase phase = ctx.getArgument("phase", Phase.class);
                                                                        PrecipitationEnvEvent ev = getFromCommand(ctx, type);
                                                                        ev.setPhase(phase, ctx.getArgument("duration", Integer.class));
                                                                        ev.setLevel(ctx.getArgument("level", Integer.class));
                                                                        ev.setThunderstorm(ctx.getArgument("thunder", Boolean.class));
                                                                        return 0;
                                                                    }, System.out))
                                                        )
                                            )
                                )
                    )
        );

        list.add(
            Commands.literal("setlevel")
                    .requires(src -> src.hasPermissionLevel(2))
                    .then(
                        Commands.argument("level", EnumArgumentType.enumerator(EPrecipitationLevel.class, l -> l != EPrecipitationLevel.NONE))
                                .executes(ctx -> {
                                    PrecipitationEnvEvent ev = getFromCommand(ctx, type);
                                    ev.setLevel(ctx.getArgument("level", EPrecipitationLevel.class).getLevel());
                                    return 0;
                                })
                    )
                    .then(
                        Commands.argument("level", IntegerArgumentType.integer(1, 4))
                                .executes(ctx -> {
                                    PrecipitationEnvEvent ev = getFromCommand(ctx, type);
                                    ev.setLevel(ctx.getArgument("level", Integer.class));
                                    return 0;
                                })
                    )
        );

        list.add(
            Commands.literal("setthunder")
                    .requires(src -> src.hasPermissionLevel(2))
                    .then(
                        Commands.argument("thunder", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    PrecipitationEnvEvent ev = getFromCommand(ctx, type);
                                    ev.setThunderstorm(ctx.getArgument("thunder", Boolean.class));
                                    return 0;
                                })
                    )
        );

        list.add(
            Commands.literal("status")
                    .executes(Functions.tryOrPrint(ctx -> {
                        CommandSource src = ctx.getSource();
                        PrecipitationEnvEvent ev = getFromCommand(ctx, type);
                        Phase phase = ev.getPhase();
                        EPrecipitationLevel level = EPrecipitationLevel.fromInt(ev.getLevel());
                        String levelExtend = "." + level.getName()
                                                 + (ev.isThunderstorm() ? ".thunder" : "");
                        src.sendFeedback(
                            new TranslationTextComponent(
                                TK_STATUS_PREC + "." + phase.name().toLowerCase() + (phase == Phase.ACTIVE
                                                                                     ? levelExtend
                                                                                     : ""),
                                type.getRegistryName(),
                                ev.getTimeInPhase(),
                                ev.getMaxTimeInPhase()
                            ),
                            true
                        );
                        return 0;
                    }, System.out))
        );
    }

    public int getLevel() {
        return level;
    }

    void setLevel(int level) {
        this.level = level;
    }

    public boolean isThunderstorm() {
        return thunderstorm;
    }

    void setThunderstorm(boolean thunderstorm) {
        this.thunderstorm = thunderstorm;
    }

    @Override
    protected int computeMaxTimeForPhase(Phase phase) {
        switch(phase) {
            case INACTIVE: return rand.nextInt(20 * Ticks.MINUTES) + 40 * Ticks.MINUTES;
            case WAITING: return getManager().getByType(MDEnvEvents.CLOUDLESS).isActive() ? 0 : -1;
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
        getManager().getByType(MDEnvEvents.CLOUDLESS).setActive(false);
        level = rand.nextInt(4) + rand.nextInt(2);
        if(level == 0) level = 1;
        switch(level) {
            default:
                thunderstorm = false;
                break;
            case 2:
                thunderstorm = rand.nextInt(10) == 0;
                break;
            case 3 | 4:
                thunderstorm = rand.nextInt(4) == 0;
                break;
        }
    }

    @Override
    public void write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.putByte("level", (byte) level);
        nbt.putBoolean("thunder", thunderstorm);
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        level = nbt.getByte("level");
        thunderstorm = nbt.getBoolean("thunder");
    }

    @Override
    protected void onDisable() {
        level = 0;
        thunderstorm = false;
    }
}
