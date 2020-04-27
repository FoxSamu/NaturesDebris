/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.tsrg;

import java.io.InputStream;
import java.util.Scanner;

// TODO Do we use this in the code?

/**
 * Stores the SRG-MCP mappings available in the META-INF folder.
 *
 * @author RGSW
 */
public final class GlobalTSRG {
    private static final TSRGMapping MAPPING;

    static {
        InputStream stream = GlobalTSRG.class.getClassLoader().getResourceAsStream( "META-INF/srg_to_snapshot_20190719-1.14.3.tsrg" );
        if( stream == null ) throw new NullPointerException( "Null mappings! Where is the mapping resource?" );
        Scanner scanner = new Scanner( stream );
        MAPPING = TSRGMapping.create( scanner );
    }

    private GlobalTSRG() {
    }

    public static void load() {

    }

    public static TSRGMapping.ClassMapping get( String name ) {
        return MAPPING.get( name );
    }
}
