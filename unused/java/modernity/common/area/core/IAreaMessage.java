/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.area.core;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IAreaMessage<T extends MessagingArea<T>> {
    void write( PacketBuffer buf );

    @OnlyIn( Dist.CLIENT )
    void read( PacketBuffer buf );

    @OnlyIn( Dist.CLIENT )
    void receive( T area );
}
