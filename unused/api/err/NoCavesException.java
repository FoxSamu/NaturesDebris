/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 16 - 2019
 */

package modernity.api.err;

public class NoCavesException extends RuntimeException {
    public NoCavesException() {
    }

    public NoCavesException( String message ) {
        super( message );
    }

    public NoCavesException( String message, Throwable cause ) {
        super( message, cause );
    }

    public NoCavesException( Throwable cause ) {
        super( cause );
    }
}
