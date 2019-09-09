/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki;

import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public class GuiWikiUtil extends GuiScreen {

    private final GuiScreen lastScreen;

    public GuiWikiUtil( GuiScreen lastScreen ) {
        this.lastScreen = lastScreen;
    }


    @Override
    protected void initGui() {
        List<GuiWikiButton> buttons = new ArrayList<>();

        buttons.add( new GuiWikiButton( 0, 0, 0, WikiTranslations.craftingType ).setEventHandler( this::startCrafting ) );
        buttons.add( new GuiWikiButton( 0, 0, 0, WikiTranslations.smeltingType ).setEventHandler( this::startSmelting ) );
        buttons.add( new GuiWikiButton( 0, 0, 0, WikiTranslations.brewingType ).setEventHandler( this::startBrewing ) );
        buttons.add( new GuiWikiButton( 0, 0, 0, WikiTranslations.blockType ).setEventHandler( () -> mc.displayGuiScreen( new GuiWikiBlockEdit( this ) ) ) );
        buttons.add( new GuiWikiButton( 0, 0, 0, WikiTranslations.itemType ).setEventHandler( this::startItem ) );
        buttons.add( new GuiWikiButton( 0, 0, 0, WikiTranslations.itemFrameType ).setEventHandler( this::startItemFramed ) );
        buttons.add( new GuiWikiButton( 0, 0, 0, WikiTranslations.itemIconType ).setEventHandler( this::startItemIcon ) );
        buttons.add( new GuiWikiButton( 0, 0, 0, WikiTranslations.entityType ).setEventHandler( () -> mc.displayGuiScreen( new GuiWikiEntityEdit( this ) ) ) );
        buttons.add( new GuiWikiButton( 0, 0, 0, WikiTranslations.cancel ).setEventHandler( this::cancel ) );

        int height = buttons.size() * 4 - 4;
        for( GuiWikiButton button : buttons ) {
            button.x = width / 2 - button.getWidth() / 2;

            height += button.height;
        }

        int rootY = this.height / 2 - height / 2;

        for( GuiWikiButton button : buttons ) {
            button.y = rootY;
            rootY += button.height + 2;
            addButton( button );
        }

        super.initGui();
    }

    @Override
    public void render( int mouseX, int mouseY, float partialTicks ) {
        drawDefaultBackground();
        super.render( mouseX, mouseY, partialTicks );

    }

    public void cancel() {
        this.mc.displayGuiScreen( lastScreen );
    }

    public void startCrafting() {
        this.mc.displayGuiScreen(
                GuiWikiContainerEdit.builder( this )
                                    .background( "modernity:textures/gui/wiki/crafting.png" )
                                    .size( 128, 66 )
                                    .addSlot( 7, 7 )
                                    .addSlot( 25, 7 )
                                    .addSlot( 43, 7 )
                                    .addSlot( 7, 25 )
                                    .addSlot( 25, 25 )
                                    .addSlot( 43, 25 )
                                    .addSlot( 7, 43 )
                                    .addSlot( 25, 43 )
                                    .addSlot( 43, 43 )
                                    .addSlot( 101, 25 )
                                    .build()
        );
    }

    public void startSmelting() {
        this.mc.displayGuiScreen(
                GuiWikiContainerEdit.builder( this )
                                    .background( "modernity:textures/gui/wiki/smelting.png" )
                                    .size( 94, 66 )
                                    .addSlot( 7, 7 )
                                    .addSlot( 7, 43 )
                                    .addSlot( 67, 25 )
                                    .build()
        );
    }

    public void startBrewing() {
        this.mc.displayGuiScreen(
                GuiWikiContainerEdit.builder( this )
                                    .background( "modernity:textures/gui/wiki/brewing.png" )
                                    .size( 115, 71 )
                                    .addSlot( 69, 7 )
                                    .addSlot( 69, 48 )
                                    .addSlot( 46, 41 )
                                    .addSlot( 92, 41 )
                                    .build()
        );
    }

    public void startItemFramed() {
        this.mc.displayGuiScreen(
                GuiWikiContainerEdit.builder( this )
                                    .background( "modernity:textures/gui/wiki/item_frame.png" )
                                    .size( 32, 32 )
                                    .addSlot( 8, 8 )
                                    .build()
        );
    }

    public void startItemIcon() {
        this.mc.displayGuiScreen(
                GuiWikiContainerEdit.builder( this )
                                    .background( "modernity:textures/gui/wiki/item_icon.png" )
                                    .size( 18, 18 )
                                    .addSlot( 1, 1 )
                                    .build()
        );
    }

    public void startItem() {
        this.mc.displayGuiScreen(
                GuiWikiContainerEdit.builder( this )
                                    .size( 16, 16 )
                                    .addSlot( 0, 0 )
                                    .build()
        );
    }
}
