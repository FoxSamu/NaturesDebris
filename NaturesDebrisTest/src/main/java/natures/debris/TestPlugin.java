/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris;

import natures.debris.api.INaturesDebris;
import natures.debris.api.plugin.ILifecycleListener;
import natures.debris.api.plugin.NaturesDebrisPlugin;

@NaturesDebrisPlugin
public class TestPlugin implements ILifecycleListener {
    public TestPlugin() {
        System.out.println("Test plugin instantiated!");
    }

    @Override
    public void ndebrisConstruct(INaturesDebris naturesDebris) {
        System.out.println("Construction-time loading!");
    }

    @Override
    public void ndebrisSetup(INaturesDebris naturesDebris) {
        System.out.println("Setup-time loading!");
    }

    @Override
    public void ndebrisLoaded(INaturesDebris naturesDebris) {
        System.out.println("Completion-time loading!");
    }
}
