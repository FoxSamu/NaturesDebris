/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 06 - 2020
 * Author: rgsw
 */

package modernity.common.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Holder class for Modernity damage sources.
 */
public class MDDamageSource extends DamageSource {
    public static final MDDamageSource NETTLES = new MDDamageSource( "nettles" );
    public static final MDDamageSource TURUPT = new MDDamageSource( "turupt" );
    public static final MDDamageSource FOXGLOVE = new MDDamageSource( "foxglove" );

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
