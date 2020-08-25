/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Holder class for Modernity damage sources.
 */
public class MDDamageSource extends DamageSource {
    public static final ArrayList<String> TRANSLATIONS = new ArrayList<>();

    public static final MDDamageSource NETTLES = new MDDamageSource("nettles");
    public static final MDDamageSource TURUPT = new MDDamageSource("turupt");
    public static final MDDamageSource FOXGLOVE = new MDDamageSource("foxglove");

    public MDDamageSource(String damageType) {
        super(damageType);
        collectTranslations(TRANSLATIONS::add);
    }

    protected void collectTranslations(Consumer<String> keyConsumer) {
        String withoutVictim = "modernity.death." + damageType;
        String withVictim = withoutVictim + ".player";
        keyConsumer.accept(withoutVictim);
        keyConsumer.accept(withVictim);
    }

    @Override
    public ITextComponent getDeathMessage(LivingEntity entity) {
        LivingEntity victim = entity.getAttackingEntity();
        String withoutVictim = "modernity.death." + damageType;
        String withVictim = withoutVictim + ".player";
        return victim != null ?
               new TranslationTextComponent(withVictim, entity.getDisplayName(), victim.getDisplayName()) :
               new TranslationTextComponent(withoutVictim, entity.getDisplayName());
    }
}
