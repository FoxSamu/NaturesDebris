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

package modernity.api.util;

/**
 * An object with an index. An index is a number that indicates an object's location in its parent container. Indexed
 * objects that know their indices and can return them in the {@link #index()} method.
 */
@FunctionalInterface
public interface IIndexed {
    /**
     * Returns the index of this object.
     */
    int index();
}
