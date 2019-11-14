/*
 * Copyright (c) 2019 RGSW
 * All rights reserved. Do not distribute.
 * This file is part of the Modernity, and is licensed under the terms and conditions of RedGalaxy.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package net.rgsw.exc;

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
