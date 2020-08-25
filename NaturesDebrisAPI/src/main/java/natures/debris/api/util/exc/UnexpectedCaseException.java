/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.api.util.exc;

public class UnexpectedCaseException extends RuntimeException {
    public UnexpectedCaseException() {
    }

    public UnexpectedCaseException(String message) {
        super(message);
    }

    public UnexpectedCaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedCaseException(Throwable cause) {
        super(cause);
    }

    public UnexpectedCaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
