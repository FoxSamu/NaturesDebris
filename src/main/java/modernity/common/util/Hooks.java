/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 3 - 2019
 */

package modernity.common.util;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraftforge.common.MinecraftForge;

import modernity.api.event.AddOverworldStructureEvent;
import modernity.api.event.OverworldStructureEvent;

public class Hooks {
    public static void registerCustomBiomeStructures( Biome biome ) {
        MinecraftForge.EVENT_BUS.post( new AddOverworldStructureEvent( biome ) );
        if( canGenerate( OverworldStructureEvent.Type.MINESHAFT ) ) {
            biome.addFeature( GenerationStage.Decoration.UNDERGROUND_STRUCTURES, Biome.createCompositeFeature( Feature.MINESHAFT, new MineshaftConfig( (double) 0.004F, MineshaftStructure.Type.NORMAL ), Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG ) );
        }
        if( canGenerate( OverworldStructureEvent.Type.VILLAGE ) ) {
            biome.addFeature( GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createCompositeFeature( Feature.VILLAGE, new VillageConfig( 0, VillagePieces.Type.OAK ), Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG ) );
        }
        if( canGenerate( OverworldStructureEvent.Type.STRONGHOLD ) ) {
            biome.addFeature( GenerationStage.Decoration.UNDERGROUND_STRUCTURES, Biome.createCompositeFeature( Feature.STRONGHOLD, new StrongholdConfig(), Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG ) );
        }
        if( canGenerate( OverworldStructureEvent.Type.SWAMP_HUT ) ) {
            biome.addFeature( GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createCompositeFeature( Feature.SWAMP_HUT, new SwampHutConfig(), Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG ) );
        }
        if( canGenerate( OverworldStructureEvent.Type.DESERT_PYRAMID ) ) {
            biome.addFeature( GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createCompositeFeature( Feature.DESERT_PYRAMID, new DesertPyramidConfig(), Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG ) );
        }
        if( canGenerate( OverworldStructureEvent.Type.JUNGLE_TEMPLE ) ) {
            biome.addFeature( GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createCompositeFeature( Feature.JUNGLE_PYRAMID, new JunglePyramidConfig(), Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG ) );
        }
        if( canGenerate( OverworldStructureEvent.Type.IGLOO ) ) {
            biome.addFeature( GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createCompositeFeature( Feature.IGLOO, new IglooConfig(), Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG ) );
        }
        if( canGenerate( OverworldStructureEvent.Type.SHIPWRECK ) ) {
            biome.addFeature( GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createCompositeFeature( Feature.SHIPWRECK, new ShipwreckConfig( false ), Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG ) );
        }
        if( canGenerate( OverworldStructureEvent.Type.OCEAN_MONUMENT ) ) {
            biome.addFeature( GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createCompositeFeature( Feature.OCEAN_MONUMENT, new OceanMonumentConfig(), Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG ) );
        }
        if( canGenerate( OverworldStructureEvent.Type.WOODLAND_MANSION ) ) {
            biome.addFeature( GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createCompositeFeature( Feature.WOODLAND_MANSION, new WoodlandMansionConfig(), Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG ) );
        }
        if( canGenerate( OverworldStructureEvent.Type.OCEAN_RUIN ) ) {
            biome.addFeature( GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createCompositeFeature( Feature.OCEAN_RUIN, new OceanRuinConfig( OceanRuinStructure.Type.COLD, 0.3F, 0.9F ), Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG ) );
        }
        if( canGenerate( OverworldStructureEvent.Type.BURIED_TREASURE ) ) {
            biome.addFeature( GenerationStage.Decoration.UNDERGROUND_STRUCTURES, Biome.createCompositeFeature( Feature.BURIED_TREASURE, new BuriedTreasureConfig( 0.01F ), Biome.PASSTHROUGH, IPlacementConfig.NO_PLACEMENT_CONFIG ) );
        }
    }

    private static boolean canGenerate( OverworldStructureEvent.Type type ) {
        return ! MinecraftForge.EVENT_BUS.post( new OverworldStructureEvent( type ) );
    }
}
