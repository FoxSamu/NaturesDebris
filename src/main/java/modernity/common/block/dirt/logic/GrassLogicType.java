/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 01 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt.logic;

import modernity.api.block.IReceiveFertilityFromFloorBlock;
import modernity.common.biome.ModernityBiome;
import modernity.common.block.dirt.DirtlikeBlock;
import modernity.common.generator.blocks.IBlockGenerator;
import modernity.common.generator.blocks.MDBlockGenerators;
import modernity.common.item.MDItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class GrassLogicType implements ISpreadingLogicType, IDecayingLogicType {
    @Override
    public IDirtLogicType getDecayed( World world, BlockPos pos, BlockState state ) {
        return MDDirtLogics.DIRT_TYPE;
    }

    @Override
    public boolean canGrowUpon( DirtLogic logic ) {
        return logic.getType() == MDDirtLogics.DIRT_TYPE;
    }

    @Override
    public IDirtLogicType getGrowType( World world, BlockPos pos, BlockState state ) {
        return this;
    }

    @Override
    public boolean allowOnFarmland() {
        return true;
    }

    @Override
    public boolean allowOnNormal() {
        return true;
    }

    @Override
    public boolean canSwitchTo( DirtLogic logic, IDirtLogicType switchTo ) {
        return true;
    }


    @Override
    public boolean canGrow( ItemStack stack ) {
        return stack.getItem().isIn( MDItemTags.FERTILIZER );
    }

    @Override
    public void grow( World world, BlockPos pos, BlockState state, Random rand ) {
        BlockPos up = pos.up();
        IBlockGenerator grass = MDBlockGenerators.MURK_GRASS_1;

        for( int i = 0; i < 128; ++ i ) {
            BlockPos p = up;
            int j = 0;

            while( true ) {
                if( j >= i / 16 ) {
                    BlockState stateAtLoc = world.getBlockState( p );
                    if( stateAtLoc.getBlock() instanceof IReceiveFertilityFromFloorBlock && rand.nextInt( 10 ) == 0 ) {
                        ( (IReceiveFertilityFromFloorBlock) stateAtLoc.getBlock() ).receiveFertilityFromFloor( world, p, stateAtLoc, rand );
                    }

                    if( ! stateAtLoc.isAir( world, p ) ) {
                        break;
                    }

                    IBlockGenerator flower;
                    Biome biome = world.getBiome( p );
                    if( biome instanceof ModernityBiome ) {
                        flower = ( (ModernityBiome) biome ).getRandomPlant( world.getBlockState( p.down() ) );
                    } else {
                        flower = grass;
                    }

                    flower.generateBlock( world, p, rand );
                    break;
                }

                p = p.add( rand.nextInt( 3 ) - 1, ( rand.nextInt( 3 ) - 1 ) * rand.nextInt( 3 ) / 2, rand.nextInt( 3 ) - 1 );
                if( ! ( world.getBlockState( p.down() ).getBlock() instanceof DirtlikeBlock ) || world.getBlockState( p ).isCollisionShapeOpaque( world, p ) ) {
                    break;
                }

                ++ j;
            }
        }
    }
}
