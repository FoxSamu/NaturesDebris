/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 7 - 2019
 */

package modernity.generic.util;

import java.util.Random;

public interface IPicker <T> {
    T random( Random rand );
}
