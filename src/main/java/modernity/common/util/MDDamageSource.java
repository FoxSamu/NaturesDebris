/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 28 - 2019
 */

package modernity.common.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MDDamageSource extends DamageSource {
    public static final MDDamageSource NETTLES = new MDDamageSource( "nettles" );

    public MDDamageSource( String damageType ) {
        super( damageType );
    }

    @Override
    public ITextComponent getDeathMessage( LivingEntity entity ) {
        LivingEntity victim = entity.getAttackingEntity();
        String withoutVictim = "modernity.death." + damageType;
        String withVictim = withoutVictim + ".player";
        return victim != null ?
               new TranslationTextComponent( withVictim, entity.getDisplayName(), victim.getDisplayName() ) :
               new TranslationTextComponent( withoutVictim, entity.getDisplayName() );
    }
}
