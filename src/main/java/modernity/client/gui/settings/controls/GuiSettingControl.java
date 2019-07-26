/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.client.gui.settings.controls;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiEventHandler;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.resources.I18n;

import modernity.common.settings.LocalServerSettings;
import modernity.common.settings.RemoteServerSettings;
import modernity.common.settings.core.AbstractSetting;
import modernity.common.settings.core.Settings;

import java.util.List;

public abstract class GuiSettingControl <T, S extends AbstractSetting<T>, C extends Settings> extends GuiEventHandler {
    private final S setting;
    private final C settings;

    protected int x;
    protected int y;
    protected int width;
    private int fullWidth;
    protected int height;

    private final List<IGuiEventListener> children = Lists.newArrayList();


    private final GuiButton resetButton = new GuiButton( 0, 0, 0, 20, 20, I18n.format( "gui.modernity.settings.reset" ) ) {
        @Override
        public void onClick( double x, double y ) {
            super.onClick( x, y );
            setting.setDefault();
            updateSetting();
        }
    };

    private final GuiButton revertButton = new GuiButton( 0, 0, 0, 20, 20, I18n.format( "gui.modernity.settings.revert" ) ) {
        @Override
        public void onClick( double x, double y ) {
            super.onClick( x, y );
            setting.deserialize( settings.getFile().get( setting.getKey() ) );
            updateSetting();
        }
    };

    private final GuiButton revertDefButton = new GuiButton( 0, 0, 0, 20, 20, I18n.format( "gui.modernity.settings.revertdef" ) ) {
        @Override
        public void onClick( double x, double y ) {
            super.onClick( x, y );
            ( (LocalServerSettings) settings ).revertToDefaultSetting( setting );
            updateSetting();
        }
    };

    public GuiSettingControl( S setting, C settings ) {
        this.setting = setting;
        this.settings = settings;
        children.add( resetButton );
        children.add( revertButton );
        if( settings instanceof LocalServerSettings ) {
            children.add( revertDefButton );
        }
        if( settings instanceof RemoteServerSettings ) {
            disable();
        }
    }

    public void setDimensions( int x, int y, int width, int height ) {
        this.x = x;
        this.y = y;

        fullWidth = width;
        if( settings instanceof LocalServerSettings ) {
            this.width = fullWidth - 60;
        } else {
            this.width = fullWidth - 40;
        }
        this.height = height;
        updateDimensions();
    }

    public void setSize( int w, int h ) {
        setDimensions( x, y, w, h );
    }

    public void setLocation( int x, int y ) {
        setDimensions( x, y, width, height );
    }

    public void updateDimensions() {
        resetButton.x = x + fullWidth - 20;
        revertButton.x = x + fullWidth - 40;
        resetButton.y = y + height - 20;
        revertButton.y = y + height - 20;
        if( settings instanceof LocalServerSettings ) {
            revertDefButton.x = x + fullWidth - 60;
            revertDefButton.y = y + height - 20;
        }
    }

    public void updateSetting() {

    }

    public C getSettings() {
        return settings;
    }

    public S getSetting() {
        return setting;
    }

    public T getValue() {
        return setting.get();
    }

    public void setValue( T value ) {
        setting.set( value );
        updateSetting();
    }


    public void render( int mouseX, int mouseY, float partialTicks ) {
        resetButton.render( mouseX, mouseY, partialTicks );
        revertButton.render( mouseX, mouseY, partialTicks );
        if( settings instanceof LocalServerSettings ) {
            revertDefButton.render( mouseX, mouseY, partialTicks );
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getX2() {
        return x + fullWidth;
    }

    public int getY2() {
        return y + height;
    }

    public int getWidth() {
        return fullWidth;
    }

    public int getHeight() {
        return height;
    }

    public void disable() {
        resetButton.enabled = false;
        revertButton.enabled = false;
        revertDefButton.enabled = false;
    }

    public void tick() {
        if( setting.isChanged() ) {
            updateSetting();
        }
    }

    @Override
    public List<IGuiEventListener> getChildren() {
        return children;
    }
}
