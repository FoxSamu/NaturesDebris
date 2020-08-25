/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.tileentity;

import modernity.common.blockold.misc.SoulLightColor;
import modernity.common.particle.MDParticleTypes;
import modernity.common.particle.SoulLightParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class SoulLightTileEntity extends TileEntity implements ISidedTickableTileEntity {

    private SoulLightColor color;
    private boolean fades;

    private boolean beingRendered;

    private int baseParticleCounter;
    private int cloudParticleCounter;

    public SoulLightTileEntity() {
        this(SoulLightColor.random(new Random()));
    }

    public SoulLightTileEntity(SoulLightColor color) {
        super(MDTileEntitiyTypes.SOUL_LIGHT);
        this.color = color;
    }

    public SoulLightColor getColor() {
        return color == null ? setColor(SoulLightColor.DEFAULT) : color;
    }

    public SoulLightColor setColor(SoulLightColor color) {
        this.color = color;
        markDirty();
        return color;
    }

    public boolean fades() {
        return fades;
    }

    public void setFades(boolean fades) {
        this.fades = fades;
    }

    public void markBeingRendered() {
        beingRendered = true;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putByte("color", (byte) color.ordinal());
        compound.putBoolean("fades", fades);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        color = SoulLightColor.fromOrdinal(compound.getByte("color"));
        fades = compound.getBoolean("fades");
        super.read(compound);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound());
    }

    @Override
    public void serverTick() {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void clientTick() {
        if(world == null) return;

        if(world.isRemote) {
            double tx = pos.getX() + 0.5;
            double ty = pos.getY() + 0.5;
            double tz = pos.getZ() + 0.5;

            Vec3d projView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
            double dx = pos.getX() + 0.5 - projView.x;
            double dy = pos.getY() + 0.5 - projView.y;
            double dz = pos.getZ() + 0.5 - projView.z;

            double distSq = dx * dx + dy * dy + dz * dz;

            if(fades() && distSq < 100) {
                return;
            }

            int distRandom = MathHelper.floor(distSq / 1024) + 1;

            baseParticleCounter++;
            cloudParticleCounter++;
            if(baseParticleCounter > 3) {
                baseParticleCounter = 0;
                if(beingRendered && world.rand.nextInt(distRandom) == 0) {
                    world.addParticle(
                        new SoulLightParticleData(MDParticleTypes.SOUL_LIGHT, getColor(), fades()),
                        tx, ty, tz,
                        (world.rand.nextDouble() - 0.5) / 200,
                        (world.rand.nextDouble() - 0.5) / 200,
                        (world.rand.nextDouble() - 0.5) / 200
                    );
                }
            }
            if(cloudParticleCounter > 10 && distSq < 4096) {
                cloudParticleCounter = 0;
                if(beingRendered) {
                    world.addParticle(
                        new SoulLightParticleData(MDParticleTypes.SOUL_LIGHT_CLOUD, getColor(), fades()),
                        tx + (world.rand.nextDouble() - 0.5) / 2,
                        ty + (world.rand.nextDouble() - 0.5) / 2,
                        tz + (world.rand.nextDouble() - 0.5) / 2,
                        (world.rand.nextDouble() - 0.5) / 200,
                        (world.rand.nextDouble() - 0.5) / 200,
                        (world.rand.nextDouble() - 0.5) / 200
                    );
                }
            }
            beingRendered = false;
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.add(-5, -5, -5), pos.add(6, 6, 6));
    }
}
