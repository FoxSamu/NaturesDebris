/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.client.handler;

import modernity.client.gui.settings.GuiSettingsHome;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OptionsButtonHandler {
    @SubscribeEvent
    public void onInitGuiPost( GuiScreenEvent.InitGuiEvent.Post event ) {
        if( event.getGui() instanceof GuiOptions ) {
            event.addButton( new GuiButton( 104, event.getGui().width / 2 - 155, event.getGui().height / 6 + 144 - 6, 150, 20, I18n.format( "options.modernity" ) ) {
                public void onClick( double mouseX, double mouseY ) {
                    Minecraft.getInstance().gameSettings.saveOptions();
                    Minecraft.getInstance().displayGuiScreen( new GuiSettingsHome( Minecraft.getInstance().currentScreen ) );
                }
            } );
        }
    }
}
