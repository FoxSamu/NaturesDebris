/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 5 - 2019
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

    /**
     * Called on init phase. This is when FML fires the {@link FMLCommonSetupEvent}.
     */
    public void init() {
        Hooks.setupBiomeStructures();
        MDStructures.register();
        MDPackets.register();
    }

    /**
     * Called when loading completed. This is when FML fires the {@link FMLLoadCompleteEvent}.
     */
    public void loadComplete() {
        Biomes.NETHER.addStructure( MDStructures.NETHER_ALTAR, IFeatureConfig.NO_FEATURE_CONFIG );
        Biomes.NETHER.addFeature( GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createCompositeFeature( MDStructures.NETHER_ALTAR, IFeatureConfig.NO_FEATURE_CONFIG, Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG ) );

        Biomes.PLAINS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.SUNFLOWER_PLAINS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.FOREST.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.FLOWER_FOREST.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.BIRCH_FOREST.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.TALL_BIRCH_FOREST.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.BIRCH_FOREST_HILLS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.TAIGA.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.TAIGA_HILLS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.GIANT_SPRUCE_TAIGA.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.GIANT_TREE_TAIGA.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.SNOWY_TAIGA.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.GIANT_SPRUCE_TAIGA_HILLS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.GIANT_TREE_TAIGA_HILLS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.SNOWY_TAIGA_HILLS.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
        Biomes.SNOWY_TUNDRA.addStructure( MDStructures.CURSE_RUIN, CurseRuinStructure.Type.DEFAULT );
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
}
