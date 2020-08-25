/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.environment;

import modernity.api.util.math.ColorUtil;

public class Light {
    public final float[] ambient = new float[3];
    public final float[] block = new float[3];
    public final float[] sky = new float[3];


    public void setAmbient(double r, double g, double b) {
        ambient[0] = (float) r;
        ambient[1] = (float) g;
        ambient[2] = (float) b;
    }

    public void setAmbient(float r, float g, float b) {
        ambient[0] = r;
        ambient[1] = g;
        ambient[2] = b;
    }

    public void setAmbient(int rgb) {
        ambient[0] = ColorUtil.redf(rgb);
        ambient[1] = ColorUtil.greenf(rgb);
        ambient[2] = ColorUtil.bluef(rgb);
    }

    public void setBlock(double r, double g, double b) {
        block[0] = (float) r;
        block[1] = (float) g;
        block[2] = (float) b;
    }

    public void setBlock(float r, float g, float b) {
        block[0] = r;
        block[1] = g;
        block[2] = b;
    }

    public void setBlock(int rgb) {
        block[0] = ColorUtil.redf(rgb);
        block[1] = ColorUtil.greenf(rgb);
        block[2] = ColorUtil.bluef(rgb);
    }

    public void setSky(double r, double g, double b) {
        sky[0] = (float) r;
        sky[1] = (float) g;
        sky[2] = (float) b;
    }

    public void setSky(float r, float g, float b) {
        sky[0] = r;
        sky[1] = g;
        sky[2] = b;
    }

    public void setSky(int rgb) {
        sky[0] = ColorUtil.redf(rgb);
        sky[1] = ColorUtil.greenf(rgb);
        sky[2] = ColorUtil.bluef(rgb);
    }
}
