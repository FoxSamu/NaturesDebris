/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
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
