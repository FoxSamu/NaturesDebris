/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import modernity.common.environment.satellite.SatelliteData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import modernity.api.util.exc.UnexpectedCaseException;

import java.util.ArrayList;

/**
 * Manages the {@code /modernity tpdim} or {@code /modernity access} command.
 */
public final class SatelliteCommand {
    private static final String TK_QUERY = Util.makeTranslationKey("command", new ResourceLocation("modernity:satellite.query"));
    private static final String TK_SET_PHASE = Util.makeTranslationKey("command", new ResourceLocation("modernity:satellite.set.phase"));
    private static final String TK_SET_TICK = Util.makeTranslationKey("command", new ResourceLocation("modernity:satellite.set.tick"));
    private static final String TK_SET_BOTH = Util.makeTranslationKey("command", new ResourceLocation("modernity:satellite.set.both"));
    private static final String TK_PLAY_FULL = Util.makeTranslationKey("command", new ResourceLocation("modernity:satellite.play.full"));
    private static final String TK_PLAY_NEW = Util.makeTranslationKey("command", new ResourceLocation("modernity:satellite.play.new"));
    private static final String TK_PLAY_FIRST = Util.makeTranslationKey("command", new ResourceLocation("modernity:satellite.play.first"));
    private static final String TK_PLAY_LAST = Util.makeTranslationKey("command", new ResourceLocation("modernity:satellite.play.last"));
    private static final String TK_PLAY_PHASE = Util.makeTranslationKey("command", new ResourceLocation("modernity:satellite.play.phase"));

    private SatelliteCommand() {
    }

    public static void createCommand(ArrayList<LiteralArgumentBuilder<CommandSource>> list) {
        createCommand("satellite", list);
        createCommand("moon", list);
    }

    private static void createCommand(String name, ArrayList<LiteralArgumentBuilder<CommandSource>> list) {
        list.add(
            Commands.literal(name)
                    .requires(SatelliteCommand::checkSource)
                    .then(
                        Commands.literal("query")
                                .requires(src -> src.hasPermissionLevel(2))
                                .executes(ctx -> {
                                    CommandSource src = ctx.getSource();
                                    SatelliteData data = getData(src);
                                    src.sendFeedback(new TranslationTextComponent(
                                        TK_QUERY,
                                        data.getPhase(),
                                        data.getTick()
                                    ), true);
                                    return 0;
                                })
                    )
                    .then(
                        Commands.literal("set")
                                .requires(src -> src.hasPermissionLevel(2))
                                .then(
                                    Commands.literal("phase")
                                            .then(
                                                Commands.argument("phase", IntegerArgumentType.integer())
                                                        .executes(ctx -> {
                                                            CommandSource src = ctx.getSource();
                                                            SatelliteData data = getData(src);
                                                            data.setPhase(ctx.getArgument("phase", Integer.class));
                                                            src.sendFeedback(new TranslationTextComponent(
                                                                TK_SET_PHASE,
                                                                data.getPhase()
                                                            ), true);
                                                            return 0;
                                                        })
                                            )
                                )
                                .then(
                                    Commands.literal("tick")
                                            .then(
                                                Commands.argument("tick", IntegerArgumentType.integer())
                                                        .executes(ctx -> {
                                                            CommandSource src = ctx.getSource();
                                                            SatelliteData data = getData(src);
                                                            data.setTick(ctx.getArgument("tick", Integer.class));
                                                            src.sendFeedback(new TranslationTextComponent(
                                                                TK_SET_TICK,
                                                                data.getTick()
                                                            ), true);
                                                            return 0;
                                                        })
                                            )
                                )
                                .then(
                                    Commands.argument("tick", IntegerArgumentType.integer())
                                            .then(
                                                Commands.argument("phase", IntegerArgumentType.integer())
                                                        .executes(ctx -> {
                                                            CommandSource src = ctx.getSource();
                                                            SatelliteData data = getData(src);
                                                            data.setTick(ctx.getArgument("tick", Integer.class));
                                                            data.setPhase(ctx.getArgument("phase", Integer.class));
                                                            src.sendFeedback(new TranslationTextComponent(
                                                                TK_SET_BOTH,
                                                                data.getTick(),
                                                                data.getPhase()
                                                            ), true);
                                                            return 0;
                                                        })
                                            )
                                )
                    )
                    .then(
                        Commands.literal("play")
                                .then(
                                    Commands.literal("fullmoon")
                                            .executes(ctx -> {
                                                CommandSource src = ctx.getSource();
                                                SatelliteData data = getData(src);
                                                data.setTick(0);
                                                data.setPhase(0);
                                                src.sendFeedback(new TranslationTextComponent(TK_PLAY_FULL), true);
                                                return 0;
                                            })
                                )
                                .then(
                                    Commands.literal("newmoon")
                                            .executes(ctx -> {
                                                CommandSource src = ctx.getSource();
                                                SatelliteData data = getData(src);
                                                data.setTick(0);
                                                data.setPhase(4);
                                                src.sendFeedback(new TranslationTextComponent(TK_PLAY_NEW), true);
                                                return 0;
                                            })
                                )
                                .then(
                                    Commands.literal("firstquarter")
                                            .executes(ctx -> {
                                                CommandSource src = ctx.getSource();
                                                SatelliteData data = getData(src);
                                                data.setTick(0);
                                                data.setPhase(6);
                                                src.sendFeedback(new TranslationTextComponent(TK_PLAY_FIRST), true);
                                                return 0;
                                            })
                                )
                                .then(
                                    Commands.literal("lastquarter")
                                            .executes(ctx -> {
                                                CommandSource src = ctx.getSource();
                                                SatelliteData data = getData(src);
                                                data.setTick(0);
                                                data.setPhase(2);
                                                src.sendFeedback(new TranslationTextComponent(TK_PLAY_LAST), true);
                                                return 0;
                                            })
                                )
                                .then(
                                    Commands.argument("phase", IntegerArgumentType.integer())
                                            .executes(ctx -> {
                                                CommandSource src = ctx.getSource();
                                                SatelliteData data = getData(src);
                                                data.setTick(0);
                                                data.setPhase(ctx.getArgument("phase", Integer.class));
                                                src.sendFeedback(new TranslationTextComponent(
                                                    TK_PLAY_PHASE,
                                                    data.getPhase()
                                                ), true);
                                                return 0;
                                            })
                                )
                    )
        );
    }

    private static boolean checkSource(CommandSource src) {
//        return src.getWorld().dimension instanceof ISatelliteDimension;
        return false; // TODO
    }

    private static SatelliteData getData(CommandSource src) {
//        World world = src.getWorld();
//        if( world.dimension instanceof ISatelliteDimension ) {
//            return ( (ISatelliteDimension) world.dimension ).getSatelliteData();
//        }
        throw new UnexpectedCaseException(); // TODO
    }
}
