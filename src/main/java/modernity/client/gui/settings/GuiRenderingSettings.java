/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.client.gui.settings;

import modernity.client.gui.settings.controls.GuiBooleanControl;
import modernity.common.settings.ClientSettings;
import net.minecraft.client.gui.GuiScreen;

public class GuiRenderingSettings extends GuiSettings<ClientSettings> {
    private final GuiBooleanControl<ClientSettings> walkingParticlesControl = new GuiBooleanControl<>( getSettings().walkingParticles, getSettings() );

    public GuiRenderingSettings( GuiScreen lastScreen, ClientSettings settings ) {
        super( lastScreen, settings );
        controls.add( walkingParticlesControl );
    }

    @Override
    protected void initGui() {
        super.initGui();
        children.add( walkingParticlesControl );
        walkingParticlesControl.setDimensions( width / 2 - 185, 20, 180, 20 );
    }
}
