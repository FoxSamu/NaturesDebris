package natures.debris.common.util;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public final class Hooks {
    public static void onSendPath(World world, MobEntity ent, @Nullable Path path, float maxDist) {
        if (world instanceof ServerWorld && path != null) {
            PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
            buf.writeInt(ent.getEntityId());
            buf.writeFloat(maxDist);
            buf.writeBoolean(path.func_224771_h());
            buf.writeInt(path.getCurrentPathIndex());
            buf.writeInt(0); // Unused flaggedPathPoints field, let's assume it's empty...
            BlockPos target = path.func_224770_k();
            buf.writeInt(target.getX());
            buf.writeInt(target.getY());
            buf.writeInt(target.getZ());
            List<PathPoint> points = path.func_215746_d();
            buf.writeInt(points.size());
            for (PathPoint pt : points) {
                writePathPoint(pt, buf);
            }
            writePathPointSet(path.getOpenSet(), buf);
            writePathPointSet(path.getClosedSet(), buf);
            send((ServerWorld) world, buf, SCustomPayloadPlayPacket.DEBUG_PATH);
        }
    }

    private static void writePathPoint(PathPoint pt, PacketBuffer buf) {
        buf.writeInt(pt.x);
        buf.writeInt(pt.y);
        buf.writeInt(pt.z);
        buf.writeFloat(pt.field_222861_j);
        buf.writeFloat(pt.costMalus);
        buf.writeBoolean(pt.visited);
        buf.writeInt(pt.nodeType.ordinal());
        buf.writeFloat(pt.distanceToTarget);
    }

    private static void writePathPointSet(PathPoint[] pts, PacketBuffer buf) {
        buf.writeInt(pts.length);
        for (PathPoint pt : pts) {
            writePathPoint(pt, buf);
        }
    }

    private static void send(ServerWorld world, PacketBuffer buf, ResourceLocation id) {
        IPacket<?> ipacket = new SCustomPayloadPlayPacket(id, buf);

        for (PlayerEntity playerentity : world.getPlayers()) {
            ((ServerPlayerEntity) playerentity).connection.sendPacket(ipacket);
        }
    }
}
