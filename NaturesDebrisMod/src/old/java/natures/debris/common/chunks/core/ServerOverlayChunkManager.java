/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.chunks.core;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;

import java.io.File;
import java.util.concurrent.Executor;

public class ServerOverlayChunkManager {
    private final ServerWorld world;
    private final ChunkManager vanillaManager;

    private final Object2ObjectOpenHashMap<ChunkPos, OverlayChunkHolder> loadedChunks = new Object2ObjectOpenHashMap<>();

    private final OverlayChunkIO io;

    private final Executor directExecutor;
    private final Executor asyncExecutor;

    public ServerOverlayChunkManager(ServerWorld world, Executor directExecutor, Executor asyncExecutor) {
        this.world = world;
        this.vanillaManager = world.getChunkProvider().chunkManager;
        this.directExecutor = directExecutor;
        this.asyncExecutor = asyncExecutor;

        File folder = getFolder(world);
        io = new OverlayChunkIO(folder, OverlayChunk::new);
    }

    public OverlayChunkIO getIO() {
        return io;
    }

    private static File getFolder(ServerWorld world) {
        return new File(world.dimension.getType().getDirectory(world.getSaveHandler().getWorldDirectory()), "md/overlay");
    }

    private class ChunkLoadTask implements Runnable {
        private final OverlayChunkHolder chunkHolder;

        private ChunkLoadTask(OverlayChunkHolder chunkHolder) {
            this.chunkHolder = chunkHolder;
        }

        @Override
        public void run() {
            if (chunkHolder.isLoaded()) return;
            OverlayChunk chunk = io.loadChunk(chunkHolder.getPos());
            chunkHolder.setChunk(chunk);
        }
    }
}
