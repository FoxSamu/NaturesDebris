/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.blockold.dispensing;

import natures.debris.common.itemold.base.ThrowableItem;
import natures.debris.generic.util.Events;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class ThrownEntityDispenseBehavior extends DefaultDispenseItemBehavior {
    private final ThrowableItem item;

    public ThrownEntityDispenseBehavior(ThrowableItem item) {
        this.item = item;
    }

    @Override
    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        World world = source.getWorld();

        IPosition pos = DispenserBlock.getDispensePosition(source);
        Direction dir = source.getBlockState().get(DispenserBlock.FACING);

        item.dispense(
            world,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            dir,
            stack.split(1)
        );

        return stack;
    }

    @Override
    protected void playDispenseSound(IBlockSource source) {
        source.getWorld().playEvent(Events.DISPENSER_LAUNCH, source.getBlockPos(), 0);
    }
}
