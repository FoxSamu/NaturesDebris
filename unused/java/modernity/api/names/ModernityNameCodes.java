/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 24 - 2020
 * Author: rgsw
 */

package modernity.generic.names;

public final class ModernityNameCodes {
    public static final int INVALID = - 1;

    private static final String[] ASCII_NAMES = {
        "a", "aa", "e", "i", "ii", "o", "u", "uu", "ue", "oo", "y",
        "b", "d", "f", "fh", "ch", "g", "h", "j", "k", "l", "lh",
        "m", "n", "p", "r", "s", "sj", "sh", "th", "t", "v", "z", "sz",
        ".", "~", ":", "'"
    };

    private static final String[] UNICODE_NAMES = {
        "â", "a", "e", "î", "i", "ô", "û", "u", "ü", "o", "y",
        "b", "d", "f", "ƒ", "ĝ", "g", "h", "j", "k", "l", "q", "m",
        "n", "p", "r", "s", "x", "ŝ", "þ", "t", "v", "z", "c",
        ".", "~", ":", "'"
    };

    public static final int CAPTIAL = 38;

    // Vowels
    public static final int a = 0;
    public static final int aa = 1;
    public static final int e = 2;
    public static final int i = 3;
    public static final int ii = 4;
    public static final int o = 5;
    public static final int u = 6;
    public static final int uu = 7;
    public static final int ue = 8;
    public static final int oo = 9;
    public static final int y = 10;

    // Consonants
    public static final int b = 11;
    public static final int d = 12;
    public static final int f = 13;
    public static final int fh = 14;
    public static final int ch = 15;
    public static final int g = 16;
    public static final int h = 17;
    public static final int j = 18;
    public static final int k = 19;
    public static final int l = 20;
    public static final int lh = 21;
    public static final int m = 22;
    public static final int n = 23;
    public static final int p = 24;
    public static final int r = 25;
    public static final int s = 26;
    public static final int sj = 27;
    public static final int sh = 28;
    public static final int th = 29;
    public static final int t = 30;
    public static final int v = 31;
    public static final int z = 32;
    public static final int sz = 33;

    // Accents
    public static final int sus = 34;
    public static final int lng = 35;
    public static final int hld = 36;
    public static final int sft = 37;

    // Utils
    public static final int START = 38;
    public static final int NONE = 38;


    public static int combination( char a, char b ) {
        if( a == 'a' && b == 'a' ) return aa;
        if( a == 'i' && b == 'i' ) return ii;
        if( a == 'u' && b == 'u' ) return uu;
        if( a == 'u' && b == 'e' ) return ue;
        if( a == 'o' && b == 'o' ) return oo;
        if( a == 'f' && b == 'h' ) return fh;
        if( a == 'c' && b == 'h' ) return ch;
        if( a == 'l' && b == 'h' ) return lh;
        if( a == 's' && b == 'j' ) return sj;
        if( a == 's' && b == 'h' ) return sh;
        if( a == 't' && b == 'h' ) return th;
        if( a == 's' && b == 'z' ) return sz;
        return INVALID;
    }

    public static int single( char c ) {
        if( c == 'a' ) return a;
        if( c == 'e' ) return e;
        if( c == 'i' ) return i;
        if( c == 'o' ) return o;
        if( c == 'u' ) return u;
        if( c == 'y' ) return y;

        if( c == 'b' ) return b;
        if( c == 'd' ) return d;
        if( c == 'f' ) return f;
        if( c == 'g' ) return g;
        if( c == 'h' ) return h;
        if( c == 'j' ) return j;
        if( c == 'k' ) return k;
        if( c == 'l' ) return l;
        if( c == 'm' ) return m;
        if( c == 'n' ) return n;
        if( c == 'p' ) return p;
        if( c == 'r' ) return r;
        if( c == 's' ) return s;
        if( c == 't' ) return t;
        if( c == 'v' ) return v;
        if( c == 'z' ) return z;

        if( c == '.' ) return sus;
        if( c == '~' ) return lng;
        if( c == ':' ) return hld;
        if( c == '\'' ) return sft;

        return INVALID;
    }

    public static String getAsciiCode( int code ) {
        if( code > 37 ) {
            String lc = ASCII_NAMES[ code - CAPTIAL ];
            return Character.toUpperCase( lc.charAt( 0 ) ) + lc.substring( 1 );
        }
        return ASCII_NAMES[ code ];
    }

    public static String getUnicodeCode( int code ) {
        if( code > 37 ) {
            return UNICODE_NAMES[ code - CAPTIAL ].toUpperCase();
        }
        return UNICODE_NAMES[ code ];
    }

    public static int lowerCase( int code ) {
        return code > 37 ? code - 38 : code;
    }

    public static int upperCase( int code ) {
        return code <= 37 ? code + 38 : code;
    }

    public static boolean vowel( int code ) {
        code = lowerCase( code );
        return code < 11;
    }

    public static boolean consonant( int code ) {
        code = lowerCase( code );
        return code >= 11 && code < 34;
    }

    public static boolean accent( int code ) {
        code = lowerCase( code );
        return code >= 34 && code < 38;
    }


    private ModernityNameCodes() {
    }
}
