/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors;

import modernity.client.colors.provider.ErrorColorProvider;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ColorProfileManager implements ISelectiveResourceReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();

    private final IResourceManager resManager;
    private final Consumer<ColorProfileManager> reloadHandler;

    private final ArrayList<ResourceLocation> loading = new ArrayList<>();
    private final HashMap<ResourceLocation, ColorProfile> loaded = new HashMap<>();

    public ColorProfileManager( IResourceManager resManager, Consumer<ColorProfileManager> reloadHandler ) {
        this.resManager = resManager;
        this.reloadHandler = reloadHandler;
    }

    public ColorProfile load( ResourceLocation loc ) {
        if( loaded.containsKey( loc ) ) return loaded.get( loc );
        if( loading.contains( loc ) ) {
            LOGGER.warn( "Color profile '{}' references itself! Using error color...", loc );
            return new ColorProfile( ErrorColorProvider.INSTANCE );
        }

        loading.add( loc );
        ColorProfile profile = ColorProfile.load( resManager, loc );
        loading.remove( loading.size() - 1 );
        loaded.put( loc, profile );
        return profile;
    }

    @Override
    public void onResourceManagerReload( IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate ) {
        loaded.clear();
        reloadHandler.accept( this );
    }
}
