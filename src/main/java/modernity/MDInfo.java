/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity;

import net.rgsw.exc.InstanceOfUtilityClassException;

/**
 * Class that holds constants about the Modernity, which are usually replaced by Gradle. Most constants are used in
 * commands to display the version message.
 */
public final class MDInfo {
    /**
     * The mod ID of the Modernity, which is {@code modernity}.
     */
    public static final String MODID = "modernity";

    /**
     * The version of the Modernity, injected by gradle and otherwise "IDE".
     */
    public static final String VERSION = DynamicConstants.VERSION;

    /**
     * The version name of the Modernity, injected by gradle and otherwise "The IDE Version.
     */
    public static final String VERSION_NAME = DynamicConstants.VERSION_NAME;

    /**
     * SHA1 of the modernity. Unused, but planned for the {@code /mddebug} command. Injected by gradle and otherwise
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

    private MDInfo() {
        throw new InstanceOfUtilityClassException( "No MDInfo instances for you!" );
    }

    // Bunch of constants gradle would replace the uses of
    private static final class DynamicConstants {
        static final boolean IDE = true;
        static final boolean SIGNED = false;
        static final String VERSION = "IDE";
        static final String VERSION_NAME = "The IDE Version";
        static final String SHA1 = "NO:FI:NG:ER:PR:IN:TA:VA:IL:AB:LE";
    }
}
