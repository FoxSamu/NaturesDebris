/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modul.modules;

public class ModuleException extends Exception {
    public ModuleException() {
    }

    public ModuleException( String message ) {
        super( message );
    }

    public ModuleException( String message, Throwable cause ) {
        super( message, cause );
    }

    public ModuleException( Throwable cause ) {
        super( cause );
    }

    public ModuleException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
