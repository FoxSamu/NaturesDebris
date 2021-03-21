package net.shadew.ndebris.data;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.data.DataGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;

import net.shadew.ndebris.common.NaturesDebris;
import net.shadew.ndebris.data.loottables.NdLootTablesProvider;
import net.shadew.ndebris.data.models.NdStateModelProvider;
import net.shadew.ndebris.data.recipes.NdRecipeProvider;
import net.shadew.ndebris.data.tags.NdBlockTagsProvider;
import net.shadew.ndebris.data.tags.NdFluidTagsProvider;
import net.shadew.ndebris.data.tags.NdItemTagsProvider;

public class DataMain {
    public static void main(String[] strings) throws IOException {
        OptionParser opts = new OptionParser();

        OptionSpec<Void> help = opts.accepts("help", "Show the help menu").forHelp();
        OptionSpec<Void> server = opts.accepts("server", "Include server generators");
        OptionSpec<Void> client = opts.accepts("client", "Include client generators");
        OptionSpec<Void> dev = opts.accepts("dev", "Include development tools");
        OptionSpec<Void> reports = opts.accepts("reports", "Include data reports");
        OptionSpec<Void> validate = opts.accepts("validate", "Validate inputs");
        OptionSpec<Void> all = opts.accepts("all", "Include all generators");

        OptionSpec<String> output = opts.accepts("output", "Output folder").withRequiredArg().defaultsTo("generated");
        OptionSpec<String> input = opts.accepts("input", "Input folder").withRequiredArg();

        OptionSet optionSet = opts.parse(strings);
        if (!optionSet.has(help) && optionSet.hasOptions()) {
            Path path = Paths.get(output.value(optionSet));
            boolean genAll = optionSet.has(all);
            boolean genClient = genAll || optionSet.has(client);
            boolean genServer = genAll || optionSet.has(server);
            boolean genDev = genAll || optionSet.has(dev);
            boolean genReports = genAll || optionSet.has(reports);
            boolean genValidate = genAll || optionSet.has(validate);
            DataGenerator gen = create(
                path,
                optionSet.valuesOf(input)
                         .stream()
                         .map(Paths::get)
                         .collect(Collectors.toList()),
                genClient,
                genServer,
                genDev,
                genReports,
                genValidate
            );
            new NaturesDebris().onInitialize();
            gen.run();
        } else {
            opts.printHelpOn(System.out);
        }
    }

    public static DataGenerator create(Path output, Collection<Path> inputs, boolean includeClient, boolean includeServer, boolean includeDev, boolean includeReports, boolean validate) {
        DataGenerator gen = new DataGenerator(output, inputs);
        if (includeClient) {
            gen.install(new NdStateModelProvider(gen));
        }

        if (includeServer) {
            gen.install(new NdFluidTagsProvider(gen));
            NdBlockTagsProvider blockTags = new NdBlockTagsProvider(gen);
            gen.install(blockTags);
            gen.install(new NdItemTagsProvider(gen, blockTags));
            gen.install(new NdRecipeProvider(gen));
            gen.install(new NdLootTablesProvider(gen));
        }

        return gen;
    }
}
