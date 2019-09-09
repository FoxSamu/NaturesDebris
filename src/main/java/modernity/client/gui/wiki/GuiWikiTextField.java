/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuiWikiTextField <T> extends GuiTextField {
    private Format<T> format;
    private T value;
    private String lastInput;
    private boolean valid;

    public GuiWikiTextField( int componentId, FontRenderer font, int x, int y, int width, int height, Format<T> format ) {
        super( componentId, font, x, y, width, height );
        this.format = format;
        validate();
    }

    public GuiWikiTextField( int componentId, FontRenderer font, int x, int y, int width, int height, @Nullable GuiTextField copyFrom, Format<T> format ) {
        super( componentId, font, x, y, width, height, copyFrom );
        this.format = format;
        validate();
    }

    public GuiWikiTextField( int componentId, FontRenderer font, int x, int y, int width, int height, String initialValue, Format<T> format ) {
        super( componentId, font, x, y, width, height );
        setText( initialValue );
        this.format = format;
        validate();
    }

    public void drawTextField( int mouseX, int mouseY, float partialTicks ) {
        GlStateManager.color4f( 1, 1, 1, 1 );
        GlStateManager.disableLighting();
        super.drawTextField( mouseX, mouseY, partialTicks );
        boolean enableBG = getEnableBackgroundDrawing();
        if( enableBG && getVisible() ) {
            int color = valid ? 0xffffffff : 0xffff0000;
            drawRect( x - 1, y - 1, x, y + height + 1, color );
            drawRect( x - 1, y - 1, x + width + 1, y, color );
            drawRect( x + width, y - 1, x + width + 1, y + height + 1, color );
            drawRect( x - 1, y + height, x + width + 1, y + height + 1, color );
        }
    }

    public void setFormat( Format<T> format ) {
        this.format = format;
        validate();
    }

    @Override
    public void tick() {
        super.tick();
        if( ! getText().equals( lastInput ) ) {
            lastInput = getText();
            validate();
        }
    }

    public void validate() {
        T value = format.parse( getText() );
        valid = value != null;
        if( valid ) {
            this.value = value;
        }
    }

    public T getValue() {
        return value;
    }

    public boolean isValid() {
        return valid;
    }

    public static Format<Integer> integer( int min, int max ) {
        return new IntegerFormat( min, max );
    }

    public static Format<Item> item() {
        return new ItemFormat();
    }

    public static Format<Fluid> fluid() {
        return new FluidFormat();
    }

    public static Format<IBlockState> state() {
        return new StateFormat();
    }

    public static Format<ResourceLocation> resLoc() {
        return new ResLocFormat();
    }

    public static Format<String> any() {
        return i -> i;
    }

    public interface Format <T> {
        T parse( String input );
    }

    private static class IntegerFormat implements Format<Integer> {
        private final int min;
        private final int max;

        private IntegerFormat( int min, int max ) {
            this.min = min;
            this.max = max;
        }

        @Override
        public Integer parse( String input ) {
            try {
                int i = Integer.parseInt( input );
                return i >= min && i <= max ? i : null;
            } catch( NumberFormatException exc ) {
                return null;
            }
        }
    }

    private static class ItemFormat implements Format<Item> {

        @Override
        public Item parse( String input ) {
            ResourceLocation loc = ResourceLocation.tryCreate( input );
            if( loc == null ) return null;
            return ForgeRegistries.ITEMS.getValue( loc );
        }
    }

    private static class FluidFormat implements Format<Fluid> {

        @Override
        public Fluid parse( String input ) {
            ResourceLocation loc = ResourceLocation.tryCreate( input );
            if( loc == null ) return null;
            return IRegistry.FLUID.get( loc );
        }
    }

    private static class StateFormat implements Format<IBlockState> {
        private static final Pattern BLOCK_PATTERN = Pattern.compile( "^((?:[a-zA-Z0-9_$]*?):)?([a-zA-Z0-9._$/]+?)(\\[.*?])?$" );

        @Override
        public IBlockState parse( String input ) {
            Matcher matcher = BLOCK_PATTERN.matcher( input );
            if( matcher.matches() ) {
                ResourceLocation loc = ResourceLocation.tryCreate( matcher.group( 1 ) + matcher.group( 2 ) );
                if( loc == null ) return null;
                String props = matcher.group( 3 );
                Block block = ForgeRegistries.BLOCKS.getValue( loc );
                if( block == null ) return null;
                IBlockState state = block.getDefaultState();
                if( props != null && ! props.isEmpty() ) {
                    if( props.length() == 1 ) return null;
                    if( props.charAt( 0 ) != '[' ) return null;
                    if( props.charAt( props.length() - 1 ) != ']' ) return null;

                    StateContainer<Block, IBlockState> container = block.getStateContainer();

                    String[] split = props.substring( 1, props.length() - 1 ).split( "," );
                    for( String prop : split ) {
                        int idx = prop.indexOf( '=' );
                        if( idx < 0 ) return null;
                        String key = prop.substring( 0, idx );
                        String value = prop.substring( idx + 1 );

                        IProperty property = container.getProperty( key );
                        if( property == null ) return null;

                        Optional optional = property.parseValue( value );
                        if( ! optional.isPresent() ) return null;

                        state = apply( state, property, (Comparable) optional.get() );
                    }
                }
                return state;
            }
            return null;
        }

        private <T extends Comparable<T>> IBlockState apply( IBlockState current, IProperty<T> prop, T val ) {
            return current.with( prop, val );
        }
    }

    private static class ResLocFormat implements Format<ResourceLocation> {

        @Override
        public ResourceLocation parse( String input ) {
            return ResourceLocation.tryCreate( input );
        }
    }
}
