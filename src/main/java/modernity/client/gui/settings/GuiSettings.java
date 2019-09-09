/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.client.gui.settings;

import modernity.client.gui.settings.controls.GuiSettingControl;
import modernity.common.settings.LocalServerSettings;
import modernity.common.settings.core.Settings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class GuiSettings <S extends Settings> extends GuiScreen {
    private final GuiScreen lastScreen;
    private final S settings;

    protected final ArrayList<GuiSettingControl<?, ?, ?>> controls = new ArrayList<>();

    protected final GuiButton doneButton = new GuiButton( 0, 0, 0, 200, 20, I18n.format( "gui.done" ) ) {
        @Override
        public void onClick( double x, double y ) {
            cancel();
        }
    };

    public GuiSettings( GuiScreen lastScreen, S settings ) {
        this.lastScreen = lastScreen;
        this.settings = settings;
    }

    @Override
    protected void initGui() {
        super.initGui();
        addButton( doneButton );
        doneButton.x = width / 2 - 100;
        doneButton.y = height - 60;

        children.addAll( controls );
    }

    public void cancel() {
        flushSettings();
        mc.displayGuiScreen( lastScreen );
    }

    @Override
    public void close() {
        flushSettings();
        super.close();
    }

    public void flushSettings() {
        settings.flush();
    }

    public S getSettings() {
        return settings;
    }

    private void drawResetTooltip( int x, int y ) {
        drawHoveringText( I18n.format( "gui.modernity.settings.reset.desc" ), x, y );
    }

    private void drawRevertTooltip( int x, int y ) {
        drawHoveringText( I18n.format( "gui.modernity.settings.revert.desc" ), x, y );
    }

    private void drawRevertDefTooltip( int x, int y ) {
        drawHoveringText( I18n.format( "gui.modernity.settings.revertdef.desc" ), x, y );
    }

    private void drawSettingTooltip( int x, int y, String settingKey ) {
        String name = I18n.format( settingKey );
        String[] lines = name.split( "[\\n\\r]" );
        drawHoveringText( Arrays.asList( lines ), x, y );
    }

    public void updateAllSettings() {
        for( GuiSettingControl<?, ?, ?> control : controls ) {
            control.updateSetting();
        }
    }

    public void renderTooltips( int x, int y ) {
        for( GuiSettingControl<?, ?, ?> control : controls ) {
            if( x >= control.getX() && x <= control.getX2() && y >= control.getY() && y <= control.getY2() ) {
                int x1 = x - control.getX() - control.getWidth();
                if( settings instanceof LocalServerSettings && x1 >= - 60 && x1 <= - 40 ) {
                    drawRevertDefTooltip( x, y );
                } else if( x1 >= - 40 && x1 <= - 20 ) {
                    drawRevertTooltip( x, y );
                } else if( x1 >= - 20 && x1 <= 0 ) {
                    drawResetTooltip( x, y );
                } else {
                    drawSettingTooltip( x, y, control.getSetting().getDescTranslationKey() );
                }
            }
        }
    }

    @Override
    public void tick() {
        for( GuiSettingControl<?, ?, ?> control : controls ) {
            control.tick();
        }
    }

    @Override
    public void render( int x, int y, float partialTicks ) {
        drawDefaultBackground();
        super.render( x, y, partialTicks );
        for( GuiSettingControl<?, ?, ?> control : controls ) {
            control.render( x, y, partialTicks );
        }
        renderTooltips( x, y );
    }
}
