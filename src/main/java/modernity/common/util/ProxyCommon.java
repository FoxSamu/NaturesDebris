/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 7 - 2019
 */

package modernity.common.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import modernity.common.command.MDCommands;
import modernity.common.handler.CapabilityHandler;
import modernity.common.handler.StructureHandler;
import modernity.common.net.MDPackets;
import modernity.common.world.dim.MDDimensions;
import modernity.common.world.gen.structure.CurseRuinStructure;
import modernity.common.world.gen.structure.MDStructures;

public class ProxyCommon {

    private LootTableManager lootManager;

    /**
     * Called on init phase. This is when FML fires the {@link FMLCommonSetupEvent}.
     */
    public void init() {
        Hooks.setupBiomeStructures();
        MDStructures.register();
        MDPackets.register();
        MDLootTables.load();
    }

    /**
     * Called when loading completed. This is when FML fires the {@link FMLLoadCompleteEvent}.
     */
    public void loadComplete() {
        Biomes.NETHER.addStructure( MDStructures.NETHER_ALTAR, IFeatureConfig.NO_FEATURE_CONFIG );
        Biomes.NETHER.addFeature( GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createCompositeFeature( MDStructures.NETHER_ALTAR, IFeatureConfig.NO_FEATURE_CONFIG, Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG ) );

        Biomes.PLAINS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.PLAINS );
        Biomes.SUNFLOWER_PLAINS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.PLAINS );

        Biomes.FOREST.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.FOREST );

        Biomes.FLOWER_FOREST.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.FLOWER_FOREST );

        Biomes.BIRCH_FOREST.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.BIRCH_FOREST );
        Biomes.TALL_BIRCH_FOREST.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.BIRCH_FOREST );
        Biomes.BIRCH_FOREST_HILLS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.BIRCH_FOREST );

        Biomes.TAIGA.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.TAIGA );
        Biomes.TAIGA_HILLS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.TAIGA );
        Biomes.GIANT_SPRUCE_TAIGA.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.TAIGA );
        Biomes.GIANT_TREE_TAIGA.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.TAIGA );
        Biomes.GIANT_SPRUCE_TAIGA_HILLS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.TAIGA );
        Biomes.GIANT_TREE_TAIGA_HILLS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.TAIGA );

        Biomes.SNOWY_TAIGA.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.SNOW_TAIGA );
        Biomes.SNOWY_TAIGA_HILLS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.SNOW_TAIGA );

        Biomes.SNOWY_TUNDRA.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.TUNDRA );

        Biomes.SAVANNA.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.SAVANNA );
        Biomes.SAVANNA_PLATEAU.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.SAVANNA );
        Biomes.SHATTERED_SAVANNA.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.SAVANNA );
        Biomes.SHATTERED_SAVANNA_PLATEAU.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.SAVANNA );

        Biomes.DESERT.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DESERT );
        Biomes.DESERT_LAKES.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DESERT );
        Biomes.DESERT_HILLS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DESERT );

        Biomes.BADLANDS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.MESA );
        Biomes.BADLANDS_PLATEAU.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.MESA );
        Biomes.ERODED_BADLANDS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.MESA );
        Biomes.MODIFIED_BADLANDS_PLATEAU.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.MESA );
        Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.MESA );
        Biomes.WOODED_BADLANDS_PLATEAU.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.MESA );

        Biomes.DARK_FOREST.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DARK_FOREST );
        Biomes.DARK_FOREST_HILLS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DARK_FOREST );

        Biomes.JUNGLE.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.JUNGLE );
        Biomes.JUNGLE_EDGE.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.JUNGLE );
        Biomes.JUNGLE_HILLS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.JUNGLE );
        Biomes.MODIFIED_JUNGLE.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.JUNGLE );
        Biomes.MODIFIED_JUNGLE_EDGE.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.JUNGLE );

        Biomes.SWAMP.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.SWAMP );
        Biomes.SWAMP_HILLS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.SWAMP );
    }

    /**
     * Called when the modernity mod class is constructed, to create event listeners.
     */
    public void registerListeners() {
        MinecraftForge.EVENT_BUS.register( new CapabilityHandler() );
        MinecraftForge.EVENT_BUS.register( new StructureHandler() );
    }

    public boolean fancyGraphics() {
        return false;
    }

    @SubscribeEvent
    public void serverStart( FMLServerStartingEvent event ) {
        MDCommands.register( event.getCommandDispatcher() );
        lootManager = event.getServer().getLootTableManager();
    }

    @SubscribeEvent
    public void onRegisterDimensions( RegisterDimensionsEvent event ) {
        MDDimensions.init( event.getMissingNames() );
    }

    public void openContainer( EntityPlayer player, IInventory inventory ) {
        if( player instanceof EntityPlayerMP ) {
            ContainerManager.openContainerMP( (EntityPlayerMP) player, inventory );
        }
    }

    public LootTableManager getLootTableManager() {
        return lootManager;
    }
}
