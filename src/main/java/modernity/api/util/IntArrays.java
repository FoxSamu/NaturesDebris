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
