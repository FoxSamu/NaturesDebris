/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 28 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.growing;

import modernity.common.block.farmland.IFarmlandLogic;
import modernity.common.item.MDItemTags;
import net.minecraft.item.ItemStack;

import java.util.Random;
import java.util.function.Predicate;

public interface IPlantResources extends IResourceConsumer, IResourcePredicate, Predicate<ItemStack> {
    static IPlantResources fertile( int minRes, int maxRes ) {
        return new IPlantResources() {
            @Override
            public boolean test( ItemStack stack ) {
                return stack.getItem().isIn( MDItemTags.FERTILIZER )
                           || stack.getItem().isIn( MDItemTags.LITTLE_FERTILIZER ) && Math.random() < 0.3;
            }

            @Override
            public void consumeResources( IFarmlandLogic logic, Random rand ) {
                logic.consumeFertility( minRes, maxRes, rand );
            }

            @Override
            public boolean checkResources( IFarmlandLogic logic, Random rand ) {
                return logic != null && logic.isFertile( minRes );
            }
        };
    }
    static IPlantResources salty( int minRes, int maxRes ) {
        return new IPlantResources() {
            @Override
            public boolean test( ItemStack stack ) {
                return stack.getItem().isIn( MDItemTags.SALTY )
                           || stack.getItem().isIn( MDItemTags.LITTLE_SALTY ) && Math.random() < 0.3;
            }

            @Override
            public void consumeResources( IFarmlandLogic logic, Random rand ) {
                logic.consumeSaltiness( minRes, maxRes, rand );
            }

            @Override
            public boolean checkResources( IFarmlandLogic logic, Random rand ) {
                return logic != null && logic.isSalty( minRes );
            }
        };
    }
    static IPlantResources decay( int minRes, int maxRes ) {
        return new IPlantResources() {
            @Override
            public boolean test( ItemStack stack ) {
                return false;
            }

            @Override
            public void consumeResources( IFarmlandLogic logic, Random rand ) {
                logic.consumeDecay( minRes, maxRes, rand );
            }

            @Override
            public boolean checkResources( IFarmlandLogic logic, Random rand ) {
                return logic != null && logic.isDecayed( minRes );
            }
        };
    }

    default IPlantResources chance( int chance, int over ) {
        IPlantResources wrap = this;
        return new IPlantResources() {
            @Override
            public boolean test( ItemStack itemStack ) {
                return wrap.test( itemStack );
            }

            @Override
            public void consumeResources( IFarmlandLogic logic, Random rand ) {
                wrap.consumeResources( logic, rand );
            }

            @Override
            public boolean checkResources( IFarmlandLogic logic, Random rand ) {
                return wrap.checkResources( logic, rand ) && rand.nextInt( over ) < chance;
            }
        };
    }

    default IPlantResources requireWet() {
        IPlantResources wrap = this;
        return new IPlantResources() {
            @Override
            public boolean test( ItemStack itemStack ) {
                return wrap.test( itemStack );
            }

            @Override
            public void consumeResources( IFarmlandLogic logic, Random rand ) {
                wrap.consumeResources( logic, rand );
            }

            @Override
            public boolean checkResources( IFarmlandLogic logic, Random rand ) {
                return wrap.checkResources( logic, rand ) && logic.isWet();
            }
        };
    }
}
