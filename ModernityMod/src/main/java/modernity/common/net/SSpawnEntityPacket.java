/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 22 - 2020
 * Author: rgsw
 */

package modernity.common.net;

import modernity.common.entity.FallBlockEntity;
import modernity.common.entity.GooBallEntity;
import modernity.common.entity.MDEntityTypes;
import modernity.common.entity.ShadeBallEntity;
import modernity.network.Packet;
import modernity.network.ProcessContext;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

/**
 * Imitation of {@link SSpawnObjectPacket} so that we can spawn our own entities...
 */
public class SSpawnEntityPacket implements Packet {
    private int entityId;
    private UUID uniqueId;
    private double x;
    private double y;
    private double z;
    private int speedX;
    private int speedY;
    private int speedZ;
    private int pitch;
    private int yaw;
    private EntityType<?> type;
    private int data;


    public SSpawnEntityPacket() {
    }

    public SSpawnEntityPacket( int entityID, UUID uuid, double posX, double posY, double posZ, float pitch, float yaw, EntityType<?> type, int data, Vec3d motion ) {
        this.entityId = entityID;
        this.uniqueId = uuid;
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.pitch = MathHelper.floor( pitch * 256 / 360 );
        this.yaw = MathHelper.floor( yaw * 256 / 360 );
        this.type = type;
        this.data = data;
        this.speedX = (int) ( MathHelper.clamp( motion.x, - 3.9, 3.9 ) * 8000 );
        this.speedY = (int) ( MathHelper.clamp( motion.y, - 3.9, 3.9 ) * 8000 );
        this.speedZ = (int) ( MathHelper.clamp( motion.z, - 3.9, 3.9 ) * 8000 );
    }

    public SSpawnEntityPacket( Entity entity ) {
        this( entity, 0 );
    }

    public SSpawnEntityPacket( Entity entity, int data ) {
        this( entity.getEntityId(), entity.getUniqueID(), entity.getPosX(), entity.getPosY(), entity.getPosZ(), entity.rotationPitch, entity.rotationYaw, entity.getType(), data, entity.getMotion() );
    }

    public SSpawnEntityPacket( Entity entity, EntityType<?> type, int data, BlockPos pos ) {
        this( entity.getEntityId(), entity.getUniqueID(), pos.getX(), pos.getY(), pos.getZ(), entity.rotationPitch, entity.rotationYaw, type, data, entity.getMotion() );
    }

    @Override
    public void write( PacketBuffer buf ) {
        buf.writeVarInt( this.entityId );
        buf.writeUniqueId( this.uniqueId );
        buf.writeVarInt( Registry.ENTITY_TYPE.getId( this.type ) );
        buf.writeDouble( this.x );
        buf.writeDouble( this.y );
        buf.writeDouble( this.z );
        buf.writeByte( this.pitch );
        buf.writeByte( this.yaw );
        buf.writeInt( this.data );
        buf.writeShort( this.speedX );
        buf.writeShort( this.speedY );
        buf.writeShort( this.speedZ );
    }

    @Override
    public void read( PacketBuffer buf ) {
        this.entityId = buf.readVarInt();
        this.uniqueId = buf.readUniqueId();
        this.type = Registry.ENTITY_TYPE.getByValue( buf.readVarInt() );
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.pitch = buf.readByte();
        this.yaw = buf.readByte();
        this.data = buf.readInt();
        this.speedX = buf.readShort();
        this.speedY = buf.readShort();
        this.speedZ = buf.readShort();
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void process( ProcessContext ctx ) {
        ctx.ensureMainThread();
        Minecraft mc = Minecraft.getInstance();

        Entity entity;
        if( type == MDEntityTypes.FALL_BLOCK ) {
            entity = new FallBlockEntity( mc.world, x, y, z, Block.getStateById( data ) );
        } else if( type == MDEntityTypes.GOO_BALL ) {
            if( data == 0 ) entity = new GooBallEntity( x, y, z, mc.world );
            else entity = new GooBallEntity( x, y, z, mc.world ).setPoisonous();
        } else if( type == MDEntityTypes.SHADE_BALL ) {
            entity = new ShadeBallEntity( x, y, z, mc.world );
        } else {
            entity = type.create( mc.world );
            if( entity != null ) {
                entity.setPosition( x, y, z );
                entity.setMotion( speedX, speedY, speedZ );
            }
        }

        if( entity != null ) {
            entity.setRawPosition( x, y, z );
            entity.rotationPitch = (float) ( pitch * 360 ) / 256F;
            entity.rotationYaw = (float) ( yaw * 360 ) / 256F;
            entity.setEntityId( entityId );
            entity.setUniqueId( uniqueId );
            mc.world.addEntity( entityId, entity );
        }
    }
}
