/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.plant;

import modernity.common.blockold.MDBlockStateProperties;
import modernity.common.blockold.plant.growing.ShadeBlueGrowLogic;
import modernity.common.entity.MDEntityTags;
import modernity.common.event.MDBlockEvents;
import modernity.common.particle.MDParticleTypes;
import modernity.generic.util.BlockUpdates;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class ShadeBlueBlock extends SimplePlantBlock {
    public static final BooleanProperty ACTIVE = MDBlockStateProperties.ACTIVE;

    public ShadeBlueBlock(Properties properties) {
        super(properties, makePlantShape(15, 10));
        setGrowLogic(new ShadeBlueGrowLogic(this));

        setDefaultState(stateContainer.getBaseState().with(ACTIVE, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(ACTIVE));
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        return false;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(world, pos, state, player);
        List<Entity> nearEntities = world.getEntitiesInAABBexcluding(
            null,
            new AxisAlignedBB(
                pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2,
                pos.getX() + 3, pos.getY() + 3, pos.getZ() + 3
            ),
            null
        );

        if(!nearEntities.contains(player)) nearEntities.add(player);

        for(Entity entity : nearEntities) teleportEntity(world, pos, entity);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        List<Entity> nearEntities = world.getEntitiesInAABBexcluding(
            null,
            new AxisAlignedBB(
                pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2,
                pos.getX() + 3, pos.getY() + 3, pos.getZ() + 3
            ),
            null
        );

        for(Entity entity : nearEntities) teleportEntity(world, pos, entity);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        teleportEntity(world, pos, player);
        return ActionResultType.SUCCESS;
    }

    private void teleportEntity(World world, BlockPos pos, Entity entity) {
        if(world.isRemote) return;
        if(entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.isFlying) return;
        if(entity.getType().isContained(MDEntityTags.SHADE_BLUE_IMMUNE)) return;
        MovingBlockPos mpos = new MovingBlockPos();
        for(int i = 0; i < 20; i++) {
            int xoff = world.rand.nextInt(17) - 8;
            int yoff = world.rand.nextInt(17) - 8;
            int zoff = world.rand.nextInt(17) - 8;

            mpos.setPos(pos).addPos(xoff, yoff, zoff);
            if(canTeleportTo(world, mpos, entity)) {
                entity.setPositionAndUpdate(mpos.getX() + 0.5, mpos.getY(), mpos.getZ() + 0.5);
                entity.setMotion(0, 0, 0);

                MDBlockEvents.SHADE_BLUE_TELEPORT.play(world, mpos);
                break;
            }
        }
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        super.animateTick(state, world, pos, rand);
        if(rand.nextDouble() < 0.4) {
            world.addParticle(
                MDParticleTypes.SHADE,
                pos.getX() + world.rand.nextDouble(),
                pos.getY() + world.rand.nextDouble() * 0.6,
                pos.getZ() + world.rand.nextDouble(),
                (world.rand.nextDouble() - world.rand.nextDouble()) * 0.005,
                (world.rand.nextDouble() - world.rand.nextDouble()) * 0.005,
                (world.rand.nextDouble() - world.rand.nextDouble()) * 0.005
            );
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        List<Entity> nearEntities = world.getEntitiesInAABBexcluding(
            null,
            new AxisAlignedBB(
                pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2,
                pos.getX() + 3, pos.getY() + 3, pos.getZ() + 3
            ),
            null
        );

        for(Entity entity : nearEntities) teleportEntity(world, pos, entity);

        world.setBlockState(pos, state.with(ACTIVE, false), BlockUpdates.NO_RENDER);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if(!world.isRemote) {
            if(!state.get(ACTIVE)) {
                world.setBlockState(pos, state.with(ACTIVE, true), BlockUpdates.NO_RENDER);
                world.getPendingBlockTicks().scheduleTick(pos, this, 0);
            }
        }
//        teleportEntity( world, pos, entity );
    }

    private boolean canTeleportTo(World world, BlockPos pos, Entity entity) {
        float wr = entity.getWidth() / 2;

        int lowX = MathHelper.floor(pos.getX() + 0.5 - wr);
        int lowY = pos.getY();
        int lowZ = MathHelper.floor(pos.getZ() + 0.5 - wr);

        int uppX = MathHelper.ceil(pos.getX() + 0.5 + wr);
        int uppY = MathHelper.ceil(pos.getY() + entity.getHeight()) + 1;
        int uppZ = MathHelper.ceil(pos.getZ() + 0.5 + wr);

        MovingBlockPos mpos = new MovingBlockPos();

        for(int x = lowX; x <= uppX; x++) {
            for(int z = lowZ; z <= uppZ; z++) {
                for(int y = lowY; y <= uppY; y++) {
                    mpos.setPos(x, y, z);

                    BlockState state = world.getBlockState(mpos);
                    if(!state.getCollisionShape(world, mpos).isEmpty()) {
                        return false;
                    }

                    if(!state.getFluidState().isEmpty()) {
                        return false;
                    }
                }
            }
        }

        mpos.setPos(pos).moveDown();

        BlockState state = world.getBlockState(mpos);
        return isBlockSideSustainable(state, world, mpos, Direction.UP);
    }
}
