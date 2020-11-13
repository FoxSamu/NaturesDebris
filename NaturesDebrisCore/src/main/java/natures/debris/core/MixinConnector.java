/*
 * Copyright (c) 2020 Cryptic Mushroom and contributors
 * This file belongs to the Midnight mod and is licensed under the terms and conditions of Cryptic Mushroom. See
 * https://github.com/Cryptic-Mushroom/The-Midnight/blob/rewrite/LICENSE.md for the full license.
 *
 * Last updated: 2020 - 10 - 18
 */

package natures.debris.core;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;

import natures.debris.core.util.mixin.CustomInjectInjectionInfo;
import natures.debris.core.util.mixin.ModifyStackInjectionInfo;

public class MixinConnector implements IMixinConnector {
    @Override
    public void connect() {
        InjectionInfo.register(ModifyStackInjectionInfo.class);
        InjectionInfo.register(CustomInjectInjectionInfo.class);
        Mixins.addConfiguration("ndebris.mixins.json");
    }
}
