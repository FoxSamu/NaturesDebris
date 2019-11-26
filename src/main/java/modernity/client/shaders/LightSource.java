/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 26 - 2019
 * Author: rgsw
 */

package modernity.client.shaders;

public class LightSource {
    public static final int LIGHT = 0;
    public static final int SHADOW = 2;

    public final float[] pos = new float[ 3 ];
    public final float[] color = { 0, 0, 0, 1 };
    public float radius;
    public int type;

    public LightSource() {

    }

    public LightSource( float x, float y, float z, float r, float g, float b, float a, float rad, int type ) {
        this.pos[ 0 ] = x;
        this.pos[ 1 ] = y;
        this.pos[ 2 ] = z;
        this.color[ 0 ] = r;
        this.color[ 1 ] = g;
        this.color[ 2 ] = b;
        this.color[ 3 ] = a;
        this.radius = rad;
        this.type = type;
    }
}
