/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.api.util;

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
}
