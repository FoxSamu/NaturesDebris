/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.chunks.core;

import net.minecraft.util.math.ChunkPos;

import java.util.ArrayDeque;
import java.util.function.Consumer;

public class OverlayChunkHolder {
    private final ServerOverlayChunkManager manager;
    private final ChunkPos pos;
    private final ArrayDeque<Consumer<OverlayChunkHolder>> tasks = new ArrayDeque<>();

    private volatile OverlayChunk chunk;
    private volatile boolean runningTasks;

    public OverlayChunkHolder( ServerOverlayChunkManager manager, ChunkPos pos ) {
        this.manager = manager;
        this.pos = pos;
    }

    public void setChunk( OverlayChunk chunk ) {
        if( runningTasks ) throw new IllegalStateException( "Can't set holder chunk from task" );

        this.chunk = chunk;

        runningTasks = true;
        if( chunk != null ) {
            while( ! tasks.isEmpty() ) {
                tasks.poll().accept( this );
            }
        }
        runningTasks = false;
    }

    public OverlayChunk getChunk() {
        return chunk;
    }

    public ChunkPos getPos() {
        return pos;
    }

    public ServerOverlayChunkManager getManager() {
        return manager;
    }

    public void execute( Consumer<OverlayChunkHolder> task ) {
        if( chunk != null ) {
            task.accept( this );
        } else {
            tasks.add( task );
        }
    }

    public void executeIfLoaded( Consumer<OverlayChunkHolder> task ) {
        if( chunk != null ) {
            task.accept( this );
        }
    }

    public boolean isLoaded() {
        return chunk != null;
    }
}
