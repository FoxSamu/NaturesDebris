/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.util;

public final class CTMUtil {
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 4;
    public static final int LEFT = 8;
    public static final int UPRIGHT = 16;
    public static final int DOWNRIGHT = 32;
    public static final int DOWNLEFT = 64;
    public static final int UPLEFT = 128;

    private static final int[][] ONE_SIDE_VALUES = {
        { 14, 27, 38, 25 },
        { 31, 43, 42, 30 },
        { 29, 41, 40, 28 },
        { 7, 19, 18, 6 }
    };

    private static final int[][] ONE_SIDE_CORNER_MASKS = {
        { 0b0010, 0b1000, 0b0100, 0b0001 },
        { 0b0100, 0b0001, 0b0010, 0b1000 }
    };

    private static final int[][] TWO_SIDES_VALUES = {
        { 15, 39, 37, 13 },
        { 5, 17, 16, 4 }
    };

    private static final int[] TWO_SIDES_CORNER_MASKS = {
        0b0010, 0b1000, 0b0100, 0b0001
    };

    private static final int[] INDEX_LOOKUP = new int[ 256 ];

    static {
        for( int i = 0; i < 256; i++ ) {
            INDEX_LOOKUP[ i ] = computeCTMIndex( i );
        }
    }

    private CTMUtil() {
    }

    public static int getCTMIndex( int connections ) {
        return INDEX_LOOKUP[ connections ];
    }

    private static int computeCTMIndex( int connections ) {
        if( connections == 0 ) return 0;
        if( connections == 255 ) return 26;

        boolean up = ( connections & UP ) != 0;
        boolean right = ( connections & RIGHT ) != 0;
        boolean down = ( connections & DOWN ) != 0;
        boolean left = ( connections & LEFT ) != 0;
        boolean upright = ( connections & UPRIGHT ) != 0;
        boolean downright = ( connections & DOWNRIGHT ) != 0;
        boolean downleft = ( connections & DOWNLEFT ) != 0;
        boolean upleft = ( connections & UPLEFT ) != 0;

        boolean upEdge = ! up;
        boolean rightEdge = ! right;
        boolean downEdge = ! down;
        boolean leftEdge = ! left;

        boolean uprightCorner = up && right && ! upright;
        boolean upleftCorner = up && left && ! upleft;
        boolean downleftCorner = down && left && ! downleft;
        boolean downrightCorner = down && right && ! downright;

        int edgeCount = 0;
        if( upEdge ) edgeCount++;
        if( rightEdge ) edgeCount++;
        if( downEdge ) edgeCount++;
        if( leftEdge ) edgeCount++;

        int corners = 0;
        if( upleftCorner ) corners |= 0b1000;
        if( uprightCorner ) corners |= 0b0100;
        if( downleftCorner ) corners |= 0b0010;
        if( downrightCorner ) corners |= 0b0001;

        if( edgeCount == 0 ) {
            switch( corners ) {
                default:
                case 0b0000: return 26;
                case 0b1011: return 8;
                case 0b1110: return 9;
                case 0b0101: return 10;
                case 0b0011: return 11;
                case 0b0111: return 20;
                case 0b1101: return 21;
                case 0b1100: return 22;
                case 0b1010: return 23;
                case 0b0001: return 32;
                case 0b0010: return 33;
                case 0b1001: return 34;
                case 0b0110: return 35;
                case 0b0100: return 44;
                case 0b1000: return 45;
                case 0b1111: return 46;
            }
        } else if( edgeCount == 1 ) {
            int sideIndex = 0;
            if( rightEdge ) sideIndex = 1;
            if( downEdge ) sideIndex = 2;
            if( leftEdge ) sideIndex = 3;

            boolean leftCorner = ( corners & ONE_SIDE_CORNER_MASKS[ 0 ][ sideIndex ] ) != 0;
            boolean rightCorner = ( corners & ONE_SIDE_CORNER_MASKS[ 1 ][ sideIndex ] ) != 0;

            int lr = 0;
            if( leftCorner ) lr |= 1;
            if( rightCorner ) lr |= 2;

            return ONE_SIDE_VALUES[ lr ][ sideIndex ];
        } else if( edgeCount == 2 ) {
            if( rightEdge && leftEdge ) return 24;
            if( upEdge && downEdge ) return 2;

            int cornerIndex = 0;
            if( rightEdge && downEdge ) cornerIndex = 1;
            if( leftEdge && downEdge ) cornerIndex = 2;
            if( leftEdge && upEdge ) cornerIndex = 3;

            boolean corner = ( corners & TWO_SIDES_CORNER_MASKS[ cornerIndex ] ) != 0;

            return TWO_SIDES_VALUES[ corner ? 1 : 0 ][ cornerIndex ];
        } else if( edgeCount == 3 ) {
            if( ! downEdge ) return 12;
            if( ! rightEdge ) return 1;
            if( ! upEdge ) return 36;
            return 3;
        } else {
            return 0;
        }
    }
}
