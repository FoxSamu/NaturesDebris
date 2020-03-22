/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modul.modules;

import java.lang.annotation.*;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface ModuleListener {
    String[] value() default {};
}
