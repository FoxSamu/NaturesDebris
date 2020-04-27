/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.IProperty;
import net.minecraft.tags.Tag;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public final class MDBlockPredicates {
    /**
     * Matches any of the rock types (rock, darkrock, lightrock and redrock)
     */
    public static final Predicate<BlockState> ROCK_TYPES = blocks(
        MDNatureBlocks.ROCK,
        MDNatureBlocks.DARKROCK
    );

    public static final Predicate<BlockState> ROCKS_OR_LIMESTONE = ROCK_TYPES.or( block( MDNatureBlocks.LIMESTONE ) );

    public static final Predicate<BlockState> ROCK_ONLY = block( MDNatureBlocks.ROCK );

    public static final Predicate<BlockState> SUMESTONE = block( MDNatureBlocks.SUMESTONE );

    /**
     * Matches everything
     */
    public static final Predicate<BlockState> TRUE = state -> true;

    /**
     * Matches nothing
     */
    public static final Predicate<BlockState> FALSE = state -> false;

    public static Predicate<BlockState> block( Block block ) {
        return state -> state.getBlock() == block;
    }

    public static Predicate<BlockState> blocks( Block... blocks ) {
        List<Block> list = Arrays.asList( blocks );
        return state -> list.contains( state.getBlock() );
    }

    public static Predicate<BlockState> blockState( BlockState s ) {
        return state -> state == s;
    }

    public static Predicate<BlockState> blockStates( BlockState... states ) {
        List<BlockState> list = Arrays.asList( states );
        return list::contains;
    }

    public static Predicate<BlockState> fluid( Fluid fluid ) {
        return state -> state.getFluidState().getFluid() == fluid;
    }

    public static Predicate<BlockState> fluids( Fluid... states ) {
        List<Fluid> list = Arrays.asList( states );
        return state -> list.contains( state.getFluidState().getFluid() );
    }

    public static Predicate<BlockState> fluidState( IFluidState s ) {
        return state -> state.getFluidState() == s;
    }

    public static Predicate<BlockState> fluidStates( IFluidState... states ) {
        List<IFluidState> list = Arrays.asList( states );
        return state -> list.contains( state.getFluidState() );
    }

    public static Predicate<BlockState> withProperty( IProperty<?> property ) {
        return state -> state.has( property );
    }

    public static <T extends Comparable<T>> Predicate<BlockState> withProperty( IProperty<T> property, T value ) {
        return state -> state.has( property ) && state.get( property ).equals( value );
    }

    @SafeVarargs
    public static <T extends Comparable<T>> Predicate<BlockState> withProperty( IProperty<T> property, T... values ) {
        List<T> list = Arrays.asList( values );
        return state -> state.has( property ) && list.contains( state.get( property ) );
    }

    public static <T extends Comparable<T>> Predicate<BlockState> withProperty( IProperty<T> property, Predicate<T> values ) {
        return state -> state.has( property ) && values.test( state.get( property ) );
    }

    public static Predicate<BlockState> tag( Tag<Block> tag ) {
        return state -> state.isIn( tag );
    }

    @SafeVarargs
    public static Predicate<BlockState> tags( Tag<Block>... tags ) {
        return state -> {
            for( Tag<Block> tag : tags ) {
                if( state.isIn( tag ) ) return true;
            }
            return false;
        };
    }

    public static Predicate<BlockState> fluidTag( Tag<Fluid> tag ) {
        return state -> state.getFluidState().isTagged( tag );
    }

    @SafeVarargs
    public static Predicate<BlockState> fluidTags( Tag<Fluid>... tags ) {
        return state -> {
            IFluidState s = state.getFluidState();
            for( Tag<Fluid> tag : tags ) {
                if( s.isTagged( tag ) ) return true;
            }
            return false;
        };
    }


    private MDBlockPredicates() {
    }
}
