/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 15 - 2019
 * Author: rgsw
 */

package net.redgalaxy.exc;

public class InstanceOfUtilityClassException extends RuntimeException {
    private final String msg;

    public InstanceOfUtilityClassException() {
        msg = String.format( "Instance of utility class %s", getStackTrace()[ 0 ].getClassName() );
    }

    public InstanceOfUtilityClassException( String message ) {
        super( message );
        msg = message;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
