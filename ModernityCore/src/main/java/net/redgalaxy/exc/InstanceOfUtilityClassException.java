/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
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
