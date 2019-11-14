/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.client.reloader;

import modernity.api.biome.BiomeColoringProfile;
import modernity.common.Modernity;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Reloads {@link BiomeColoringProfile}s off-thread.
 */
public class BiomeColorProfileReloader extends ReloadListener<BiomeColoringProfile> {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ResourceLocation type;
    private final Consumer<BiomeColoringProfile> out;

    public BiomeColorProfileReloader( ResourceLocation type, Consumer<BiomeColoringProfile> out ) {
        this.type = type;
        this.out = out;
    }

    public BiomeColorProfileReloader( String type, Consumer<BiomeColoringProfile> out ) {
        this( Modernity.res( type ), out );
    }

    @Override
    protected BiomeColoringProfile prepare( IResourceManager resManager, IProfiler profiler ) {
        ResourceLocation loc = new ResourceLocation( type.getNamespace(), "data/biomecolors/" + type.getPath() + ".json" );

        try {
            return BiomeColoringProfile.create( resManager, loc, type.toString() );
        } catch( IOException e ) {
            LOGGER.error( "Unable to load color profile '" + type + "' due to exception!", e );
            return new BiomeColoringProfile();
        }
    }

    @Override
    protected void apply( BiomeColoringProfile splashList, IResourceManager resManager, IProfiler profiler ) {
        out.accept( splashList );
    }
}
