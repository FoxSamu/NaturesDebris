/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import java.util.function.Consumer;

public class GuiWikiButton extends GuiButton {
    private Consumer<GuiWikiButton> event;

    public GuiWikiButton( int buttonId, int x, int y, String buttonText ) {
        super( buttonId, x, y, I18n.format( buttonText ) );
    }

    public GuiWikiButton( int buttonId, int x, int y, int width, int height, String buttonText ) {
        super( buttonId, x, y, width, height, I18n.format( buttonText ) );
    }

    public GuiWikiButton setEventHandler( Runnable event ) {
        this.event = b -> event.run();
        return this;
    }

    public GuiWikiButton setEventHandler( Consumer<GuiWikiButton> event ) {
        this.event = event;
        return this;
    }

    public void setText( String buttonText ) {
        displayString = I18n.format( buttonText );
    }

    @Override
    public void onClick( double mouseX, double mouseY ) {
        super.onClick( mouseX, mouseY );
        if( this.event != null ) this.event.accept( this );
    }
}
