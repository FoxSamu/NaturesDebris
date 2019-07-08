/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 8 - 2019
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

public class DimensionArgumentType implements ArgumentType<DimensionType> {


    @Override
    public DimensionType parse( StringReader reader ) throws CommandSyntaxException {
        int cursor = reader.getCursor();

        ResourceLocation read = ResourceLocation.read( reader );
        for( DimensionType type : DimensionType.getAll() ) {
            ResourceLocation loc = DimensionType.getKey( type );
            if( read.equals( loc ) ) {
                return type;
            }
        }
        reader.setCursor( cursor );
        throw new SimpleCommandExceptionType( new LiteralMessage( "No such dimension" ) ).createWithContext( reader );
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions( CommandContext<S> context, SuggestionsBuilder builder ) {
        for( DimensionType type : DimensionType.getAll() ) {
            builder.suggest( DimensionType.getKey( type ) + "" );
        }
        return builder.buildFuture();
    }
}
