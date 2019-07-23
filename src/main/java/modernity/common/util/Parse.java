/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.common.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {
    public static boolean validateInt( String input, int min, int max ) {
        try {
            int i = Integer.parseInt( input );
            return i >= min && i <= max;
        } catch( NumberFormatException exc ) {
            return false;
        }
    }

    public static boolean validateItem( String item ) {
        ResourceLocation loc = ResourceLocation.tryCreate( item );
        if( loc == null ) return false;
        return ForgeRegistries.ITEMS.containsKey( loc );
    }

    private static final Pattern BLOCK_PATTERN = Pattern.compile( "^((?:[a-zA-Z0-9_$]*?):)?([a-zA-Z0-9._$/]+?)(\\[.*?])?$" );

    public static boolean validateBlockState( String input ) {
        Matcher matcher = BLOCK_PATTERN.matcher( input );
        if( matcher.matches() ) {
            ResourceLocation loc = ResourceLocation.tryCreate( matcher.group( 1 ) + matcher.group( 2 ) );
            if( loc == null ) return false;
            String props = matcher.group( 3 );
            Block block = ForgeRegistries.BLOCKS.getValue( loc );
            if( block == null ) return false;
            if( ! props.isEmpty() ) {
                if( props.length() == 1 ) return false;
                if( props.charAt( 0 ) != '[' ) return false;
                if( props.charAt( props.length() - 1 ) != ']' ) return false;

                StateContainer<Block, IBlockState> container = block.getStateContainer();

                String[] split = props.substring( 1, props.length() - 1 ).split( "," );
                for( String prop : split ) {
                    int idx = prop.indexOf( '=' );
                    if( idx < 0 ) return false;
                    String key = prop.substring( 0, idx );
                    String value = prop.substring( idx + 1 );

                    IProperty<?> property = container.getProperty( key );
                    if( property == null ) return false;

                    Optional<?> optional = property.parseValue( value );
                    if( ! optional.isPresent() ) return false;
                }
            }
            return true;
        }
        return false;
    }
}
