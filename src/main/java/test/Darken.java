package test;

import modernity.api.util.ColorUtil;

public class Darken {
    public static void main( String[] args ) {
        int col = 0xffffff;
        System.out.printf( "#%06x", ColorUtil.darken( col, 0.2 ) );
    }
}
