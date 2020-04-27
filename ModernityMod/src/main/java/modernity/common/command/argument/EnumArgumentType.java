/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@SuppressWarnings( "unchecked" )
public class EnumArgumentType<T extends Enum<T> & IStringSerializable> implements ArgumentType<T> {

    private static final DynamicCommandExceptionType ENUM_TYPE_INCORRECT = new DynamicCommandExceptionType(
        literal -> new TranslationTextComponent( "command.modernity.argument.enum_incorrect", literal )
    );

    private final Object[] values;
    private final String[] names;

    private EnumArgumentType( Class<T> cls ) {
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

    private EnumArgumentType( Class<T> cls, T... allowed ) {
        if( cls == null ) {
            throw new NullPointerException( "Specified class is null" );
        }
        if( ! cls.isEnum() ) {
            throw new IllegalArgumentException( "Specified class is not an enum" );
        }

        values = allowed;
        names = new String[ values.length ];

        for( int i = 0; i < values.length; i++ ) {
            names[ i ] = ( (IStringSerializable) values[ i ] ).getName();
        }
    }

    private EnumArgumentType( Class<T> cls, Predicate<T> allowed ) {
        if( cls == null ) {
            throw new NullPointerException( "Specified class is null" );
        }
        if( ! cls.isEnum() ) {
            throw new IllegalArgumentException( "Specified class is not an enum" );
        }
        ArrayList<T> list = new ArrayList<>();

        for( T t : cls.getEnumConstants() ) {
            if( allowed.test( t ) ) {
                list.add( t );
            }
        }

        values = list.toArray();
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

    public static <T extends Enum<T> & IStringSerializable> EnumArgumentType<T> enumerator( Class<T> cls ) {
        return new EnumArgumentType<>( cls );
    }

    public static <T extends Enum<T> & IStringSerializable> EnumArgumentType<T> enumerator( Class<T> cls, T... allowed ) {
        return new EnumArgumentType<>( cls, allowed );
    }

    public static <T extends Enum<T> & IStringSerializable> EnumArgumentType<T> enumerator( Class<T> cls, Predicate<T> allowed ) {
        return new EnumArgumentType<>( cls, allowed );
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
