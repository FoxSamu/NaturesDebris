/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.resources.I18n;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GuiWikiLabel extends GuiLabel {
    public GuiWikiLabel( List<String> text, int color, FontRenderer font, int x, int y, boolean visible ) {
        super( text.stream().map( s -> I18n.format( s ) ).collect( Collectors.toList() ), color, font );
        this.x = x;
        this.y = y;
        this.visible = visible;
    }

    public GuiWikiLabel( String text, int color, FontRenderer font, int x, int y, boolean visible ) {
        super( Collections.singletonList( I18n.format( text ) ), color, font );
        this.x = x;
        this.y = y;
        this.visible = visible;
    }

    public GuiWikiLabel( List<String> text, int color, int x, int y, boolean visible ) {
        super( text.stream().map( s -> I18n.format( s ) ).collect( Collectors.toList() ), color, Minecraft.getInstance().fontRenderer );
        this.x = x;
        this.y = y;
        this.visible = visible;
    }

    public GuiWikiLabel( String text, int color, int x, int y, boolean visible ) {
        super( Collections.singletonList( I18n.format( text ) ), color, Minecraft.getInstance().fontRenderer );
        this.x = x;
        this.y = y;
        this.visible = visible;
    }
}
