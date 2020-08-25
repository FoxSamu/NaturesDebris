/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.tileentity;

import natures.debris.common.container.WorkbenchContainer;
import natures.debris.generic.util.MDVoxelShapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class WorkbenchTileEntity extends ContainerTileEntity {
    private static final ITextComponent TITLE = new TranslationTextComponent("container.crafting");

    public WorkbenchTileEntity() {
        super(MDTileEntitiyTypes.WORKBENCH);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TITLE;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        assert world != null;
        return new WorkbenchContainer(id, player, this, IWorldPosCallable.of(world, pos));
    }

    @Override
    public int getSizeInventory() {
        return 9;
    }

    public void dropAll() {
        assert world != null;
        if (!world.isRemote) {
            for (ItemStack stack : stacks) {
                world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
            }
        }
    }

    @Nonnull
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound());
    }

    @Override
    public void onSlotChanged(int index) {
        assert world != null;
        if (!world.isRemote) {
            ServerWorld sw = (ServerWorld) world;
            ChunkManager manager = sw.getChunkProvider().chunkManager;
            Stream<ServerPlayerEntity> players = manager.getTrackingPlayers(new ChunkPos(pos), false);
            players.forEach(player -> player.connection.sendPacket(getUpdatePacket()));
        }
    }

    @Nonnull
    @Override
    public World getWorld() {
        assert world != null;
        return world;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean canRenderItemsOnTop() {
        assert world != null;

        BlockPos up = pos.up();

        BlockState above = world.getBlockState(up);

        // Items can't be in a block
        if (MDVoxelShapes.hitsSideOfCube(above.getCollisionShape(world, up), Direction.DOWN) && above.isSolid()) {
            return false;
        }

        return !Block.doesSideFillSquare(above.getShape(world, up), Direction.DOWN);
    }
}
