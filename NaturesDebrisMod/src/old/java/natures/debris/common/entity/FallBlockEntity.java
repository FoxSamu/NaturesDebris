/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.entity;

import com.google.common.collect.Lists;
import com.sun.javafx.geom.Vec3d;
import natures.debris.common.blockold.base.FallBlock;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;

/**
 * Represents a falling block entity.
 */
public class FallBlockEntity extends Entity {
    protected static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(FallBlockEntity.class, DataSerializers.BLOCK_POS);
    public int fallTime;
    public boolean shouldDropItem = true;
    public CompoundNBT tileEntityData;
    private boolean dontSetBlock;
    private boolean hurtEntities;
    private Tag<Fluid> floatIn;
    private int fallHurtMax = 40;
    private float gravityScale = 1;
    private float fallHurtAmount = 2.0F;
    private BlockState fallingBlock;

    public FallBlockEntity(EntityType type, World world) {
        super(type, world);
    }

    public FallBlockEntity(World world, double x, double y, double z, BlockState fallingBlockState) {
        this(MDEntityTypes.FALL_BLOCK, world);

        fallingBlock = fallingBlockState;
        preventEntitySpawning = true;

        setPosition(x, y + (1 - getHeight()) / 2, z);
        setMotion(0, 0, 0);

        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;

        setOrigin(new BlockPos(this));
    }

    /**
     * Returns the tag this falling block should float in. Returns null when this block doesn't float.
     */
    public Tag<Fluid> getFloatIn() {
        return floatIn;
    }

    /**
     * Sets the tag this falling block should float in. When null, this block does not float.
     */
    public void setFloatIn(Tag<Fluid> floatIn) {
        this.floatIn = floatIn;
    }

    /**
     * Returns the gravity scale of the block.
     */
    public float getGravityScale() {
        return gravityScale;
    }

    /**
     * Sets the gravity scale of the block. When negative, it falls upwards.
     */
    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    /**
     * Returns the origin pos of this falling block.
     */
    public BlockPos getOrigin() {
        return dataManager.get(ORIGIN);
    }

    /**
     * Sets the origin pos of this falling block.
     */
    public void setOrigin(BlockPos origin) {
        dataManager.set(ORIGIN, origin);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void registerData() {
        dataManager.register(ORIGIN, BlockPos.ZERO);
    }

    @Override
    public boolean canBeCollidedWith() {
        return isAlive();
    }

    @Override
    public void tick() {
        BlockState fallTile = getFallingBlock();
        if (fallTile.isAir(world, new BlockPos(this))) {
            remove();
        } else {
            prevPosX = getPosX();
            prevPosY = getPosY();
            prevPosZ = getPosZ();

            Block fallBlock = fallTile.getBlock();

            if (fallTime++ == 0) {
                // Remove when starting to fall
                BlockPos currPos = new BlockPos(this);
                if (world.getBlockState(currPos).getBlock() == fallBlock) {
                    world.removeBlock(currPos, false);
                } else if (!world.isRemote) {
                    remove();
                    return;
                }
            }

            // Apply gravity
            if (!hasNoGravity()) {
                setMotion(getMotion().add(0, -0.04 * gravityScale, 0));
            }

            // Do movement
            this.move(MoverType.SELF, getMotion());

            if (!this.world.isRemote) {
                BlockPos currPos = new BlockPos(this);

                boolean isConcretePowder = fallTile.getBlock() instanceof ConcretePowderBlock;
                boolean makeConcrete = isConcretePowder && world.getFluidState(currPos).isTagged(FluidTags.WATER);
                boolean doesFloat = false;

                // Stop when colliding concrete powder with water
                double speedSq = getMotion().lengthSquared();
                if (isConcretePowder && speedSq > 1.0D) {
                    BlockRayTraceResult rtr = world.rayTraceBlocks(new RayTraceContext(new Vec3d(prevPosX, prevPosY, prevPosZ), getPositionVec(), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.SOURCE_ONLY, this));

                    if (rtr.getType() == RayTraceResult.Type.BLOCK && world.getFluidState(rtr.getPos()).isTagged(FluidTags.WATER)) {
                        currPos = rtr.getPos();
                        makeConcrete = true;
                    }

                    if (floatIn != null && rtr.getType() == RayTraceResult.Type.BLOCK && world.getFluidState(rtr.getPos()).isTagged(floatIn)) {
                        currPos = rtr.getPos();
                        doesFloat = true;
                    }
                }

                boolean floatConcrete = doesFloat || makeConcrete;

                if (!onGround && !floatConcrete) {
                    // We're falling
                    if (fallTime > 100 && !world.isRemote && (currPos.getY() < 1 || currPos.getY() > 256) || fallTime > 600) {
                        if (shouldDropItem && world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                            entityDropItem(fallBlock);
                        }

                        remove();
                    }
                } else {
                    // We're on ground or floating in fluid: try to place our block again

                    BlockState inState = world.getBlockState(currPos);
                    if (world.isAirBlock(new BlockPos(getPosX(), getPosY() - 0.01, getPosZ()))) {
                        if (!floatConcrete && FallBlock.canFallThrough(world.getBlockState(new BlockPos(getPosX(), getPosY() - 0.01, getPosZ())))) {
                            this.onGround = false;
                            return;
                        }
                    }

                    setMotion(getMotion().mul(0.7, -0.5, 0.7));

                    if (inState.getBlock() != Blocks.MOVING_PISTON) {
                        remove();
                        if (!dontSetBlock) {
                            boolean didPlace = inState.getMaterial().isReplaceable(); // Can we place here?
                            didPlace = didPlace && (floatConcrete || !FallBlock.canFallThrough(world.getBlockState(currPos.down()))); // Can't we continue falling?
                            didPlace = didPlace && !ForgeEventFactory.onBlockPlace( // Could we actually place block
                                                                                    this,
                                                                                    new BlockSnapshot(getEntityWorld(), getPosition(), inState),
                                                                                    Direction.UP
                            );
                            didPlace = didPlace && world.setBlockState(currPos, fallTile, 3); // Did we actually place block

                            if (didPlace) {
                                // End falling
                                if (fallBlock instanceof FallBlock) {
                                    ((FallBlock) fallBlock).onEndFalling(world, currPos, fallTile, inState);
                                }

                                // Restore tile entity
                                if (tileEntityData != null && fallTile.hasTileEntity()) {
                                    TileEntity te = world.getTileEntity(currPos);
                                    if (te != null) {
                                        CompoundNBT nbt = te.write(new CompoundNBT());

                                        for (String s : tileEntityData.keySet()) {
                                            INBT inbtbase = tileEntityData.get(s);
                                            if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                                                nbt.put(s, inbtbase.copy());
                                            }
                                        }

                                        te.read(nbt);
                                        te.markDirty();
                                    }
                                }
                            } else if (shouldDropItem && world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                entityDropItem(fallBlock);
                            }
                        } else if (fallBlock instanceof FallBlock) {
                            ((FallBlock) fallBlock).onBroken(world, currPos);
                        }
                    }
                }
            }

            // Air friction
            setMotion(getMotion().mul(0.98, 0.98, 0.98));
        }
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        if (this.hurtEntities) {
            int i = MathHelper.ceil(distance - 1.0F);
            if (i > 0) {
                List<Entity> list = Lists.newArrayList(world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox()));
                boolean flag = getFallingBlock().isIn(BlockTags.ANVIL);
                DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;

                for (Entity entity : list) {
                    entity.attackEntityFrom(damagesource, (float) Math.min(MathHelper.floor((float) i * fallHurtAmount), fallHurtMax));
                }

                if (flag && rand.nextDouble() < 0.05 + i * 0.05D) {
                    BlockState state = AnvilBlock.damage(getFallingBlock());
                    if (state == null) {
                        dontSetBlock = true;
                    } else {
                        setFallingBlock(state);
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.put("BlockState", NBTUtil.writeBlockState(getFallingBlock()));
        compound.putInt("Time", fallTime);
        compound.putBoolean("DropItem", shouldDropItem);
        compound.putBoolean("HurtEntities", hurtEntities);
        compound.putFloat("FallHurtAmount", fallHurtAmount);
        compound.putFloat("GravityScale", gravityScale);
        compound.putString("FloatIn", floatIn == null ? "" : floatIn.getId().toString());
        compound.putInt("FallHurtMax", fallHurtMax);
        if (tileEntityData != null) {
            compound.put("TileEntityData", tileEntityData);
        }

    }

    @Override
    @SuppressWarnings("deprecation")
    protected void readAdditional(CompoundNBT compound) {
        BlockState fallTile = NBTUtil.readBlockState(compound.getCompound("BlockState"));
        fallTime = compound.getInt("Time");
        if (compound.contains("HurtEntities", 99)) {
            hurtEntities = compound.getBoolean("HurtEntities");
            fallHurtAmount = compound.getFloat("FallHurtAmount");
            fallHurtMax = compound.getInt("FallHurtMax");
        } else if (fallTile.isIn(BlockTags.ANVIL)) {
            hurtEntities = true;
        }

        if (compound.contains("DropItem", 99)) {
            shouldDropItem = compound.getBoolean("DropItem");
        }

        if (compound.contains("TileEntityData", 10)) {
            tileEntityData = compound.getCompound("TileEntityData");
        }

        if (compound.contains("GravityScale")) {
            gravityScale = compound.getFloat("GravityScale");
        }

        if (compound.contains("FloatIn")) {
            String s = compound.getString("FloatIn");
            if (!s.isEmpty()) {
                MinecraftServer server = world.getServer();
                if (server != null) {
                    floatIn = server.getNetworkTagManager().getFluids().get(new ResourceLocation(s));
                }
            }
        }

        if (fallTile.isAir()) {
            fallTile = Blocks.SAND.getDefaultState();
        }
        setFallingBlock(fallTile);
    }

    @OnlyIn(Dist.CLIENT)
    public World getWorldObj() {
        return world;
    }

    /**
     * Sets whether this block hurts entities or not.
     */
    public void setHurtEntities(boolean hurt) {
        hurtEntities = hurt;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public void fillCrashReport(CrashReportCategory category) {
        super.fillCrashReport(category);
        category.addDetail("Immitating BlockState", getFallingBlock().toString());
    }

    /**
     * Returns the actual block state that is falling
     */
    public BlockState getFallingBlock() {
        return fallingBlock;
    }

    /**
     * Sets the block state that is falling
     */
    protected void setFallingBlock(BlockState state) {
        fallingBlock = state;
    }

    @Override
    public boolean ignoreItemEntityData() {
        return true;
    }

    @Override
    public IPacket<?> createSpawnPacket() { // TODO Spawn packet
        return null;
//        return Modernity.network().toPlayClientPacket( new SSpawnEntityPacket( this, Block.getStateId( this.getFallingBlock() ) ) );
    }
}
