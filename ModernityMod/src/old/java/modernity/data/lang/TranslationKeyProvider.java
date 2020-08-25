/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.data.lang;

import modernity.common.environment.event.EnvironmentEventType;
import modernity.common.item.group.MDItemGroup;
import modernity.common.registryold.MDRegistries;
import modernity.common.util.MDDamageSource;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class TranslationKeyProvider implements IDataProvider {
    protected final DataGenerator generator;
    protected final String namespace = "modernity";

    public TranslationKeyProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void act(DirectoryCache directoryCache) throws IOException {
        Path out = generator.getOutputFolder();
        out = out.resolve("assets/lang/generated.json");

        Files.createDirectories(out.getParent());

        OutputStream stream = Files.newOutputStream(out);
        PrintStream printer = new PrintStream(stream);
        printer.println("{");
        KeyPrinter collector = new KeyPrinter(printer);

        collectBlocks(collector);
        collectItems(collector);
        collectBiomes(collector);
        collectEntities(collector);
        collectEnvEvents(collector);
        collectWorldGenerators(collector);
        collectItemGroups(collector);
        collectCommands(collector);
        collectAdvancements(collector);
        collectDeathMessages(collector);

        printer.println("}");

        stream.close();
    }

    private void collectBiomes(KeyPrinter collector) {
        collector.block("BIOMES");

        for(Biome biome : ForgeRegistries.BIOMES) {
            ResourceLocation regName = biome.getRegistryName();
            if(regName != null && regName.getNamespace().equals(namespace)) {
                collector.add(biome.getTranslationKey(), guessName(regName.getPath()));
            }
        }
    }

    private void collectBlocks(KeyPrinter collector) {
        collector.block("BLOCKS");

        for(Block block : ForgeRegistries.BLOCKS) {
            ResourceLocation regName = block.getRegistryName();
            if(regName != null && regName.getNamespace().equals(namespace)) {
                collector.add(block.getTranslationKey(), guessName(regName.getPath()));
            }
        }
    }

    private void collectItems(KeyPrinter collector) {
        collector.block("ITEMS");

        for(Item item : ForgeRegistries.ITEMS) {
            ResourceLocation regName = item.getRegistryName();
            if(regName != null && regName.getNamespace().equals(namespace)) {
                String key = item.getTranslationKey();
                if(key.startsWith("item")) {
                    collector.add(item.getTranslationKey(), guessName(regName.getPath()));
                }
            }
        }
    }

    private void collectEntities(KeyPrinter collector) {
        collector.block("ENTITIES");

        for(EntityType<?> type : ForgeRegistries.ENTITIES) {
            ResourceLocation regName = type.getRegistryName();
            if(regName != null && regName.getNamespace().equals(namespace)) {
                collector.add(type.getTranslationKey(), guessName(regName.getPath()));
            }
        }
    }

    private void collectEnvEvents(KeyPrinter collector) {
        collector.block("ENVIRONMENT EVENTS");

        for(EnvironmentEventType type : MDRegistries.ENVIRONMENT_EVENTS) {
            ResourceLocation regName = type.getRegistryName();
            if(regName != null && regName.getNamespace().equals(namespace)) {
                collector.add(type.getTranslationKey(), guessName(regName.getPath()));
            }
        }
    }

    private void collectWorldGenerators(KeyPrinter collector) {
        collector.block("WORLD GENERATORS");

        for(ChunkGeneratorType<?, ?> type : ForgeRegistries.CHUNK_GENERATOR_TYPES) {
            ResourceLocation regName = type.getRegistryName();
            if(regName != null && regName.getNamespace().equals(namespace)) {
                collector.add(Util.makeTranslationKey("generator", regName), guessName(regName.getPath()));
            }
        }
    }

    private void collectItemGroups(KeyPrinter collector) {
        collector.block("ITEM GROUPS");

        for(MDItemGroup group : MDItemGroup.getModernityItemGroups()) {
            collector.add(group.getTranslationKey(), guessName("modernity_" + group.getPath()));
        }
    }

    private void collectDeathMessages(KeyPrinter collector) {
        collector.block("DEATH MESSAGES");

        for(String key : MDDamageSource.TRANSLATIONS) {
            collector.add(key, "[not guessed]");
        }
    }

    private void collectCommands(KeyPrinter collector) {
        collector.block("COMMANDS");

        collector.add("command.modernity.main.title", "The Modernity");
        collector.add("command.modernity.main.version", "Version %s: %s");
        collector.newline();
        collector.add("command.modernity.argument.enum_incorrect", "Enum Incorrect %s");
        collector.newline();
        collector.add("command.modernity.event.precipitation.status.inactive", "Inactive Precipitation %s for %s out of %s ticks");
        collector.add("command.modernity.event.precipitation.status.waiting", "Waiting Precipitation %s for %s out of %s ticks");
        collector.add("command.modernity.event.precipitation.status.active.none", "Active Precipitation %s for %s out of %s ticks - None");
        collector.add("command.modernity.event.precipitation.status.active.none.thunder", "Active Precipitation %s for %s out of %s ticks - None Thunder");
        collector.add("command.modernity.event.precipitation.status.active.light", "Active Precipitation %s for %s out of %s ticks - Light");
        collector.add("command.modernity.event.precipitation.status.active.light.thunder", "Active Precipitation %s for %s out of %s ticks - Light Thunder");
        collector.add("command.modernity.event.precipitation.status.active.moderate", "Active Precipitation %s for %s out of %s ticks - Moderate");
        collector.add("command.modernity.event.precipitation.status.active.moderate.thunder", "Active Precipitation %s for %s out of %s ticks - Moderate Thunder");
        collector.add("command.modernity.event.precipitation.status.active.reasonable", "Active Precipitation %s for %s out of %s ticks - Reasonable");
        collector.add("command.modernity.event.precipitation.status.active.reasonable.thunder", "Active Precipitation %s for %s out of %s ticks - Reasonable Thunder");
        collector.add("command.modernity.event.precipitation.status.active.heavy", "Active Precipitation %s for %s out of %s ticks - Heavy");
        collector.add("command.modernity.event.precipitation.status.active.heavy.thunder", "Active Precipitation %s for %s out of %s ticks - Heavy Thunder");
        collector.add("command.modernity.event.precipitation.status.cooldown", "Cooldown Precipitation %s for %s out of %s ticks");
        collector.newline();
        collector.add("command.modernity.event.schedule.status.inactive", "Inactive Schedule Event %s for %s out of %s ticks");
        collector.add("command.modernity.event.schedule.status.waiting", "Inactive Schedule Event %s for %s out of %s ticks");
        collector.add("command.modernity.event.schedule.status.active", "Inactive Schedule Event %s for %s out of %s ticks");
        collector.add("command.modernity.event.schedule.status.cooldown", "Inactive Schedule Event %s for %s out of %s ticks");
        collector.newline();
        collector.add("command.modernity.satellite.query", "Satellite - Phase %s Ticks %s");
        collector.add("command.modernity.satellite.set.phase", "Satellite - Set Phase %s");
        collector.add("command.modernity.satellite.set.tick", "Satellite - Set Ticks %s");
        collector.add("command.modernity.satellite.set.both", "Satellite - Set Ticks %s Phase %s");
        collector.add("command.modernity.satellite.play.full", "Satellite - Play Full");
        collector.add("command.modernity.satellite.play.new", "Satellite - Play New");
        collector.add("command.modernity.satellite.play.first", "Satellite - Play First");
        collector.add("command.modernity.satellite.play.last", "Satellite - Play Last");
        collector.add("command.modernity.satellite.play.phase", "Satellite - Play Phase %s");
        collector.newline();
        collector.add("command.modernity.access.invalid", "Invalid Dimension %s");
        collector.add("command.modernity.access.no_entity", "Not An Entity");
        collector.add("command.modernity.access.already_here", "Already Here");
        collector.add("command.modernity.access.changed_dimen", "Changed Dimen");
    }

    private void collectAdvancements(KeyPrinter collector) {
        collector.block("ADVANCEMENTS");

        collector.add("advancements.modernity.generic.bone_stone", "Bone Stone");
        collector.add("advancements.modernity.generic.bone_stone.desc", "Bone Stone Desc");
        collector.add("advancements.modernity.generic.elementary", "Elementary");
        collector.add("advancements.modernity.generic.elementary.desc", "Elementary Desc");
        collector.add("advancements.modernity.generic.extinguishable", "Extinguishable");
        collector.add("advancements.modernity.generic.extinguishable.desc", "Extinguishable Desc");
        collector.add("advancements.modernity.generic.grass_inc", "Grass Inc");
        collector.add("advancements.modernity.generic.grass_inc.desc", "Grass Inc Desc");
        collector.add("advancements.modernity.generic.let_it_goo", "Let It Goo");
        collector.add("advancements.modernity.generic.let_it_goo.desc", "Let It Goo Desc");
        collector.add("advancements.modernity.generic.light_it_up", "Light It Up");
        collector.add("advancements.modernity.generic.light_it_up.desc", "Light It Up Desc");
        collector.add("advancements.modernity.generic.metalized", "Metalized");
        collector.add("advancements.modernity.generic.metalized.desc", "Metalized Desc");
        collector.add("advancements.modernity.generic.moo_dernity", "Moo-dernity");
        collector.add("advancements.modernity.generic.moo_dernity.desc", "Moo-dernity Desc");
        collector.add("advancements.modernity.generic.murkified_darkness", "Murkified Darkness");
        collector.add("advancements.modernity.generic.murkified_darkness.desc", "Murkified Darkness Desc");
        collector.add("advancements.modernity.generic.murky_world", "Murky World");
        collector.add("advancements.modernity.generic.murky_world.desc", "Murky World Desc");
        collector.add("advancements.modernity.generic.personal_highway", "Personal Highway");
        collector.add("advancements.modernity.generic.personal_highway.desc", "Personal Highway Desc");
        collector.add("advancements.modernity.generic.root", "Root");
        collector.add("advancements.modernity.generic.root.desc", "Root Desc");
        collector.add("advancements.modernity.generic.salty_business", "Salty Business");
        collector.add("advancements.modernity.generic.salty_business.desc", "Salty Business Desc");
        collector.add("advancements.modernity.generic.seeds_for_your_weeds", "Seeds For Your Weeds");
        collector.add("advancements.modernity.generic.seeds_for_your_weeds.desc", "Seeds For Your Weeds Desc");
        collector.add("advancements.modernity.generic.shade_wisp", "Shade Wisp");
        collector.add("advancements.modernity.generic.shade_wisp.desc", "Shade Wisp Desc");
    }

    private String guessName(String path) {
        String[] sub = path.split("[_./]+");
        for(int i = 0; i < sub.length; i++) {
            String str = sub[i];
            if(str.isEmpty()) continue;
            sub[i] = Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
        }
        StringBuilder builder = new StringBuilder();
        boolean space = false;
        for(String s : sub) {
            if(space) builder.append(' ');
            else space = true;
            builder.append(s);
        }

        return builder.toString();
    }

    @Override
    public String getName() {
        return "TranslationKeyProvider";
    }

    private static class KeyPrinter {
        private static final String RULE_FORMAT = "  \"%s\": \"%s\"";
        private static final String HEADER_FORMAT = "  \"=== %s ===\": \"\"";

        private final PrintStream stream;
        private boolean comma;
        private boolean headerComma;

        KeyPrinter(PrintStream stream) {
            this.stream = stream;
        }

        private void printComma(boolean reset) {
            if(!comma) comma = true;
            else {
                stream.println(",");
                if(headerComma) {
                    stream.println();
                    headerComma = false;
                }
            }
            if(reset) comma = false;
        }

        void add(String key, String guess) {
            printComma(false);
            stream.printf(RULE_FORMAT, key, guess);
        }

        void newline() {
            printComma(true);
            stream.println();
        }

        void block(String name) {
            printComma(false);
            headerComma = true;

            stream.println();
            stream.println();
            stream.printf(HEADER_FORMAT, name);
        }
    }
}
