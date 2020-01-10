/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package runs;

import modernity.api.util.ILongScrambler;

import java.util.Arrays;

public final class ScramblerTest {
    private ScramblerTest() {
    }

    public static void main( String[] args ) {
//        ILongScrambler scrambler = ILongScrambler.lgc( 53269L, 61919923313L );
//        ILongScrambler scrambler = ILongScrambler.hash( 48, 5784L, 0xf3ecd35f035fL, 0x5f7L, 0xc026e435e4c53dfL, 0xfdee );
//        ILongScrambler scrambler = ILongScrambler.hash( 48, 0xB342FACE35L, 0xf3ecd35f035dL, 0x5DEE315DACE66DL );
//        ILongScrambler scrambler = ILongScrambler.hash( 48, 61919923313L, 72841242277L, 534129033257L );
//        ILongScrambler scrambler = ILongScrambler.xorshift( 0xFACE, 0xEC026E435E4C53DFL );
        ILongScrambler scrambler = seed -> seed * 1277 + 73 & 0xFFFF;

        long seed = System.nanoTime() & 0xFFFF;
        int[] counter = new int[2];
        long nanoTime = System.nanoTime();
        for( int i = 0; i < 25; i++ ) {
            for( int j = 0; j < 40; j ++ ) {
                int n = (int) Math.abs( ( seed = scrambler.scramble( seed ) ) % 30 );
                int k = n < 15 ? 0 : 1;
                counter[ k ] ++;
                System.out.printf( "%02d ", n );
            }
            System.out.println();
        }
        long nanoTime2 = System.nanoTime();

        System.out.println( nanoTime2 - nanoTime + " nanos" );

        System.out.println( Arrays.toString( counter ) );
    }
}
