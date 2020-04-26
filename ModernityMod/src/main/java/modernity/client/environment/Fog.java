/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.client.environment;

import net.redgalaxy.util.ColorUtil;

/**
 * Describes the appearance of fog.
 */
public class Fog {
    /** Defines the way fog is rendered. */
    public Type type = Type.VANILLA;

    /** The fog density. Only applies when {@link #type} is {@link Type#EXP} or {@link Type#EXP2} */
    public float density;

    /** The fog start. Only applies when {@link #type} is {@link Type#LINEAR} */
    public float start;

    /** The fog start. Only applies when {@link #type} is {@link Type#LINEAR} */
    public float end;

    /** The fog color. Only applies when {@link #type} is {@link Type#LINEAR}, {@link Type#EXP} or {@link Type#EXP2} */
    public final float[] color = { 0, 0, 0 };

    /**
     * Sets the fog color to double-precision rgb values (0-1).
     */
    public void setColor( double r, double g, double b ) {
        color[ 0 ] = (float) r;
        color[ 1 ] = (float) g;
        color[ 2 ] = (float) b;
    }

    /**
     * Sets the fog color to single-precision rgb values (0-1).
     */
    public void setColor( float r, float g, float b ) {
        color[ 0 ] = r;
        color[ 1 ] = g;
        color[ 2 ] = b;
    }

    /**
     * Sets the fog color to an integer RGB color.
     */
    public void setColor( int rgb ) {
        color[ 0 ] = ColorUtil.redf( rgb );
        color[ 1 ] = ColorUtil.greenf( rgb );
        color[ 2 ] = ColorUtil.bluef( rgb );
    }

    /**
     * The type of fog being rendered.
     */
    public enum Type {
        /**
         * Linear fog interpolation.
         * Fog alpha is interpolated from 0 to 1 between the specified {@link #start} and {@link #end}.
         */
        LINEAR,

        /**
         * Exponential fog interpolation.
         * Fog alpha is interpolated exponentially using {@link #density}
         */
        EXP,

        /**
         * Squared exponential fog interpolation.
         * Fog alpha is interpolated squared-exponentially using {@link #density}
         */
        EXP2,

        /**
         * No fog is rendered at all.
         */
        DISABLED,

        /**
         * Fog rendering is passed to vanilla's fog renderer.
         * Density, start and end values are not applicable.
         */
        VANILLA
    }
}
