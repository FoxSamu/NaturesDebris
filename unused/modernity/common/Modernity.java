package modernity.common;

import modernity.ModernityBootstrap;
import modernity.api.IModernity;
import modernity.api.LifecyclePhase;

public class Modernity implements IModernity {

    /*
     * LOADING LIFECYCLE
     */

    // Mod Construction
    public void construct() {
    }

    // Mod Loading Setup
    public void setup() {
    }

    // Mod Loading Complete
    public void complete() {
    }

    @Override
    public LifecyclePhase getLifecyclePhase() {
        return null;
    }

    public static Modernity get() {
        return ModernityBootstrap.MODERNITY;
    }
}
