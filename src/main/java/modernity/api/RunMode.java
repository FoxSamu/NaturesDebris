/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.api;

public enum RunMode {
    CLIENT( "modernity.client.ModernityClient" ),
    SERVER( "modernity.server.ModernityServer" ),
    DATA( "modernity.data.ModernityData" );

    private final String className;

    RunMode( String className ) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
