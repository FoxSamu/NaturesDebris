package test;

import net.rgsw.MathUtil;
import net.rgsw.noise.FractalPerlin3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class NoiseTest extends JFrame {
    public static final int CANVAS_WIDTH = 640;
    public static final int CANVAS_HEIGHT = 480;

    // Define the Runnable that is going to render the noise
    public NoiseRunnable noiseRunnable;

    public JSpinner zinput;
    public JSpinner tinput;

    // Main run function
    public static void main( String[] args ) {
        SwingUtilities.invokeLater( NoiseTest::new );
    }

    public NoiseTest() {
        Container cp = this.getContentPane();

        // Create a canvas
        DrawCanvas canvas = new DrawCanvas();
        canvas.setPreferredSize( new Dimension( CANVAS_WIDTH, CANVAS_HEIGHT ) );
        canvas.addMouseMotionListener( canvas );
        cp.add( canvas );

        // All the NoiseRunnables that should show up in the combo box
        NoiseRunnable[] noiseTypes = {
                this.simplex
        };

        // Create the combo box
        JComboBox<NoiseRunnable> noiseList = new JComboBox<>( noiseTypes );
        noiseList.setSelectedIndex( 0 );
        noiseList.addActionListener( e -> {
            this.noiseRunnable = (NoiseRunnable) ( (JComboBox) e.getSource() ).getSelectedItem();
            canvas.transx = 0;
            canvas.transy = 0;
            this.zinput.setValue( 0 );
            this.tinput.setValue( 0 );
            canvas.repaint();
        } );
        canvas.add( noiseList );

        // Create Z and T inputs
        this.zinput = new JSpinner();
        canvas.add( this.zinput );
        this.zinput.setPreferredSize( new Dimension( 100, 20 ) );
        this.zinput.addChangeListener( e -> canvas.repaint() );

        this.tinput = new JSpinner();
        canvas.add( this.tinput );
        this.tinput.setPreferredSize( new Dimension( 100, 20 ) );
        this.tinput.addChangeListener( e -> canvas.repaint() );

        // Set some frame constants
        this.setMinimumSize( new Dimension( 200, 200 ) );
        this.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        this.pack();
        this.setTitle( "Noise Test" );
        this.setVisible( true );

        // Set the initial runnable
        this.noiseRunnable = this.simplex;
    }

    private class DrawCanvas extends JPanel implements MouseMotionListener {

        // Translate
        public int transx;
        public int transy;

        @Override
        public void paintComponent( Graphics g ) {
            super.paintComponent( g );

            // Do some predifinitions
            Graphics2D g2 = (Graphics2D) g;
            this.setBackground( Color.BLACK );
            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            g2.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB );

            // Do predefinitions on runnable
            NoiseTest.this.noiseRunnable.g2 = g2;
            NoiseTest.this.noiseRunnable.width = this.getWidth();
            NoiseTest.this.noiseRunnable.height = this.getHeight();
            NoiseTest.this.noiseRunnable.transx = this.transx;
            NoiseTest.this.noiseRunnable.transy = this.transy;
            NoiseTest.this.noiseRunnable.transz = (int) NoiseTest.this.zinput.getValue();
            NoiseTest.this.noiseRunnable.transt = (int) NoiseTest.this.tinput.getValue();

            // Render
            NoiseTest.this.noiseRunnable.run();
        }

        private int lastx;
        private int lasty;

        @Override
        public void mouseDragged( MouseEvent e ) {
            this.transx += e.getX() - this.lastx;
            this.transy += e.getY() - this.lasty;
            this.lastx = e.getX();
            this.lasty = e.getY();
            this.repaint();
        }

        @Override
        public void mouseMoved( MouseEvent e ) {
            this.lastx = e.getX();
            this.lasty = e.getY();
        }
    }

    // Define the NoiseRunnable class
    static abstract class NoiseRunnable implements Runnable {
        public Graphics2D g2;
        public int width;
        public int height;
        public int transx;
        public int transy;
        public int transz;
        public int transt;

        public abstract String toString();
    }

    public final NoiseRunnable simplex = new NoiseRunnable() {
        private FractalPerlin3D noise = new FractalPerlin3D( 271259258, 80D, 1 );
        private int res = 1;

        private int tt;

        @Override
        public void run() {
            if( tt != transt ) {
                noise = new FractalPerlin3D( 271259258, 80D, Math.min( Math.max( 1, transt ), 16 ) );
                tt = transt;
            }
            for( int i = 0; i < this.width / this.res + 1; i++ ) {
                for( int j = 0; j < this.height / this.res + 1; j++ ) {
                    double depthNoise = this.noise.generateMultiplied( i * this.res - this.transx, j * this.res - this.transy, this.transz, 65536 ) / 8000;
                    // -1               1                -8
                    if( depthNoise < 0.0D ) {
                        depthNoise = - depthNoise * 0.3; // ?   // 0.3              1                -2.4
                    }

                    depthNoise = depthNoise * 3 - 2; // ?       // 0.4              1                -0.72
                    if( depthNoise < 0 ) {
                        depthNoise /= 2; // ?                   //                                   -0.36

                        if( depthNoise < - 1 ) { // Clamp       //                                   -0.36
                            depthNoise = - 1;
                        }

                        depthNoise /= 2.8; // ?                 //                                   -0.1285714
                    } else {
                        if( depthNoise > 1 ) { // Clamp         // 0.4              1
                            depthNoise = 1;
                        }

                        depthNoise /= 8; // ?                   // 0.05             0.125
                    }
                    depthNoise = MathUtil.invLerp( - 1 / 2.8, 1 / 8D, depthNoise );
                    depthNoise = MathUtil.clamp( depthNoise, 0, 1 );
                    this.g2.setColor( new Color( (float) depthNoise, (float) depthNoise, (float) depthNoise ) );
                    this.g2.fillRect( i * this.res, j * this.res, this.res, this.res );
                }
            }
        }

        @Override
        public String toString() {return "OpenSimplex noise";}
    };
}
