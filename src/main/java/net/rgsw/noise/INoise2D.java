/*
 * Copyright (c) 2019 RGSW.
 * This file belongs to a separate library, made for the Modernity.
 * Licensed under the Apache Licence v2.0. Do not redistribute.
 *
 * Date: 6 - 16 - 2019
 */

package net.rgsw.noise;

import net.rgsw.MathUtil;

public interface INoise2D {
    default double generate( int x, int y ) {
        return this.generate( (double) x, (double) y );
    }
    double generate( double x, double y );


    default INoise2D add( double amount ) {
        return ( x, y ) -> generate( x, y ) + amount;
    }

    default INoise2D subtract( double amount ) {
        return ( x, y ) -> generate( x, y ) - amount;
    }

    default INoise2D multiply( double amount ) {
        return ( x, y ) -> generate( x, y ) * amount;
    }

    default INoise2D divide( double amount ) {
        return ( x, y ) -> generate( x, y ) / amount;
    }


    default INoise2D add( INoise2D amount ) {
        return ( x, y ) -> generate( x, y ) + amount.generate( x, y );
    }

    default INoise2D subtract( INoise2D amount ) {
        return ( x, y ) -> generate( x, y ) - amount.generate( x, y );
    }

    default INoise2D multiply( INoise2D amount ) {
        return ( x, y ) -> generate( x, y ) * amount.generate( x, y );
    }

    default INoise2D divide( INoise2D amount ) {
        return ( x, y ) -> generate( x, y ) / amount.generate( x, y );
    }

    default INoise2D inverse() {
        return ( x, y ) -> - generate( x, y );
    }

    default INoise2D lerp( double min, double max ) {
        return ( x, y ) -> MathUtil.lerp( min, max, ( generate( x, y ) + 1 ) / 2 );
    }

    default INoise2D lerp( INoise2D min, INoise2D max ) {
        return ( x, y ) -> MathUtil.lerp( min.generate( x, y ), max.generate( x, y ), ( generate( x, y ) + 1 ) / 2 );
    }

    static INoise2D staticValue( double value ) {
        return ( x, y ) -> value;
    }

    static INoise2D from3D( INoise3D noise, double z ) {
        return ( x, y ) -> noise.generate( x, y, z );
    }

    static INoise2D combine( IDoubleCombiner combiner, INoise2D... noises ) {
        return ( x, y ) -> {
            double[] arr = new double[ noises.length ];
            int i = 0;
            for( INoise2D noise : noises ) {
                arr[ i ] = noise.generate( x, y );
            }
            return combiner.combine( arr );
        };
    }

    default INoise2D scale( double scale ) {
        return ( x, y ) -> generate( x * scale, y * scale );
    }

    default INoise2D scale( double x, double y ) {
        return ( x1, y1 ) -> generate( x1 * x, y1 * y );
    }

    default INoise2D translate( double x, double y ) {
        return ( x1, y1 ) -> generate( x1 + x, y1 + y );
    }

    default INoise2D fractal( int octaves ) {
        return ( x, y ) -> {
            double n = 0;
            double m = 1;
            for( int i = 0; i < octaves; i++ ) {
                n += generate( x / m, y / m ) * m;
                m /= 2;
            }
            return n;
        };
    }
}
