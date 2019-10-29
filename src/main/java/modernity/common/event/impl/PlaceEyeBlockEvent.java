package modernity.common.event.impl;

import modernity.common.event.SimpleBlockEvent;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceEyeBlockEvent extends SimpleBlockEvent {
    @Override
    public void playEvent( World world, BlockPos pos, Void data ) {
        world.playSound(
            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
            SoundEvents.BLOCK_END_PORTAL_FRAME_FILL,
            SoundCategory.BLOCKS,
            1, 1, true
        );

        for( int i = 0; i < 16; ++ i ) {
            double x = pos.getX() + ( 5 + world.rand.nextDouble() * 6 ) / 16;
            double y = pos.getY() + 0.625;
            double z = pos.getZ() + ( 5 + world.rand.nextDouble() * 6 ) / 16;
            world.addParticle( ParticleTypes.SMOKE, x, y, z, 0, 0, 0 );
        }
    }
}
