/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 24 - 2020
 * Author: rgsw
 */

package modernity.generic.dimension;

import modernity.generic.event.SoundEffectEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ISoundEffectDimension {
    @OnlyIn( Dist.CLIENT )
    void handleSoundEffect( SoundEffectEvent event );

    @OnlyIn( Dist.CLIENT )
    void cleanupSoundEffects();
}
