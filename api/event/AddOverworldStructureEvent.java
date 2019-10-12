/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 3 - 2019
 */

package modernity.api.event;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.eventbus.api.Event;

public class AddOverworldStructureEvent extends Event {
    private final Biome biome;

    public AddOverworldStructureEvent( Biome biome ) {
        this.biome = biome;
    }

    public <T extends IFeatureConfig> void registerStructure( GenerationStage.Decoration decorationStage, Structure<T> structure, T config ) {
        biome.addFeature( decorationStage, Biome.createDecoratedFeature( structure, config, Placement.CHANCE_PASSTHROUGH, new ChanceConfig( 1 ) ) );
    }
}
