/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.handler;

import modernity.generic.container.IPostOpenHandler;
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
