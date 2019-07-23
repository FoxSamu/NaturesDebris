/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

public class WikiTranslations {
    public static final String baseFormat = Util.makeTranslationKey( "gui", new ResourceLocation( "modernity:wiki" ) );
    public static final String cancel = "gui.cancel";
    public static final String export = baseFormat + ".export";
    public static final String item = baseFormat + ".item_label";
    public static final String count = baseFormat + ".count";
    public static final String damage = baseFormat + ".damage";
    public static final String clipboard = baseFormat + ".clipboard";
    public static final String copy = baseFormat + ".copy";
    public static final String paste = baseFormat + ".paste";
    public static final String reset = baseFormat + ".reset";
    public static final String scale = baseFormat + ".scale";
    public static final String filename = baseFormat + ".filename";
    public static final String blockState = baseFormat + ".block_state";
    public static final String rotationMode = baseFormat + ".rot_mode";
    public static final String fluidEnabled = baseFormat + ".fluids_enabled";
    public static final String entity = baseFormat + ".entity";
    public static final String margin = baseFormat + ".margin";

    public static final String craftingType = baseFormat + ".type_crafting";
    public static final String smeltingType = baseFormat + ".type_smelting";
    public static final String brewingType = baseFormat + ".type_brewing";
    public static final String fluidType = baseFormat + ".type_fluid";
    public static final String blockType = baseFormat + ".type_block";
    public static final String itemType = baseFormat + ".type_item";
    public static final String itemFrameType = baseFormat + ".type_item_frame";
    public static final String itemIconType = baseFormat + ".type_item_icon";
    public static final String entityType = baseFormat + ".type_entity";
}
