/*
 * Copyright (c) 2019 RGSW
 * All rights reserved. Do not distribute.
 * This file is part of the Modernity, and is licensed under the terms and conditions of RedGalaxy.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package net.rgsw.exc;

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
