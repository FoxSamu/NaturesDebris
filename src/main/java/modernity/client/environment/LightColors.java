package modernity.client.environment;

import net.rgsw.MathUtil;

public class LightColors {
    public static final LightColors MAIN = new LightColors();

    public final double[] ambient = { 0, 0, 0 };
    public final double[] sky = { 0, 0, 0 };
    public final double[] block = { 0, 0, 0 };

    public void setAmbient( double r, double g, double b ) {
        ambient[ 0 ] = r;
        ambient[ 1 ] = g;
        ambient[ 2 ] = b;
    }

    public void setAmbient( int col ) {
        ambient[ 0 ] = ( col >>> 16 & 0xff ) / 255D;
        ambient[ 1 ] = ( col >>> 8 & 0xff ) / 255D;
        ambient[ 2 ] = ( col & 0xff ) / 255D;
    }

    public void setSky( double r, double g, double b ) {
        sky[ 0 ] = r;
        sky[ 1 ] = g;
        sky[ 2 ] = b;
    }

    public void setSky( int col ) {
        sky[ 0 ] = ( col >>> 16 & 0xff ) / 255D;
        sky[ 1 ] = ( col >>> 8 & 0xff ) / 255D;
        sky[ 2 ] = ( col & 0xff ) / 255D;
    }

    public void setBlock( double r, double g, double b ) {
        block[ 0 ] = r;
        block[ 1 ] = g;
        block[ 2 ] = b;
    }

    public void setBlock( int col ) {
        block[ 0 ] = ( col >>> 16 & 0xff ) / 255D;
        block[ 1 ] = ( col >>> 8 & 0xff ) / 255D;
        block[ 2 ] = ( col & 0xff ) / 255D;
    }

    public void apply( LightColors col ) {
        ambient[ 0 ] = col.ambient[ 0 ];
        ambient[ 1 ] = col.ambient[ 1 ];
        ambient[ 2 ] = col.ambient[ 2 ];
        sky[ 0 ] = col.sky[ 0 ];
        sky[ 1 ] = col.sky[ 1 ];
        sky[ 2 ] = col.sky[ 2 ];
        block[ 0 ] = col.block[ 0 ];
        block[ 1 ] = col.block[ 1 ];
        block[ 2 ] = col.block[ 2 ];
    }

    public void apply( LightColors col, double amount ) {
        ambient[ 0 ] = MathUtil.lerp( ambient[ 0 ], col.ambient[ 0 ], amount );
        ambient[ 1 ] = MathUtil.lerp( ambient[ 1 ], col.ambient[ 1 ], amount );
        ambient[ 2 ] = MathUtil.lerp( ambient[ 2 ], col.ambient[ 2 ], amount );
        sky[ 0 ] = MathUtil.lerp( sky[ 0 ], col.sky[ 0 ], amount );
        sky[ 1 ] = MathUtil.lerp( sky[ 1 ], col.sky[ 1 ], amount );
        sky[ 2 ] = MathUtil.lerp( sky[ 2 ], col.sky[ 2 ], amount );
        block[ 0 ] = MathUtil.lerp( block[ 0 ], col.block[ 0 ], amount );
        block[ 1 ] = MathUtil.lerp( block[ 1 ], col.block[ 1 ], amount );
        block[ 2 ] = MathUtil.lerp( block[ 2 ], col.block[ 2 ], amount );
    }
}
