/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity;

import modernity.api.IModernity;
import modernity.api.plugin.ILifecycleListener;
import modernity.api.plugin.ModernityPlugin;

@ModernityPlugin
public class TestPlugin implements ILifecycleListener {
    public TestPlugin() {
        System.out.println( "Test plugin instantiated!" );
    }

    @Override
    public void modernityConstruct( IModernity modernity ) {
        System.out.println( "Construction-time loading!" );
    }

    @Override
    public void modernitySetup( IModernity modernity ) {
        System.out.println( "Setup-time loading!" );
    }

    @Override
    public void modernityLoaded( IModernity modernity ) {
        System.out.println( "Completion-time loading!" );
    }
}
