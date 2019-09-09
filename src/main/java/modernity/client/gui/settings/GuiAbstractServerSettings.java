/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.client.gui.settings;

import modernity.common.settings.LocalServerSettings;
import modernity.common.settings.ServerSettings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public abstract class GuiAbstractServerSettings extends GuiSettings<ServerSettings> {
    protected final GuiButton revertDefaultButton = new GuiButton( 0, 0, 0, 150, 20, I18n.format( "gui.modernity.settings.revertdefault" ) ) {
        @Override
        public void onClick( double x, double y ) {
            ( (LocalServerSettings) getSettings() ).revertDefault();
            updateAllSettings();
        }
    };


    public GuiAbstractServerSettings( GuiScreen lastScreen, ServerSettings settings ) {
        super( lastScreen, settings );
    }

    @Override
    public void renderTooltips( int x, int y ) {
        if( revertDefaultButton.visible && revertDefaultButton.isMouseOver() ) {
            drawHoveringText( I18n.format( "gui.modernity.settings.revertdefault.desc" ), x, y );
        } else {
            super.renderTooltips( x, y );
        }
    }

    @Override
    protected void initGui() {
        super.initGui();

        if( getSettings() instanceof LocalServerSettings ) {
            doneButton.width = 150;
            doneButton.x = width / 2 - 155;
            doneButton.y = height - 60;

            addButton( revertDefaultButton );
            revertDefaultButton.x = width / 2 + 5;
            revertDefaultButton.y = height - 60;
        } else {
            doneButton.width = 200;
            doneButton.x = width / 2 - 100;
            doneButton.y = height - 60;
        }
    }
}
