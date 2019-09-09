/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.client.gui.settings;

import modernity.client.gui.wiki.GuiWikiUtil;
import modernity.client.util.ProxyClient;
import modernity.common.settings.ServerSettings;
import modernity.common.settings.core.Settings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiSettingsHome extends GuiSettings<Settings> {
    private final GuiButton clientButton = new GuiButton( 0, 0, 0, I18n.format( "settings.modernity.type.client" ) ) {
        @Override
        public void onClick( double x, double y ) {
            mc.displayGuiScreen( new GuiClientSettings( GuiSettingsHome.this, ProxyClient.get().getClientSettings() ) );
        }
    };

    private final GuiButton defaultServerButton = new GuiButton( 0, 0, 0, I18n.format( "settings.modernity.type.server_default" ) ) {
        @Override
        public void onClick( double x, double y ) {
            mc.displayGuiScreen( new GuiServerSettings( GuiSettingsHome.this, ProxyClient.get().getDefaultServerSettings() ) );
        }
    };

    private final GuiButton localServerButton = new GuiButton( 0, 0, 0, I18n.format( "settings.modernity.type.server_local" ) ) {
        @Override
        public void onClick( double x, double y ) {
            mc.displayGuiScreen( new GuiServerSettings( GuiSettingsHome.this, ProxyClient.get().getServerSettings() ) );
        }
    };

    private final GuiButton wikiButton = new GuiButton( 0, 0, 0, I18n.format( "settings.modernity.type.wiki" ) ) {
        @Override
        public void onClick( double x, double y ) {
            mc.displayGuiScreen( new GuiWikiUtil( GuiSettingsHome.this ) );
        }
    };

    public GuiSettingsHome( GuiScreen lastScreen ) {
        super( lastScreen, null );
    }

    @Override
    protected void initGui() {
        super.initGui();

        addButton( clientButton );
        addButton( defaultServerButton );
        addButton( localServerButton );
        addButton( wikiButton );

        int yoffset = buttons.size() * 12;

        clientButton.x = width / 2 - 100;
        clientButton.y = height / 2 - yoffset;

        defaultServerButton.x = width / 2 - 100;
        defaultServerButton.y = height / 2 - yoffset + 24;

        localServerButton.x = width / 2 - 100;
        localServerButton.y = height / 2 - yoffset + 48;
        localServerButton.enabled = ProxyClient.get().getServerSettings() != null;

        wikiButton.x = width / 2 - 100;
        wikiButton.y = height / 2 - yoffset + 72;
    }

    @Override
    public void flushSettings() {
        ProxyClient.get().getClientSettings().save( true );
        ProxyClient.get().getDefaultServerSettings().save( true );
        ServerSettings settings = ProxyClient.get().getServerSettings();
        if( settings != null ) {
            settings.save( true );
        }
    }
}
