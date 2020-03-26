/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 24 - 2020
 * Author: rgsw
 */

package modernity.api.names;

public interface IModernityName {
    int length();
    int codeAt( int index );
    IModernityName subname( int start, int end );

    default int[] getCodes() {
        int[] codes = new int[ length() ];
        for( int i = 0; i < codes.length; i++ ) {
            codes[ i ] = codeAt( i );
        }
        return codes;
    }
}
