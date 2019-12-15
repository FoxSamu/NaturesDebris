/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 15 - 2019
 * Author: rgsw
 */

package net.redgalaxy.exc;

public class UnexpectedCaseException extends RuntimeException {
    public UnexpectedCaseException() {
    }

    public UnexpectedCaseException( String message ) {
        super( message );
    }

    public UnexpectedCaseException( String message, Throwable cause ) {
        super( message, cause );
    }

    public UnexpectedCaseException( Throwable cause ) {
        super( cause );
    }

    public UnexpectedCaseException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
