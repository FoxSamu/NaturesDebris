/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 9 - 2019
 */

package modernity.common.world.type;

import net.minecraft.world.WorldType;

public class ModernityWorldType extends WorldType {
    public ModernityWorldType( String name ) {
        super( name );
    }

    @Override
    public String getTranslationKey() {
        return "generator.modernity." + getName();
    }
}
