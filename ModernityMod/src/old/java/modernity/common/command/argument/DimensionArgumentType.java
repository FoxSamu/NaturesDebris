/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.command.argument;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;

import java.util.concurrent.CompletableFuture;

/**
 * Argument type that accepts and suggests a dimension.
 */
public class DimensionArgumentType implements ArgumentType<DimensionType> {


    @Override
    public DimensionType parse(StringReader reader) throws CommandSyntaxException {
        int cursor = reader.getCursor();

        ResourceLocation read = ResourceLocation.read(reader);
        for(DimensionType type : DimensionType.getAll()) {
            ResourceLocation loc = DimensionType.getKey(type);
            if(read.equals(loc)) {
                return type;
            }
        }
        reader.setCursor(cursor);
        throw new SimpleCommandExceptionType(new LiteralMessage("No such dimension")).createWithContext(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        StringReader reader = new StringReader(builder.getInput());
        reader.setCursor(builder.getStart());

        String read = reader.readUnquotedString();
        if(reader.canRead() && reader.peek() == ':') {
            read += ":" + reader.readUnquotedString();
        }
        String read2 = new ResourceLocation(read).toString();
        for(DimensionType type : DimensionType.getAll()) {
            String loc = DimensionType.getKey(type) + "";
            if(loc.startsWith(read) || loc.startsWith(read2)) {
                builder.suggest(loc);
            }
        }

        return builder.buildFuture();
    }
}
