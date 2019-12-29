/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 29 - 2019
 * Author: rgsw
 */

package modernity.common.handler;

import modernity.api.container.IPostOpenHandler;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public enum ContainerHandler {
    INSTANCE;

    @SubscribeEvent
    public void onOpenContainer( PlayerContainerEvent.Open event ) {
        if( event.getContainer() instanceof IPostOpenHandler ) {
            ( (IPostOpenHandler) event.getContainer() ).onOpened();
        }
    }

}
