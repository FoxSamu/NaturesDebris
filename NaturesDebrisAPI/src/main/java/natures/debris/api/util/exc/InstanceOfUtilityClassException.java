/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.api.util.exc;

public class InstanceOfUtilityClassException extends RuntimeException {
    private final String msg;

    public InstanceOfUtilityClassException() {
        msg = String.format("No instance of %s for you!", getStackTrace()[0].getClassName());
    }

    public InstanceOfUtilityClassException(String message) {
        msg = message;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
