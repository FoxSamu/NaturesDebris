/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modul;

public class ModulException extends Exception {
    public ModulException() {
    }

    public ModulException( String message ) {
        super( message );
    }

    public ModulException( String message, Throwable cause ) {
        super( message, cause );
    }

    public ModulException( Throwable cause ) {
        super( cause );
    }

    public ModulException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
