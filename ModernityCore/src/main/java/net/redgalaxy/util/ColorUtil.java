/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package net.redgalaxy.util;

import net.minecraft.util.math.MathHelper;

/**
 * Utility class for processing {@code int}-formatted colors.
 */
public final class ColorUtil {
    private ColorUtil() {
    }

    /**
     * Creates a packed color from RGB values (0-255)
     */
    public static int rgb( int r, int g, int b ) {
        return r << 16 | g << 8 | b;
    }

    /**
     * Creates a packed color from RGB values (0-1)
     */
    public static int rgb( double r, double g, double b ) {
        return rgb( (int) ( r * 255 ), (int) ( g * 255 ), (int) ( b * 255 ) );
    }

    /**
     * Creates a packed color from RGB values (0-1)
     */
    public static int rgb( float r, float g, float b ) {
        return rgb( (int) ( r * 255 ), (int) ( g * 255 ), (int) ( b * 255 ) );
    }

    /**
     * Creates an {@code int}-color from HSV values
     *
     * @param h Hue (0-360)
     * @param s Saturation (0-1)
     * @param v Value (0-1)
     */
    public static int hsv( double h, double s, double v ) {
        h = h < 0 ? h % 360 + 360 : h % 360;
        double r, g, b;
        if( s == 0 ) r = g = b = v;
        else {
            int hi = MathHelper.fastFloor( h / 60 );
            double f = h / 60 - hi;
            double p = v * ( 1 - s );
            double q = v * ( 1 - s * f );
            double t = v * ( 1 - s * ( 1 - f ) );

            switch( hi ) {
                default:
                case 0:
                    r = v;
                    g = t;
                    b = p;
                    break;
                case 1:
                    r = q;
                    g = v;
                    b = p;
                    break;
                case 2:
                    r = p;
                    g = v;
                    b = t;
                    break;
                case 3:
                    r = p;
                    g = q;
                    b = v;
                    break;
                case 4:
                    r = t;
                    g = p;
                    b = v;
                    break;
                case 5:
                    r = v;
                    g = p;
                    b = q;
                    break;
            }
        }
        return rgb( r, g, b );
    }

    /**
     * Makes a color darker
     */
    public static int darken( int col, double amount ) {
        if( amount < 0 ) return lighten( col, - amount );
        int r = col >>> 16 & 0xff;
        int g = col >>> 8 & 0xff;
        int b = col & 0xff;

        r *= 1 - amount;
        g *= 1 - amount;
        b *= 1 - amount;

        return rgb( r, g, b );
    }

    /**
     * Makes a color lighter
     */
    public static int lighten( int col, double amount ) {
        if( amount < 0 ) return darken( col, - amount );
        int r = col >>> 16 & 0xff;
        int g = col >>> 8 & 0xff;
        int b = col & 0xff;

        r = 255 - r;
        g = 255 - g;
        b = 255 - b;

        r *= 1 - amount;
        g *= 1 - amount;
        b *= 1 - amount;

        return rgb( 255 - r, 255 - g, 255 - b );
    }

    /**
     * Returns the red component of a color
     */
    public static double red( int col ) {
        return ( col >>> 16 & 0xFF ) / 255D;
    }

    /**
     * Returns the red component of a color as float
     */
    public static float redf( int col ) {
        return ( col >>> 16 & 0xFF ) / 255F;
    }

    /**
     * Returns the green component of a color
     */
    public static double green( int col ) {
        return ( col >>> 8 & 0xFF ) / 255D;
    }


    /**
     * Returns the green component of a color as float
     */
    public static float greenf( int col ) {
        return ( col >>> 8 & 0xFF ) / 255F;
    }

    /**
     * Returns the blue component of a color
     */
    public static double blue( int col ) {
        return ( col & 0xFF ) / 255D;
    }

    /**
     * Returns the blue component of a color as float
     */
    public static float bluef( int col ) {
        return ( col & 0xFF ) / 255F;
    }

    /**
     * Returns the alpha component of a color
     */
    public static double alpha( int col ) {
        return ( col >>> 24 & 0xFF ) / 255D;
    }

    /**
     * Returns the alpha component of a color as float
     */
    public static float alphaf( int col ) {
        return ( col >>> 24 & 0xFF ) / 255F;
    }

    /**
     * Returns the average (grayscale) of the three components of a color
     */
    public static double grayscale( int col ) {
        return ( red( col ) + green( col ) + blue( col ) ) / 3;
    }

    /**
     * Creates a gray-tinge
     */
    public static int fromGrayscale( double grayscale ) {
        return rgb( grayscale, grayscale, grayscale );
    }

    /**
     * Creates a color from temperature
     */
    public static int temperature( double temp ) {
        if( temp < 0 ) {
            return rgb( 1 - temp, 1 - temp / 2, 1 );
        } else {
            return rgb( 1, 1 - temp / 2, 1 - temp );
        }
    }

    /**
     * Interpolates between two colors
     */
    public static int interpolate( int colorA, int colorB, double v ) {
        double rA = ( colorA >>> 16 & 0xff ) / 255D;
        double gA = ( colorA >>> 8 & 0xff ) / 255D;
        double bA = ( colorA & 0xff ) / 255D;

        double rB = ( colorB >>> 16 & 0xff ) / 255D;
        double gB = ( colorB >>> 8 & 0xff ) / 255D;
        double bB = ( colorB & 0xff ) / 255D;

        double rC = MathUtil.lerp( rA, rB, v );
        double gC = MathUtil.lerp( gA, gB, v );
        double bC = MathUtil.lerp( bA, bB, v );

        return rgb( rC, gC, bC );
    }

    /**
     * Inverts a color
     */
    public static int inverse( int col ) {
        return 0xffffff - col;
    }

    /**
     * Parses a color from a hex string ('#xxxxxx' or '#xxx'), returns null on parse errors
     */
    public static Integer parseHexString( String string ) {
        if( string.charAt( 0 ) != '#' ) {
            return null;
        }
        if( string.length() != 7 && string.length() != 4 ) {
            return null;
        }
        String hex = string.substring( 1 );

        if( hex.length() == 6 ) {
            try {
                return Integer.parseInt( hex, 16 );
            } catch( NumberFormatException exc ) {
                return null;
            }
        } else {
            try {
                int r = Integer.parseInt( hex.charAt( 0 ) + "", 16 );
                int g = Integer.parseInt( hex.charAt( 1 ) + "", 16 );
                int b = Integer.parseInt( hex.charAt( 2 ) + "", 16 );

                return r << 20 | r << 16 | g << 12 | g << 8 | b << 4 | b;
            } catch( NumberFormatException exc ) {
                return null;
            }
        }
    }
}
