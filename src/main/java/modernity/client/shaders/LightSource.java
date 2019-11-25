/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 25 - 2019
 * Author: rgsw
 */

package modernity.client.shaders;

public class LightSource {
    public static final int LIGHT = 0;
    public static final int SHADOW = 2;

    public final float[] pos = new float[ 3 ];
    public final float[] color = new float[ 4 ];
    public float radius;
    public int type;
}
