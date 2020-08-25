/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.area.core;

import natures.debris.generic.util.BMFRegionCacher;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class AreaReferenceManager extends BMFRegionCacher {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<ChunkPos, TrackableAreaReferenceChunk> loadedReferenceChunks = Collections.synchronizedMap(new HashMap<>());
    private final Queue<ChunkPos> unloadQueue = new ArrayDeque<>();

    protected AreaReferenceManager(File dir) {
        super(dir);
    }

    public synchronized TrackableAreaReferenceChunk getLoadedChunk(int x, int z) {
        return loadedReferenceChunks.get(new ChunkPos(x, z));
    }

    private TrackableAreaReferenceChunk loadChunk(int x, int z) {
        try {
            CompoundNBT nbt = readChunk(new ChunkPos(x, z));
            TrackableAreaReferenceChunk chunk = new TrackableAreaReferenceChunk(x, z);
            if (nbt != null) {
                chunk.read(nbt);
            }
            loadedReferenceChunks.put(new ChunkPos(x, z), chunk);
            return chunk;
        } catch (IOException exc) {
            CrashReport report = CrashReport.makeCrashReport(exc, "Loading area reference chunk");
            report.makeCategory("Area reference chunk")
                  .addDetail("Chunk pos", new ChunkPos(x, z));
            throw new ReportedException(report);
        }
    }

    public TrackableAreaReferenceChunk getChunk(int x, int z) {
        TrackableAreaReferenceChunk chunk = getLoadedChunk(x, z);
        if (chunk == null) {
            chunk = loadChunk(x, z);
        }
        return chunk;
    }

    private void saveChunk(int x, int z) {
        TrackableAreaReferenceChunk saving = getLoadedChunk(x, z);
        if (saving == null) {
            LOGGER.warn("Attempted to save unloaded chunk");
            return;
        }
        if (!saving.isDirty()) return;
        CompoundNBT nbt = new CompoundNBT();
        saving.write(nbt);
        try {
            writeChunk(new ChunkPos(x, z), nbt);
        } catch (IOException exc) {
            CrashReport report = CrashReport.makeCrashReport(exc, "Saving area reference chunk");
            report.makeCategory("Area reference chunk")
                  .addDetail("Chunk pos", new ChunkPos(x, z));
            throw new ReportedException(report);
        }
        saving.setDirty(false);
    }

    private void writeChunk(ChunkPos pos, CompoundNBT nbt) throws IOException {
        long key = pos.asLong();
        saveNBTIfNotEmpty(pos.getRegionCoordX(), pos.getRegionCoordZ(), key, nbt);
    }

    private CompoundNBT readChunk(ChunkPos pos) throws IOException {
        long key = pos.asLong();
        return loadNBT(pos.getRegionCoordX(), pos.getRegionCoordZ(), key);
    }

    public boolean isLoaded(int x, int z) {
        return loadedReferenceChunks.containsKey(new ChunkPos(x, z));
    }

    public void load(int x, int z) {
        if (isLoaded(x, z)) return;
        loadChunk(x, z);
    }

    public void unload(int x, int z) {
        if (!isLoaded(x, z)) return;
        saveChunk(x, z);
        loadedReferenceChunks.remove(new ChunkPos(x, z));
    }

    public void saveAll() {
        for (ChunkPos pos : loadedReferenceChunks.keySet()) {
            saveChunk(pos.x, pos.z);
        }

        try {
            flushAll();
        } catch (Exception exc) {
            CrashReport report = CrashReport.makeCrashReport(exc, "Flushing world area references to file system");
            throw new ReportedException(report);
        }
        LOGGER.info("Saved all reference chunks");
    }

    public void unloadAll() {
        saveAll();
        loadedReferenceChunks.clear();
    }

    public Stream<TrackableAreaReferenceChunk> loadedChunksStream() {
        return loadedReferenceChunks.values().stream();
    }

    public void unloadNotWatched(Consumer<ChunkPos> doBeforeUnload) {
        for (ChunkPos pos : loadedReferenceChunks.keySet()) {
            TrackableAreaReferenceChunk chunk = loadedReferenceChunks.get(pos);
            if (!chunk.isTracked()) {
                unloadQueue.add(pos);
            }
        }
        while (!unloadQueue.isEmpty()) {
            ChunkPos pos = unloadQueue.poll();
            doBeforeUnload.accept(pos);
            unload(pos.x, pos.z);
        }
    }

    @Override
    protected String getFileName(int x, int z) {
        return "r." + x + "." + z;
    }

    @Override
    protected int sectorSize() {
        return 128;
    }
}
