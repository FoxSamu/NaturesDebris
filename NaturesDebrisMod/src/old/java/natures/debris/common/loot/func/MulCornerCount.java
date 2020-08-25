/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.loot.func;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import natures.debris.common.ModernityOld;
import natures.debris.common.blockold.base.CornerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.LootParameter;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

import java.util.Set;

/**
 * Loot function that multiplies count with the amount of corners in a corner block.
 */
public class MulCornerCount extends LootFunction {
    protected MulCornerCount(ILootCondition[] conditions) {
        super(conditions);
    }

    public static Builder<?> builder() {
        return builder(MulCornerCount::new);
    }

    @Override
    public Set<LootParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootParameters.BLOCK_STATE);
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {
        BlockState state = context.get(LootParameters.BLOCK_STATE);
        if (state == null) return stack;
        if (state.getBlock() instanceof CornerBlock) {
            CornerBlock block = (CornerBlock) state.getBlock();

            int quantity = block.getQuantity(state);
            stack.setCount(stack.getCount() * quantity);
        }
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<MulCornerCount> {

        public Serializer() {
            super(ModernityOld.res("multiply_corner_count"), MulCornerCount.class);
        }

        @Override
        public MulCornerCount deserialize(JsonObject object, JsonDeserializationContext ctx, ILootCondition[] conditions) {
            return new MulCornerCount(conditions);
        }
    }
}
