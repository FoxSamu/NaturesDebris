/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.settings.core.setting;

import modernity.common.settings.core.CycleableSetting;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;

public class BooleanSetting extends CycleableSetting<Boolean> {
    private final boolean def;

    public BooleanSetting( String key, boolean def ) {
        super( key );
        this.def = def;
    }

    public BooleanSetting( String key ) {
        this( key, false );
    }

    @Override
    public void cycle( boolean reverse ) {
        set( ! get() );
    }

    @Override
    public Boolean getDefault() {
        return def;
    }

    @Override
    public String serialize() {
        return def ? "true" : "false";
    }

    @Override
    public void deserialize( String string ) {
        string = string.toLowerCase();
        set( string.equals( "t" ) || string.equals( "true" ) || string.equals( "1" ) );
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public String formatValue() {
        return I18n.format( "settings.modernity." + get() );
    }

    @Override
    public void getSuggestions( String input, Consumer<String> output ) {
        input = input.toLowerCase();
        if( input.isEmpty() || "true".startsWith( input ) ) output.accept( "true" );
        if( input.isEmpty() || "false".startsWith( input ) ) output.accept( "false" );
    }

    @Override
    public boolean accepts( String string ) {
        string = string.toLowerCase();
        return string.equals( "t" ) || string.equals( "true" ) || string.equals( "1" ) ||
                string.equals( "f" ) || string.equals( "false" ) || string.equals( "0" );
    }
}
