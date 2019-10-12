package modernity.api.util;

public final class IntArrays {
    private IntArrays() {
    }

    public static int[] add( int[] arr, int value ) {
        for( int i = 0; i < arr.length; i++ ) {
            arr[ i ] += value;
        }
        return arr;
    }
}
