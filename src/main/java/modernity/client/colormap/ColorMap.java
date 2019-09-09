/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.client.colormap;

import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.function.Predicate;

public class ColorMap implements ISelectiveResourceReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();

    private final ResourceLocation location;
    private final int fallbackColor;
    private boolean loaded;
    private int width;
    private int height;
    private int[] values;

    public ColorMap( ResourceLocation location, int fallbackColor ) {
        this.location = location;
        this.fallbackColor = fallbackColor;
    }

    public ColorMap( ResourceLocation location ) {
        this( location, 0xffff00ff );
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int get( int x, int y ) {
        if( ! loaded || outOfBounds( x, y ) ) return fallbackColor;
        return values[ y * width + x ];
    }

    public int get( double x, double y ) {
        return get( (int) ( x * ( width - 1 ) ), (int) ( y * ( width - 1 ) ) );
    }

    public int get( float x, float y ) {
        return get( (int) ( x * ( width - 1 ) ), (int) ( y * ( width - 1 ) ) );
    }

    public int random( Random rand ) {
        if( ! loaded ) return fallbackColor;
        return values[ rand.nextInt( values.length ) ];
    }

    private boolean outOfBounds( int x, int y ) {
        return x >= width || x < 0 || y >= height || y < 0;
    }

    @Override
    public void onResourceManagerReload( IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate ) {
//        if( ! skipPreload ) {
//            skipPreload = true;
//            return;
//        }
        if( resourcePredicate.test( VanillaResourceType.TEXTURES ) ) {
            reload( resourceManager );
        }
    }

    public void reload( IResourceManager manager ) {
        try {
            IResource resource = manager.getResource( location );
            InputStream stream = resource.getInputStream();

            BufferedImage image = ImageIO.read( stream );
            if( image == null ) throw new IOException( "No ImageReader available for current format" );
            int w = width = image.getWidth();
            int h = height = image.getHeight();

            LOGGER.debug( "Loaded color map will be {}x{}", w, h );

            values = new int[ w * h ];

            for( int x = 0; x < w; x++ ) {
                for( int y = 0; y < h; y++ ) {
                    values[ y * w + x ] = image.getRGB( x, y );
                }
            }

            loaded = true;
            LOGGER.info( "Loaded color map {}", location );
        } catch( IOException exc ) {
            LOGGER.error( "Unable to load color map " + location, exc );
            LOGGER.error( "A fallback color will be used (magenta, unless otherwise configured)" );
            loaded = false;
        }
    }
}
