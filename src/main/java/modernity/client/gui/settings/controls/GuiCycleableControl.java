/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.client.gui.settings.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import modernity.common.settings.core.CycleableSetting;
import modernity.common.settings.core.Settings;

public class GuiCycleableControl <T, S extends CycleableSetting<T>, C extends Settings> extends GuiSettingControl<T, S, C> {

    private final GuiButton cycleButton = new GuiButton( 0, 0, 0, 0, 20, "" ) {
        @Override
        public boolean mouseClicked( double x, double y, int button ) {
            if( button == 0 || button == 1 ) {
                boolean pressable = this.isPressable( x, y );
                if( pressable ) {
                    this.playPressSound( Minecraft.getInstance().getSoundHandler() );
                    getSetting().cycle( button == 1 );
                    updateSetting();
                    return true;
                }
            }

            return false;
        }
    };

    public GuiCycleableControl( S setting, C settings ) {
        super( setting, settings );
        getChildren().add( cycleButton );
        updateSetting();
    }

    @Override
    public void updateDimensions() {
        super.updateDimensions();
        cycleButton.x = x;
        cycleButton.y = y;
        cycleButton.width = width;
        cycleButton.height = height;
    }

    @Override
    public void updateSetting() {
        cycleButton.displayString = I18n.format( getSetting().getTranslationKey(), getSetting().formatValue() );
    }

    @Override
    public void render( int mouseX, int mouseY, float partialTicks ) {
        super.render( mouseX, mouseY, partialTicks );
        cycleButton.render( mouseX, mouseY, partialTicks );
    }

    @Override
    public void disable() {
        super.disable();
        cycleButton.enabled = false;
    }
}
