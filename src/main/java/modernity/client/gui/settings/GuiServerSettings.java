/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.client.gui.settings;

import net.minecraft.client.gui.GuiScreen;

import modernity.client.gui.settings.controls.GuiIntCycleControl;
import modernity.common.settings.ServerSettings;

public class GuiServerSettings extends GuiAbstractServerSettings {

    private final GuiIntCycleControl<ServerSettings> moonPhaseControl = new GuiIntCycleControl<>( getSettings().moonPhase, getSettings() );
    private final GuiIntCycleControl<ServerSettings> settingsCmdPermControl = new GuiIntCycleControl<>( getSettings().settingsCmdPermission, getSettings() );


    public GuiServerSettings( GuiScreen lastScreen, ServerSettings settings ) {
        super( lastScreen, settings );
        controls.add( moonPhaseControl );
        controls.add( settingsCmdPermControl );
    }

    @Override
    protected void initGui() {
        super.initGui();
        moonPhaseControl.setDimensions( width / 2 - 185, 20, 180, 20 );
        settingsCmdPermControl.setDimensions( width / 2 + 5, 20, 180, 20 );
    }
}
