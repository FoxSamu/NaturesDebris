/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki.fakeworld;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.FlatGenSettings;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.function.BooleanSupplier;

public class FakeChunkProvider implements IChunkProvider {
    private final HashMap<Long, EmptyChunk> chunks = new HashMap<>();
    private final FakeWorld world;
    private ChunkGeneratorFlat flatGen;

    public FakeChunkProvider( FakeWorld world ) {
        this.world = world;
    }

    @Nullable
    @Override
    public Chunk getChunk( int x, int z, boolean load, boolean generate ) {
        long key = ChunkPos.asLong( x, z );
        return chunks.computeIfAbsent( key, pos -> new EmptyChunk( world, x, z ) );
    }

    @Nullable
    @Override
    public IChunk getChunkOrPrimer( int x, int z, boolean noEmpty ) {
        return getChunk( x, z, true, true );
    }

    @Override
    public boolean tick( BooleanSupplier hasTimeLeft ) {
        return false;
    }

    @Override
    public String makeString() {
        return "FakeChunkCache: " + this.chunks.size();
    }

    @Override
    public IChunkGenerator<?> getChunkGenerator() {
        if( flatGen == null ) {
            flatGen = new ChunkGeneratorFlat( world, new SingleBiomeProvider( new SingleBiomeProviderSettings() ), new FlatGenSettings() );
        }
        return flatGen;
    }

    @Override
    public void close() {

    }
}
