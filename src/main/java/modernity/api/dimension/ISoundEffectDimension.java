/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 14 - 2020
 * Author: rgsw
 */

package modernity.api.dimension;

import modernity.api.event.SoundEffectEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@FunctionalInterface
public interface ISoundEffectDimension {
    @OnlyIn( Dist.CLIENT )
    void handleSoundEffect( SoundEffectEvent event );
}
