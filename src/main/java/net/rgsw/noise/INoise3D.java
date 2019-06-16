/*
 * Copyright (c) 2019 RGSW.
 * This file belongs to a separate library, made for the Modernity.
 * Licensed under the Apache Licence v2.0. Do not redistribute.
 *
 * Date: 6 - 16 - 2019
 */

package net.rgsw.noise;

import net.rgsw.MathUtil;

public interface INoise3D {
    default double generate( int x, int y, int z ) {
        return this.generate( (double) x, (double) y, (double) z );
    }
    double generate( double x, double y, double z );


    default INoise3D add( double amount ) {
        return ( x, y, z ) -> generate( x, y, z ) + amount;
    }

    default INoise3D subtract( double amount ) {
        return ( x, y, z ) -> generate( x, y, z ) - amount;
    }

    default INoise3D multiply( double amount ) {
        return ( x, y, z ) -> generate( x, y, z ) * amount;
    }

    default INoise3D divide( double amount ) {
        return ( x, y, z ) -> generate( x, y, z ) / amount;
    }


    default INoise3D add( INoise3D amount ) {
        return ( x, y, z ) -> generate( x, y, z ) + amount.generate( x, y, z );
    }

    default INoise3D subtract( INoise3D amount ) {
        return ( x, y, z ) -> generate( x, y, z ) - amount.generate( x, y, z );
    }

    default INoise3D multiply( INoise3D amount ) {
        return ( x, y, z ) -> generate( x, y, z ) * amount.generate( x, y, z );
    }

    default INoise3D divide( INoise3D amount ) {
        return ( x, y, z ) -> generate( x, y, z ) / amount.generate( x, y, z );
    }

    default INoise3D inverse() {
        return ( x, y, z ) -> - generate( x, y, z );
    }

    default INoise3D lerp( double min, double max ) {
        return ( x, y, z ) -> MathUtil.lerp( min, max, ( generate( x, y, z ) + 1 ) / 2 );
    }

    default INoise3D lerp( INoise3D min, INoise3D max ) {
        return ( x, y, z ) -> MathUtil.lerp( min.generate( x, y, z ), max.generate( x, y, z ), ( generate( x, y, z ) + 1 ) / 2 );
    }

    static INoise3D staticValue( double value ) {
        return ( x, y, z ) -> value;
    }

    static INoise3D from2DXY( INoise2D noise ) {
        return ( x, y, z ) -> noise.generate( x, y );
    }

    static INoise3D from2DYZ( INoise2D noise ) {
        return ( x, y, z ) -> noise.generate( y, z );
    }

    static INoise3D from2DXZ( INoise2D noise ) {
        return ( x, y, z ) -> noise.generate( x, z );
    }

    static INoise3D combine( IDoubleCombiner combiner, INoise3D... noises ) {
        return ( x, y, z ) -> {
            double[] arr = new double[ noises.length ];
            int i = 0;
            for( INoise3D noise : noises ) {
                arr[ i ] = noise.generate( x, y, z );
            }
            return combiner.combine( arr );
        };
    }

    default INoise3D scale( double scale ) {
        return ( x, y, z ) -> generate( x * scale, y * scale, z * scale );
    }

    default INoise3D scale( double x, double y, double z ) {
        return ( x1, y1, z1 ) -> generate( x1 * x, y1 * y, z1 * z );
    }

    default INoise3D translate( double x, double y, double z ) {
        return ( x1, y1, z1 ) -> generate( x1 + x, y1 + y, z1 * z );
    }

    default INoise3D fractal( int octaves ) {
        return ( x, y, z ) -> {
            double n = 0;
            double m = 1;
            for( int i = 0; i < octaves; i++ ) {
                n += generate( x / m, y / m, z / m ) * m;
                m /= 2;
            }
            return n;
        };
    }
}
