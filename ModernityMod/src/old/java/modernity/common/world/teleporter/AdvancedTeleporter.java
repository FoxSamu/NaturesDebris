/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.world.teleporter;

// TODO Re-evaluate as Forge re-added ITeleporter again
//public abstract class AdvancedTeleporter extends Teleporter {
//    public AdvancedTeleporter( ServerWorld world ) {
//        super( world );
//    }
//
//    @Override
//    public final boolean func_222268_a( Entity e, float rotYaw ) {
//        return tryPlaceInPortal( e, rotYaw );
//    }
//
//    public boolean tryPlaceInPortal( Entity entity, float rotationYaw ) {
//        return super.func_222268_a( entity, rotationYaw );
//    }
//
//    @Nullable
//    @Override
//    public final BlockPattern.PortalInfo func_222272_a( BlockPos pos, Vec3d motion, Direction portalDir, double portalX, double portalY, boolean isPlayer ) {
//        PortalInfo info = determineLocation( pos, motion, portalDir, portalX, portalY, isPlayer );
//        return info == null ? null : info.toVanilla();
//    }
//
//    public abstract PortalInfo determineLocation( BlockPos pos, Vec3d motion, Direction teleportDir, double lastPortalX, double lastPortalY, boolean isPlayer );
//
//    @Override
//    public abstract boolean makePortal( Entity entity );
//
//
//    public static class PortalInfo {
//        public final Vec3d teleportLocation;
//        public final Vec3d motion;
//        public final int additionalRotation;
//
//        public PortalInfo( Vec3d teleportLocation, Vec3d motion, int additionalRotation ) {
//            this.teleportLocation = teleportLocation;
//            this.motion = motion;
//            this.additionalRotation = additionalRotation;
//        }
//
//        public BlockPattern.PortalInfo toVanilla() {
//            return new BlockPattern.PortalInfo( teleportLocation, motion, additionalRotation );
//        }
//    }
//}
