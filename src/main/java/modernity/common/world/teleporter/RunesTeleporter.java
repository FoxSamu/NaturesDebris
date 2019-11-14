/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.world.teleporter;

import modernity.common.area.ForestRunesArea;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Teleporter;
import net.minecraft.world.server.ServerWorld;

public class RunesTeleporter extends AdvancedTeleporter {
    public RunesTeleporter( ServerWorld world ) {
        super( world );
    }

    @Override
    public PortalInfo determineLocation( BlockPos pos, Vec3d motion, Direction teleportDir, double lastPortalX, double lastPortalY, boolean isPlayer ) {
        System.out.println( "Finding portal" );
        ForestRunesArea area = ForestRunesArea.findAt( world, pos.getX(), pos.getZ() );
        if( area == null ) return null;
        int a = random.nextInt( 6 ) + 3;
        int b = random.nextBoolean() ? 3 : 8;
        boolean flip = random.nextBoolean();

        int x = flip ? b : a;
        int z = flip ? a : b;

        BlockPos placePos = area.getBox().getGlobal( new BlockPos( x, 2, z ) );

        return new PortalInfo(
            new Vec3d( placePos.getX() + 0.5, placePos.getY(), placePos.getZ() + 0.5 ),
            new Vec3d( 0, 0, 0 ),
            random.nextInt( 360 )
        );
    }

    @Override
    public boolean makePortal( Entity entity ) {
        int x = MathHelper.floor( entity.posX ) - 32 + random.nextInt( 64 );
        int z = MathHelper.floor( entity.posZ ) - 32 + random.nextInt( 64 );

        System.out.println( "Making portal" );
        ForestRunesArea.createAt( world, x, z );
        return true;
    }

    public static RunesTeleporter get( ServerWorld world ) {
        for( Teleporter tp : world.customTeleporters ) {
            if( tp instanceof RunesTeleporter ) return (RunesTeleporter) tp;
        }
        RunesTeleporter tp = new RunesTeleporter( world );
        world.customTeleporters.add( tp );
        return tp;
    }
}
