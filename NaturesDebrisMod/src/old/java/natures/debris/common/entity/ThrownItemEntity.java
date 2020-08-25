/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public abstract class ThrownItemEntity extends ProjectileItemEntity {

    public ThrownItemEntity(EntityType<? extends ProjectileItemEntity> type, World world) {
        super(type, world);
    }

    public ThrownItemEntity(EntityType<? extends ProjectileItemEntity> type, double x, double y, double z, World world) {
        super(type, x, y, z, world);
    }

    public ThrownItemEntity(EntityType<? extends ProjectileItemEntity> type, LivingEntity ent, World world) {
        super(type, ent, world);
    }

    @Override
    protected Item getDefaultItem() {
        return getThrownItem();
    }

    protected abstract Item getThrownItem();

    public void setItemStack(ItemStack stack) {
        setItem(stack);
    }

    protected ItemStack getItemStack() {
        return func_213882_k();
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult) result).getEntity();
            int i = entity instanceof BlazeEntity ? 3 : 0;
            entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) i);
        }

        if (!world.isRemote) {
            world.setEntityState(this, (byte) 3);
            remove();
        }
    }
}
