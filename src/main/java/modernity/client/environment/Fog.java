/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 12 - 2019
 */

package modernity.client.environment;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.rgsw.MathUtil;

@OnlyIn( Dist.CLIENT )
public class Fog {
    public static final Fog MAIN = new Fog();

    public double density = 0.01;
    public double start = 20;
    public double end = 100;
    public final double[] color = { 0, 0, 0 };
    public Mode mode = Mode.EXP;
    public boolean enabled;

    public void setColor( double r, double g, double b ) {
        color[ 0 ] = r;
        color[ 1 ] = g;
        color[ 2 ] = b;
    }

    public void setColor( int col ) {
        color[ 0 ] = ( col >>> 16 & 0xff ) / 255D;
        color[ 1 ] = ( col >>> 8 & 0xff ) / 255D;
        color[ 2 ] = ( col & 0xff ) / 255D;
    }

    public void apply( Fog fog ) {
        density = fog.density;
        start = fog.start;
        end = fog.end;
        color[ 0 ] = fog.color[ 0 ];
        color[ 1 ] = fog.color[ 1 ];
        color[ 2 ] = fog.color[ 2 ];
        mode = fog.mode;
        enabled = fog.enabled;
    }

    public void apply( Fog fog, double amount ) {
        density = MathUtil.lerp( density, fog.density, amount );
        start = MathUtil.lerp( start, fog.start, amount );
        end = MathUtil.lerp( end, fog.end, amount );
        color[ 0 ] = MathUtil.lerp( color[ 0 ], fog.color[ 0 ], amount );
        color[ 1 ] = MathUtil.lerp( color[ 1 ], fog.color[ 1 ], amount );
        color[ 2 ] = MathUtil.lerp( color[ 2 ], fog.color[ 2 ], amount );
        mode = amount < 0.5 ? mode : fog.mode;
        enabled = amount < 0.5 ? enabled : fog.enabled;
    }

    public enum Mode {
        LIN,
        EXP,
        EXP2
    }
}
