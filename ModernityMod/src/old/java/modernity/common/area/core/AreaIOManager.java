/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.area.core;

import modernity.generic.util.BMFRegionCacher;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import modernity.util.Randomizer;
import net.rgsw.io.BMFFile;

import java.io.File;
import java.io.IOException;

public class AreaIOManager extends BMFRegionCacher {
    protected final World world;

    public AreaIOManager(File folder, World world) {
        super(folder);
        this.world = world;
    }

    private static long longify(int regX, int regZ, int id) {
        return (regX & 0xffffL) << 48 | (regZ & 0xffffL) << 32 | id & 0xffffffffL;
    }

    public Area loadArea(long refID) {
        short regX = (short) (refID >>> 48 & 0xffffL);
        short regZ = (short) (refID >>> 32 & 0xffffL);
        try {
            CompoundNBT nbt = loadNBT(regX, regZ, refID);

            if(nbt == null) return null;
            return Area.deserialize(nbt, refID, world, Area.SerializeType.FILESYSTEM);
        } catch(Exception exc) {
            CrashReport report = CrashReport.makeCrashReport(exc, "Loading world area");
            report.makeCategory("Reference")
                  .addDetail("Region Pos", new ChunkPos(regX, regZ))
                  .addDetail("Reference", Long.toString(refID, 16));
            throw new ReportedException(report);
        }
    }

    public void saveArea(long refID, Area area) {
        short regX = (short) (refID >>> 48 & 0xffffL);
        short regZ = (short) (refID >>> 32 & 0xffffL);
        AreaType type = area.getType();
        try {
            CompoundNBT nbt = Area.serialize(area, Area.SerializeType.FILESYSTEM);
            saveNBT(regX, regZ, refID, nbt);
        } catch(Exception exc) {
            CrashReport report = CrashReport.makeCrashReport(exc, "Saving world area");
            report.makeCategory("Reference")
                  .addDetail("Region Pos", new ChunkPos(regX, regZ))
                  .addDetail("Reference", Long.toString(refID, 16))
                  .addDetail("Area Type", type.getRegistryName());
            throw new ReportedException(report);
        }
    }

    public void removeArea(long refID) {
        short regX = (short) (refID >>> 48 & 0xffffL);
        short regZ = (short) (refID >>> 32 & 0xffffL);
        try {
            removeNBT(regX, regZ, refID);
        } catch(Exception exc) {
            CrashReport report = CrashReport.makeCrashReport(exc, "Removing world area");
            report.makeCategory("Reference")
                  .addDetail("Region Pos", new ChunkPos(regX, regZ))
                  .addDetail("Reference", Long.toString(refID, 16));
            throw new ReportedException(report);
        }
    }

    public long findFreeRefID(int regX, int regZ) {
        try {
            BMFFile bmf = loadFile(regX, regZ);
            int randomID = Randomizer.randomSignedInt();

            while(randomID == 0 || bmf.containsEntry(longify(regX, regZ, randomID))) {
                randomID++;
            }

            return longify(regX, regZ, randomID);
        } catch(Exception exc) {
            CrashReport report = CrashReport.makeCrashReport(exc, "Finding a free area ID for region");
            report.makeCategory("Region")
                  .addDetail("Pos", new ChunkPos(regX, regZ));
            throw new ReportedException(report);
        }
    }

    public void saveAll() {
        try {
            flushAll();
        } catch(IOException exc) {
            CrashReport report = CrashReport.makeCrashReport(exc, "Flushing world areas to file system");
            throw new ReportedException(report);
        }
    }

    @Override
    protected int sectorSize() {
        return 128;
    }

    @Override
    protected String getFileName(int x, int z) {
        return "a." + x + "." + z;
    }
}
