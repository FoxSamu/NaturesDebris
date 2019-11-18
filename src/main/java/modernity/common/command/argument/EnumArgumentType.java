/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 18 - 2019
 * Author: rgsw
 */

package modernity.common.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings( "unchecked" )
public class EnumArgumentType<T extends Enum & IStringSerializable> implements ArgumentType<T> {

    private static final DynamicCommandExceptionType ENUM_TYPE_INCORRECT = new DynamicCommandExceptionType(
        literal -> new TranslationTextComponent( "command.modernity.argument.enum_incorrect", literal )
    );

    private final Object[] values;
    private final String[] names;

    public EnumArgumentType( Class<T> cls ) {
        if( cls == null ) {
            throw new NullPointerException( "Specified class is null" );
        }
        if( ! cls.isEnum() ) {
            throw new IllegalArgumentException( "Specified class is not an enum" );
        }

        values = cls.getEnumConstants();
        names = new String[ values.length ];

        for( int i = 0; i < values.length; i++ ) {
            names[ i ] = ( (IStringSerializable) values[ i ] ).getName();
        }
    }

    private EnumArgumentType( String[] valueNames ) {
        values = new Object[ valueNames.length ];
        names = valueNames;
    }

    @Override
    public T parse( StringReader reader ) throws CommandSyntaxException {
        int start = reader.getCursor();
        for( int i = 0; i < values.length; i++ ) {
            String str = names[ i ];
            if( reader.canRead( str.length() ) ) {
                int end = start + str.length();
                if( reader.getString().substring( start, end ).equals( str ) ) {
                    reader.setCursor( end );
                    if( ! reader.canRead() || reader.peek() == ' ' ) {
                        return (T) values[ i ];
                    } else {
                        reader.setCursor( start );
                    }
                }
            }
        }
        throw ENUM_TYPE_INCORRECT.createWithContext( reader, Arrays.toString( names ) );
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions( CommandContext<S> context, SuggestionsBuilder builder ) {
        return ISuggestionProvider.suggest( names, builder );
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.asList( names );
    }

    public static <T extends Enum & IStringSerializable> EnumArgumentType<T> enumerator( Class<T> cls ) {
        return new EnumArgumentType<>( cls );
    }

    public static class Serializer implements IArgumentSerializer<EnumArgumentType<?>> {

        @Override
        public void write( EnumArgumentType<?> argument, PacketBuffer buffer ) {
            buffer.writeShort( argument.names.length );
            for( String str : argument.names ) {
                buffer.writeString( str );
            }
        }

        @Override
        public EnumArgumentType<?> read( PacketBuffer buffer ) {
            int len = buffer.readShort();
            String[] names = new String[ len ];

            for( int i = 0; i < len; i++ ) {
                names[ i ] = buffer.readString();
            }

            return new EnumArgumentType<>( names );
        }

        @Override
        public void write( EnumArgumentType<?> p_212244_1_, JsonObject p_212244_2_ ) {

        }
    }

    private enum Dummy implements IStringSerializable {
        ;

        @Override
        public String getName() {
            return "dummy";
        }
    }
}
