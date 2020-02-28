/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 28 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.growing;

import modernity.common.block.farmland.IFarmlandLogic;
import modernity.common.block.plant.PlantBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Predicate;

public class SpreadingGrowLogic extends AbstractSpreadingGrowLogic {
    private final PlantBlock plant;
    private final IResourcePredicate predicate;
    private final IResourceConsumer consumer;
    private final Predicate<ItemStack> fertilizer;

    public SpreadingGrowLogic( PlantBlock plant, IResourcePredicate predicate, IResourceConsumer consumer, Predicate<ItemStack> fertilizer ) {
        this.plant = plant;
        this.predicate = predicate;
        this.consumer = consumer;
        this.fertilizer = fertilizer;
    }

    public SpreadingGrowLogic( PlantBlock plant, IPlantResources res ) {
        this( plant, res, res, res );
    }

    @Override
    protected boolean isFertilizer( ItemStack item ) {
        return fertilizer.test( item );
    }

    @Override
    protected boolean checkResources( IFarmlandLogic logic, Random rand ) {
        return predicate.checkResources( logic, rand );
    }

    @Override
    protected void consumeResources( IFarmlandLogic logic, Random rand ) {
        consumer.consumeResources( logic, rand );
    }

    @Override
    protected boolean canPlacePlant( World world, BlockPos pos, Random rand ) {
        return plant.canGenerateAt( world, pos, world.getBlockState( pos ) );
    }

    @Override
    protected void placePlant( World world, BlockPos pos, Random rand ) {
        plant.growAt( world, pos );
    }
}
