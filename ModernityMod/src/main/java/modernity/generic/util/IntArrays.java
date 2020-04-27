/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.util;

/**
 * Utility class for modifying int arrays
 */
public final class IntArrays {
    private IntArrays() {
    }

    /**
     * Adds a value to each element of the array
     */
    public static int[] add( int[] arr, int value ) {
        for( int i = 0; i < arr.length; i++ ) {
            arr[ i ] += value;
        }
        return arr;
    }

    /**
     * Adds a value to each element of the array
     */
    public static int[] max( int[] arr, int value ) {
        for( int i = 0; i < arr.length; i++ ) {
            arr[ i ] = Math.max( arr[ i ], value );
        }
        return arr;
    }
}
