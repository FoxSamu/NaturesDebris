/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved.
 * This file is part of the Modernity Plugin API and may be used,
 * included and distributed within other projects without further
 * permission, unless the copyright holder is not the original
 * author or the owner had forbidden the user to use this file.
 * Other terms and conditions still apply.
 *
 * For a full license, see LICENSE.txt.
 */

package modernity.api.plugin;

import modernity.api.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a plugin for the Modernity. All classes annotated with {@code @ModernityPlugin} are recognized by the
 * Modernity mod and loaded right before construction time.
 * <p>
 * A distribution side may be given as optional {@code value} argument to make your plugin only load on a specific
 * distribution:
 * <ul>
 * <li>{@link Side#COMMON COMMON}: The plugin always loads (default)</li>
 * <li>{@link Side#CLIENT CLIENT}: The plugin only loads on the client distribution</li>
 * <li>{@link Side#SERVER SERVER}: The plugin only loads on the dedicated server distribution</li>
 * </ul>
 * <p>
 * Example:
 * <pre>
 * &#64;ModernityPlugin( ModernityPlugin.Side.CLIENT )
 * public class MyClientPlugin implements ILifecycleListener {
 *     &#64;Override
 *     public void modernitySetup( IModernity modernity ) {
 *         System.out.println( "Plugin loaded" );
 *     }
 * }
 * </pre>
 * Once the Modernity loads this plugin, it will print "{@code Plugin loaded}" once the Modernity finished setup-time
 * loading. For more info, check the wiki on GitHub
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface ModernityPlugin {
    Side value() default Side.COMMON;

}
