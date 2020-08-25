/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris;

import natures.debris.api.INaturesDebrisInfo;
import natures.debris.api.util.exc.InstanceOfUtilityClassException;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Class that holds constants about Nature's Debris, which are usually replaced by Gradle. Most constants are used in
 * commands to display the version message.
 */
public final class NdInfo {
    public static final INaturesDebrisInfo INSTANCE = new Instance();

    /**
     * The mod ID of Nature's Debris, which is {@code ndebris}.
     */
    public static final String ID = "ndebris";

    /**
     * The version of Nature's Debris, injected by gradle and otherwise "IDE".
     */
    public static final String VERSION = DynamicConstants.VERSION;

    /**
     * The version name of Nature's Debris, injected by gradle and otherwise "The IDE Version.
     */
    public static final String VERSION_NAME = DynamicConstants.VERSION_NAME;

    /**
     * SHA1 of Nature's Debris. Unused, but planned for the {@code /mddebug} command. Injected by gradle and otherwise
     * {@code NO:FI:NG:ER:PR:IN:TA:VA:IL:AB:LE}.
     */
    public static final String SHA1 = DynamicConstants.SHA1;

    /**
     * Boolean indicating we are running from the IDE. Unused, but planned for the {@code /mddebug} command. Injected by
     * gradle and otherwise true.
     */
    public static final boolean IDE = DynamicConstants.IDE;

    /**
     * Boolean indicating the current JAR is signed, if there is any jar, and otherwise false. Unused, but planned for
     * the {@code /mddebug} command. Injected by gradle and otherwise false.
     */
    public static final boolean SIGNED = DynamicConstants.SIGNED;

    private NdInfo() {
        throw new InstanceOfUtilityClassException("No MDInfo instances for you!");
    }

    // Bunch of constants gradle would replace the uses of
    private static final class DynamicConstants {
        static final boolean IDE = true;
        static final boolean SIGNED = false;
        static final String VERSION = "IDE";
        static final String VERSION_NAME = "The IDE Version";
        static final String SHA1 = "NO:FI:NG:ER:PR:IN:TA:VA:IL:AB:LE";
    }

    private static class Instance implements INaturesDebrisInfo {
        @Override
        public String version() {
            return VERSION;
        }

        @Override
        public String versionName() {
            return VERSION_NAME;
        }

        @Override
        public boolean isClient() {
            return FMLEnvironment.dist == Dist.CLIENT;
        }

        @Override
        public boolean isDedicatedServer() {
            return FMLEnvironment.dist == Dist.DEDICATED_SERVER;
        }

        @Override
        public boolean isDevVersion() {
            return IDE;
        }
    }
}
