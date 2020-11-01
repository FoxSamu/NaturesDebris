/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import natures.debris.api.INaturesDebrisInfo;
import natures.debris.api.util.exc.InstanceOfUtilityClassException;

/**
 * Class that holds constants about Nature's Debris, which are usually replaced by Gradle. Most constants are used in
 * commands to display the version message.
 */
public final class NdInfo {
    public static final INaturesDebrisInfo INSTANCE = new Instance();

    /**
     * The mod ID of Nature's Debris, which is {@code ndebris}.
     */
    @DynamicConstant("mod_id")
    public static final String ID = "ndebris";

    /**
     * The version of Nature's Debris, injected by gradle and otherwise "IDE".
     */
    @DynamicConstant("version")
    public static final String VERSION = "IDE";

    /**
     * The version name of Nature's Debris, injected by gradle and otherwise "The IDE Version".
     */
    @DynamicConstant("version_name")
    public static final String VERSION_NAME = "The IDE Version";

    /**
     * SHA1 of Nature's Debris. Unused, but planned for the {@code /mddebug} command. Injected by gradle and otherwise
     * {@code NO:FI:NG:ER:PR:IN:TA:VA:IL:AB:LE}.
     */
    @DynamicConstant("sha1")
    public static final String SHA1 = "NO:FI:NG:ER:PR:IN:TA:VA:IL:AB:LE";

    /**
     * Boolean indicating we are running from the IDE. Unused, but planned for the {@code /mddebug} command. Injected by
     * gradle and otherwise true.
     */
    public static final boolean IDE = Boolean.parseBoolean(System.getProperty("natures.debris.ide", "false"));

    /**
     * Boolean indicating we are running the data generator.
     */
    public static final boolean DATAGEN = Boolean.parseBoolean(System.getProperty("natures.debris.datagen", "false"));

    /**
     * Boolean indicating the current JAR is signed, if there is any jar, and otherwise false. Unused, but planned for
     * the {@code /mddebug} command. Injected by gradle and otherwise false.
     */
    @DynamicConstant("signed")
    public static final boolean SIGNED = false;

    private NdInfo() {
        throw new InstanceOfUtilityClassException();
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
