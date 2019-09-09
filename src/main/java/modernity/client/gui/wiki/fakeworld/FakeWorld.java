/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki.fakeworld;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tags.NetworkTagManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedDataStorage;

public class FakeWorld extends World {
    private final Scoreboard scoreboard = new Scoreboard();
    private final RecipeManager rcpManager = new RecipeManager();
    private final NetworkTagManager tagManager = new NetworkTagManager();
    private final ITickList<Block> blockTicks = new EmptyTickList<>();
    private final ITickList<Fluid> fluidTicks = new EmptyTickList<>();

    private FakeWorld( ISaveHandler saveHandler ) {
        super( saveHandler, new WorldSavedDataStorage( saveHandler ),
                new WorldInfo( new WorldSettings( 0, GameType.CREATIVE, false, false, WorldType.DEFAULT ), "FakeWorld" ),
                new OverworldDimension(), new Profiler(), false
        );
        chunkProvider = createChunkProvider();
        dimension.setWorld( this );
    }

    public FakeWorld() {
        this( null );
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        return new FakeChunkProvider( this );
    }

    @Override
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public RecipeManager getRecipeManager() {
        return rcpManager;
    }

    @Override
    public NetworkTagManager getTags() {
        return tagManager;
    }

    @Override
    public ITickList<Block> getPendingBlockTicks() {
        return blockTicks;
    }

    @Override
    public ITickList<Fluid> getPendingFluidTicks() {
        return fluidTicks;
    }

    @Override
    public boolean isChunkLoaded( int x, int z, boolean allowEmpty ) {
        return true;
    }

    @Override
    public BlockPos getSpawnPoint() {
        return BlockPos.ORIGIN;
    }
}
