/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.api.util;

import net.minecraft.util.math.MathHelper;
import net.rgsw.MathUtil;

public class ColorUtil {
    public static int rgb( int r, int g, int b ) {
        return r << 16 | g << 8 | b;
    }

    public static int rgb( double r, double g, double b ) {
        return rgb( (int) ( r * 255 ), (int) ( g * 255 ), (int) ( b * 255 ) );
    }

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
                    r = v; g = t; b = p; break;
                case 1:
                    r = q; g = v; b = p; break;
                case 2:
                    r = p; g = v; b = t; break;
                case 3:
                    r = p; g = q; b = v; break;
                case 4:
                    r = t; g = p; b = v; break;
                case 5:
                    r = v; g = p; b = q; break;
            }
        }
        return rgb( r, g, b );
    }

    public static int darken( int col, double amount ) {
        if( amount < 0 ) return lighten( col, - amount );
        int r = col >>> 16 & 0xff;
        int g = col >>> 8 & 0xff;
        int b = col & 0xff;

        r = (int) ( r * ( 1 - amount ) );
        g = (int) ( g * ( 1 - amount ) );
        b = (int) ( b * ( 1 - amount ) );

        return rgb( r, g, b );
    }

    public static int lighten( int col, double amount ) {
        if( amount < 0 ) return darken( col, - amount );
        int r = col >>> 16 & 0xff;
        int g = col >>> 8 & 0xff;
        int b = col & 0xff;

        r = 255 - r;
        g = 255 - g;
        b = 255 - b;

        r = (int) ( r * ( 1 - amount ) );
        g = (int) ( g * ( 1 - amount ) );
        b = (int) ( b * ( 1 - amount ) );

        return rgb( 255 - r, 255 - g, 255 - b );
    }

    public static double red( int col ) {
        return ( col >>> 16 & 0xFF ) / 255D;
    }

    public static double green( int col ) {
        return ( col >>> 8 & 0xFF ) / 255D;
    }

    public static double blue( int col ) {
        return ( col & 0xFF ) / 255D;
    }

    public static double alpha( int col ) {
        return ( col >>> 24 & 0xFF ) / 255D;
    }

    public static double grayscale( int col ) {
        return ( red( col ) + green( col ) + blue( col ) ) / 3;
    }

    public static int fromGrayscale( double grayscale ) {
        return rgb( grayscale, grayscale, grayscale );
    }

    public static int temperature( double temp ) {
        if( temp < 0 ) {
            return rgb( 1 - temp, 1 - temp / 2, 1 );
        } else {
            return rgb( 1, 1 - temp / 2, 1 - temp );
        }
    }

    public static int interpolate( int colorA, int colorB, double v ) {
        int rA = colorA >>> 16 & 0xff;
        int gA = colorA >>> 8 & 0xff;
        int bA = colorA & 0xff;

        int rB = colorB >>> 16 & 0xff;
        int gB = colorB >>> 8 & 0xff;
        int bB = colorB & 0xff;

        int rC = (int) MathUtil.lerp( rA, rB, v );
        int gC = (int) MathUtil.lerp( gA, gB, v );
        int bC = (int) MathUtil.lerp( bA, bB, v );

        return rgb( rC, gC, bC );
    }

    public static int inverse( int col ) {
        return 0xffffff - col;
    }

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
