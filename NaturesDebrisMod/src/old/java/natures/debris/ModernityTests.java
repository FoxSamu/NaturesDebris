/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris;

import natures.debris.generic.IRunMode;

import java.util.HashMap;
import java.util.Properties;

public final class ModernityTests {
    private static final HashMap<String, IRunMode> TEST_MODES = new HashMap<>();

    public static final IRunMode MODELS = mode("models", "modernity.tests.models.ModelsTest");

    private ModernityTests() {
    }

    private static IRunMode mode(String testName, String className) {
        IRunMode mode = () -> className;
        TEST_MODES.put(testName, mode);
        return mode;
    }

    public static IRunMode getTest() {
        Properties props = System.getProperties();
        if (props.containsKey("modernity.test")) {
            return TEST_MODES.get(props.getProperty("modernity.test"));
        }
        return null;
    }
}
