/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 26 - 2019
 * Author: rgsw
 */

package modul.module;

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
