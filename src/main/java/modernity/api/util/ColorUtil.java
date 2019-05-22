package modernity.api.util;

public class ColorUtil {
    public static int rgb( int r, int g, int b ) {
        return r << 16 | g << 8 | b;
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

    public static int inverse( int col ) {
        return 0xffffff - col;
    }
}
