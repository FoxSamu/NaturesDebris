/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.util;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.math.ChunkPos;
import net.rgsw.io.BMFFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public abstract class BMFRegionCacher {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Long2ObjectLinkedOpenHashMap<BMFFile> cache = new Long2ObjectLinkedOpenHashMap<>();
    private final File folder;

    public BMFRegionCacher( File folder ) {
        this.folder = folder;
        if( ! folder.exists() ) {
            folder.mkdirs();
        }
    }

    protected BMFFile loadFile( int x, int z ) throws IOException {
        long key = ChunkPos.asLong( x, z );
        BMFFile bmf = cache.getAndMoveToFirst( key );
        if( bmf != null ) {
            return bmf;
        } else {
            if( cache.size() >= 256 ) {
                cache.removeLast().close();
            }

            if( ! folder.exists() ) {
                folder.mkdirs();
            }

            File file = new File( folder, getFileName( x, z ) + "." + getFileExtension() );
            BMFFile newBMF = BMFFile.open( file, sectorSize(), compressionType() );
            cache.putAndMoveToFirst( key, newBMF );
            return newBMF;
        }
    }

    public CompoundNBT loadNBT( int x, int z, long key ) throws IOException {
        BMFFile bmf = loadFile( x, z );

        if( bmf.containsEntry( key ) ) {
            try( DataInputStream in = new DataInputStream( bmf.readEntry( key ) ) ) {
                return CompressedStreamTools.read( in );
            }
        }
        return null;
    }

    public void saveNBT( int x, int z, long key, CompoundNBT nbt ) throws IOException {
        BMFFile bmf = loadFile( x, z );

        try( DataOutputStream in = new DataOutputStream( bmf.writeEntry( key ) ) ) {
            CompressedStreamTools.write( nbt, in );
        }
    }

    public void removeNBT( int x, int z, long key ) throws IOException {
        BMFFile bmf = loadFile( x, z );

        bmf.removeEntry( key );
    }

    public void saveNBTIfNotEmpty( int x, int z, long key, CompoundNBT nbt ) throws IOException {
        if( nbt.isEmpty() ) removeNBT( x, z, key );
        else saveNBT( x, z, key, nbt );
    }

    public void flushAll() throws IOException {
        for( BMFFile file : cache.values() ) {
            try {
                file.flush();
            } catch( IOException exc ) {
                throw exc;
            } catch( Throwable exc ) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream stream = new PrintStream( baos );
                file.printDebug( stream );

                String str = baos.toString();
                String[] lines = str.split( "[\n\r]" );
                LOGGER.error( "Internal BMF failure, here is a debug dump:" );
                for( String line : lines ) {
                    LOGGER.error( line );
                }
                throw exc;
            }
        }
    }

    protected String getFileName( int x, int z ) {
        return "r." + x + "." + z;
    }

    protected String getFileExtension() {
        return "bmf";
    }

    protected int sectorSize() {
        return 512;
    }

    protected BMFFile.Compression compressionType() {
        return BMFFile.Compression.DEFLATE;
    }
}
