/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.client.gui.settings.controls;

import modernity.common.settings.core.CycleableSetting;
import modernity.common.settings.core.Settings;

public class GuiIntCycleControl <C extends Settings> extends GuiCycleableControl<Integer, CycleableSetting<Integer>, C> {
    public GuiIntCycleControl( CycleableSetting<Integer> setting, C settings ) {
        super( setting, settings );
    }
}
