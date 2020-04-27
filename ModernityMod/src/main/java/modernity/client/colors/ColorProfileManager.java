/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.colors;

// TODO Re-evaluate
//public class ColorProfileManager implements ISelectiveResourceReloadListener {
//    private static final Logger LOGGER = LogManager.getLogger();
//
//    private final IResourceManager resManager;
//    private final Consumer<ColorProfileManager> reloadHandler;
//
//    private final ArrayList<ResourceLocation> loading = new ArrayList<>();
//    private final HashMap<ResourceLocation, ColorProfile> loaded = new HashMap<>();
//
//    public ColorProfileManager( IResourceManager resManager, Consumer<ColorProfileManager> reloadHandler ) {
//        this.resManager = resManager;
//        this.reloadHandler = reloadHandler;
//    }
//
//    public ColorProfile load( ResourceLocation loc ) {
//        if( loaded.containsKey( loc ) ) return loaded.get( loc );
//        if( loading.contains( loc ) ) {
//            LOGGER.warn( "Color profile '{}' references itself! Using error color...", loc );
//            return new ColorProfile( ErrorColorProvider.INSTANCE );
//        }
//
//        loading.add( loc );
//        ColorProfile profile = ColorProfile.load( resManager, loc );
//        loading.remove( loading.size() - 1 );
//        loaded.put( loc, profile );
//        return profile;
//    }
//
//    @Override
//    public void onResourceManagerReload( IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate ) {
//        loaded.clear();
//        reloadHandler.accept( this );
//    }
//}
