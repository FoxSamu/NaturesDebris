/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import modernity.common.settings.core.AbstractSetting;
import modernity.common.util.ProxyCommon;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SettingArgumentType implements ArgumentType<String> {

    private final AbstractSetting<?> setting;

    public SettingArgumentType( AbstractSetting<?> setting ) {
        this.setting = setting;
    }

    @Override
    public String parse( StringReader reader ) throws CommandSyntaxException {
        String input = reader.getRemaining();
        if( ! setting.accepts( input ) )
            throw new SimpleCommandExceptionType( new LiteralMessage( "Unaccepted value!" ) ).createWithContext( reader );
        return input;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions( CommandContext<S> context, SuggestionsBuilder builder ) {
        StringReader reader = new StringReader( context.getInput() );
        reader.setCursor( builder.getStart() );
        String input = reader.getRemaining();
        List<String> suggs = new ArrayList<>();
        setting.getSuggestions( input, d -> {
            if( ! suggs.contains( d ) ) suggs.add( d );
        } );
        for( String str : suggs ) {
            builder.suggest( str );
        }

        return builder.buildFuture();
    }

    public static class Serializer implements IArgumentSerializer<SettingArgumentType> {
        @Override
        public void write( SettingArgumentType settingArgumentType, PacketBuffer packetBuffer ) {
            packetBuffer.writeString( settingArgumentType.setting.getKey() );
        }

        @Override
        public SettingArgumentType read( PacketBuffer packetBuffer ) {
            return new SettingArgumentType( ProxyCommon.serverSettings().getSetting( packetBuffer.readString( 256 ) ) );
        }

        @Override
        public void write( SettingArgumentType settingArgumentType, JsonObject jsonObject ) {
            jsonObject.addProperty( "setting", settingArgumentType.setting.getKey() );
        }
    }
}
