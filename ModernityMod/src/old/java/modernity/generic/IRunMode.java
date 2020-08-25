/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic;

@FunctionalInterface
public interface IRunMode {
    IRunMode CLIENT = () -> "modernity.client.ModernityClient";
    IRunMode SERVER = () -> "modernity.server.ModernityServer";
    IRunMode DATA = () -> "modernity.data.ModernityData";

    String getClassName();
}
