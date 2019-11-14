/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.area.core;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class TrackableAreaReferenceChunk extends SimpleAreaReferenceChunk {
    private final ObjectOpenHashSet<ServerPlayerEntity> trackers = new ObjectOpenHashSet<>();

    public TrackableAreaReferenceChunk( int x, int z ) {
        super( x, z );
    }

    public boolean track( ServerPlayerEntity entity ) {
        if( ! trackers.contains( entity ) ) {
            trackers.add( entity );
            return true;
        }
        return false;
    }

    public boolean untrack( ServerPlayerEntity entity ) {
        if( trackers.contains( entity ) ) {
            trackers.remove( entity );
            return true;
        }
        return false;
    }

    public Set<ServerPlayerEntity> untrackAll() {
        Set<ServerPlayerEntity> unwatched = new HashSet<>( trackers );
        trackers.clear();
        return unwatched;
    }

    public boolean isTracked( ServerPlayerEntity entity ) {
        return trackers.contains( entity );
    }

    public boolean isNotTracked( ServerPlayerEntity entity ) {
        return ! trackers.contains( entity );
    }

    public Stream<ServerPlayerEntity> trackerStream() {
        return trackers.stream();
    }


    public boolean isTracked() {
        return ! trackers.isEmpty();
    }

    public boolean isNotTracked() {
        return trackers.isEmpty();
    }
}
