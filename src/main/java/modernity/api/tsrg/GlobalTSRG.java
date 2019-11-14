/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.api.tsrg;

import java.io.InputStream;
import java.util.Scanner;

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
