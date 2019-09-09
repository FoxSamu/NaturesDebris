/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.client.gui.settings;

import modernity.common.settings.ClientSettings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiClientSettings extends GuiSettings<ClientSettings> {
    private final GuiButton renderingButton = new GuiButton( 0, 0, 0, I18n.format( "settings.modernity.category.rendering" ) ) {
        @Override
        public void onClick( double x, double y ) {
            mc.displayGuiScreen( new GuiRenderingSettings( GuiClientSettings.this, getSettings() ) );
        }
    };

    public GuiClientSettings( GuiScreen lastScreen, ClientSettings settings ) {
        super( lastScreen, settings );
    }

    @Override
    protected void initGui() {
        super.initGui();
        addButton( renderingButton );
        renderingButton.x = width / 2 - 100;
        renderingButton.y = 60;
    }
}
