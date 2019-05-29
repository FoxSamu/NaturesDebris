package test;

import net.rgsw.noise.InverseFractalPerlin3D;

public class Darken {
    public static void main( String[] args ) {
        InverseFractalPerlin3D improved = new InverseFractalPerlin3D( 52712488, 614, 614, 614, 16 );
        for( int i = 0; i < 100; i++ ) {
            System.out.println( improved.generate( i, 0.2, 0.2 ) );
        }
    }
}
