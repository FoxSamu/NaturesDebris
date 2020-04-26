/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 25 - 2019
 * Author: rgsw
 */

package modernity.client.shaders;

public class ShaderException extends RuntimeException {
    public ShaderException() {
    }

    public ShaderException( String message ) {
        super( message );
    }

    public ShaderException( String message, Throwable cause ) {
        super( message, cause );
    }
}
