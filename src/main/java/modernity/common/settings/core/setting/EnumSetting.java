/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.settings.core.setting;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.api.util.ITranslatable;
import modernity.common.settings.core.CycleableSetting;

import java.util.function.Consumer;

public class EnumSetting <T extends Enum> extends CycleableSetting<T> {
    private final T defaultValue;
    private final Class<? extends T> enumClass;
    private final T[] enumValues;

    public EnumSetting( String key, T defaultValue ) {
        super( key );
        this.defaultValue = defaultValue;
        enumClass = (Class<? extends T>) defaultValue.getClass();
        enumValues = enumClass.getEnumConstants();
    }

    @Override
    public void cycle( boolean reverse ) {
        int ord = get().ordinal() + ( reverse ? - 1 : 1 );
        if( ord < 0 ) ord = enumValues.length - 1;
        if( ord >= enumValues.length ) ord = 0;
        set( enumValues[ ord ] );
    }

    @Override
    public T getDefault() {
        return defaultValue;
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public String formatValue() {
        if( get() instanceof ITranslatable ) {
            return I18n.format( ( (ITranslatable) get() ).getTranslationKey() );
        }
        return super.formatValue();
    }

    @Override
    public String serialize() {
        return get().name().toLowerCase();
    }

    @Override
    public void deserialize( String string ) {
        for( T t : enumValues ) {
            if( t.name().toLowerCase().equals( string ) ) {
                set( t );
                return;
            }
        }
    }

    @Override
    public void getSuggestions( String input, Consumer<String> output ) {
        input = input.toLowerCase();
        for( T t : enumValues ) {
            String str = t.name().toLowerCase();
            if( str.equals( input ) ) {
                output.accept( str );
            }
        }
        for( T t : enumValues ) {
            String str = t.name().toLowerCase();
            if( str.startsWith( input ) ) {
                output.accept( str );
            }
        }
    }

    @Override
    public boolean accepts( String input ) {
        for( T t : enumValues ) {
            if( t.name().toLowerCase().equals( input ) ) {
                return true;
            }
        }
        return false;
    }
}
