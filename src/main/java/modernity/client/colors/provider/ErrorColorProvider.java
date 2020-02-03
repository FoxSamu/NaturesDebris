/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors.provider;

public class ErrorColorProvider extends Checkerboard2DColorProvider {
    public static final ErrorColorProvider INSTANCE = new ErrorColorProvider();

    private ErrorColorProvider() {
        super( new SolidColorProvider( 0xff00ff ), new SolidColorProvider( 0x000000 ), 1, 1 );
    }
}
