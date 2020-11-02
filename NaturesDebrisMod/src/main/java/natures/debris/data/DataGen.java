/*
 * Copyright (c) 2020 Cryptic Mushroom and contributors
 * This file belongs to the Midnight mod and is licensed under the terms and conditions of Cryptic Mushroom. See
 * https://github.com/Cryptic-Mushroom/The-Midnight/blob/rewrite/LICENSE.md for the full license.
 *
 * Last updated: 2020 - 10 - 25
 */

package natures.debris.data;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Bootstrap;
import net.minecraft.util.registry.Registry;

import natures.debris.core.util.IRegistry;
import natures.debris.common.block.NdBlocks;
import natures.debris.common.item.NdItems;
import natures.debris.common.sound.NdSoundEvents;
import natures.debris.data.loottables.NdLootTableProvider;
import natures.debris.data.models.NdStateModelProvider;
import natures.debris.data.recipes.NdRecipeProvider;
import natures.debris.data.recipes.NdStonecuttingRecipeProvider;
import natures.debris.data.tags.NdBlockTagsProvider;
import natures.debris.data.tags.NdFluidTagsProvider;
import natures.debris.data.tags.NdItemTagsProvider;

/**
 * Data generator that circumvents Forges very slow modloading. Using this data can be generated rapidly right before
 * the processResources Gradle task runs.
 *
 * @author Shadew
 * @since 0.6.0
 */
public final class DataGen {
    private DataGen() {
    }

    public static void main(String[] args) throws IOException {
        OptionParser parser = new OptionParser();

        OptionSpec<Void> helpSpec = parser.accepts("help", "Show the help menu")
                                          .forHelp();
        OptionSpec<Void> serverSpec = parser.accepts("server", "Include server generators");
        OptionSpec<Void> clientSpec = parser.accepts("client", "Include client generators");
        OptionSpec<Void> devSpec = parser.accepts("dev", "Include development tools");
        OptionSpec<Void> reportsSpec = parser.accepts("reports", "Include data reports");
        OptionSpec<Void> validateSpec = parser.accepts("validate", "Validate inputs");
        OptionSpec<Void> allSpec = parser.accepts("all", "Include all generators");
        OptionSpec<String> outputSpec = parser.accepts("output", "Output folder")
                                              .withRequiredArg()
                                              .defaultsTo("generated");
        OptionSpec<String> inputSpec = parser.accepts("input", "Input folder")
                                             .withRequiredArg();
        OptionSpec<File> gameDirSpec = parser.accepts("gameDir")
                                             .withRequiredArg()
                                             .ofType(File.class)
                                             .defaultsTo(new File("."))
                                             .required();
        OptionSet opts = parser.parse(args);

        if (!opts.has(helpSpec) && opts.hasOptions() && !(opts.specs().size() == 1 && opts.has(gameDirSpec))) {
            Path output = Paths.get(outputSpec.value(opts));
            boolean all = opts.has(allSpec);
            boolean client = all || opts.has(clientSpec);
            boolean server = all || opts.has(serverSpec);
            boolean dev = all || opts.has(devSpec);
            boolean reports = all || opts.has(reportsSpec);
            boolean validate = all || opts.has(validateSpec);
            Collection<Path> inputs = opts.valuesOf(inputSpec).stream().map(Paths::get).collect(Collectors.toList());
            bootstrap();
            makeGenerator(output, inputs, client, server, dev, reports, validate).run();
        } else {
            parser.printHelpOn(System.out);
        }
    }

    @SuppressWarnings("deprecation")
    private static void bootstrap() {
        Bootstrap.register();

        // We must not access Forge registries - here we hack directly into minecraft by registering through vanilla's
        // registries and imitate Forge's ObjectHolder using ObjectHolderHacks. Sounds dangerous, but it works and is
        // much faster.

        NdBlocks.registerBlocks(IRegistry.vanilla(Registry.BLOCK));
        ObjectHolderHacks.hackObjectHolder(NdBlocks.class, Registry.BLOCK, Block.class);

        NdBlocks.registerItems(IRegistry.vanilla(Registry.ITEM));
        NdItems.registerItems(IRegistry.vanilla(Registry.ITEM));
        ObjectHolderHacks.hackObjectHolder(NdItems.class, Registry.ITEM, Item.class);

        NdSoundEvents.registerSoundEvents(IRegistry.vanilla(Registry.SOUND_EVENT));
    }

    public static DataGenerator makeGenerator(Path out, Collection<Path> ins, boolean client, boolean server, boolean dev, boolean reports, boolean validate) {
        DataGenerator gen = new DataGenerator(out, ins);

        if (client) {
            gen.addProvider(new NdStateModelProvider(gen));
        }
        if (server) {
            gen.addProvider(new NdLootTableProvider(gen));

            NdBlockTagsProvider blockTags = new NdBlockTagsProvider(gen);
            gen.addProvider(blockTags);
            gen.addProvider(new NdItemTagsProvider(gen, blockTags));
            gen.addProvider(new NdFluidTagsProvider(gen));

            gen.addProvider(new NdRecipeProvider(gen));
            gen.addProvider(new NdStonecuttingRecipeProvider(gen));
        }

        return gen;
    }
}
