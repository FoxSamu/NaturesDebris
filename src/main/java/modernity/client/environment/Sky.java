package modernity.client.environment;

/**
 * Describes the appearance of a Modernity sky.
 */
public class Sky {
    /**
     * The color of sky twilight, random colored light coming from the horizon. Fog will not fade to this color so that
     * distant object make shades in the backlight.
     */
    public final float[] twilightColor = { 0, 0, 0 };

    /**
     * The color of backlight, uniformly colored light coming from above.
     */
    public final float[] backlightColor = { 0, 0, 0 };

    /**
     * The color of sky light or dark matter (when set darker than twilight), which are alpha-blended clouds between
     * others.
     */
    public final float[] skylightColor = { 0, 0, 0 };

    /**
     * The color multiplier of stars and meteorites. This is not the color of stars themselves!
     */
    public final float[] starColor = { 0, 0, 0 };

    /**
     * The color multiplier of clouds. This is not the color of clouds themselves.
     */
    public final float[] cloudColor = { 0, 0, 0 };

    /**
     * The color multiplier of the satellite.
     */
    public final float[] moonColor = { 0, 0, 0 };

    /**
     * The chance that a game tick will spawn a falling star (meteorite).
     */
    public float meteoriteAmount;

    /**
     * The chance that a game tick will spawn a cloud cluster.
     */
    public float cloudAmount;

    /**
     * The brightness of the stars and meteorites.
     */
    public float starBrightness;

    /**
     * The brightness of cloud clusters.
     */
    public float cloudBrightness;

    /**
     * The brightness of skylight / dark matter clusters.
     */
    public float skylightBrightness;

    /**
     * The brightness of backlight.
     */
    public float backlightBrightness;

    /**
     * The brightness of twilight.
     */
    public float twilightBrightness;

    /**
     * The height of twilight.
     */
    public float twilightHeight;

    /**
     * The height randomisation of twilight.
     */
    public float twilightHeightRandom;

    /**
     * The brightness of the satellite.
     */
    public float moonBrightness;

    /**
     * The light reflection phase of the satellite
     */
    public int moonPhase;

    /**
     * The celestial angle of the satellite
     */
    public float moonRotation;

    public void setTwilightColor( float r, float g, float b ) {
        twilightColor[ 0 ] = r;
        twilightColor[ 1 ] = g;
        twilightColor[ 2 ] = b;
    }

    public void setBacklightColor( float r, float g, float b ) {
        backlightColor[ 0 ] = r;
        backlightColor[ 1 ] = g;
        backlightColor[ 2 ] = b;
    }

    public void setSkylightColor( float r, float g, float b ) {
        skylightColor[ 0 ] = r;
        skylightColor[ 1 ] = g;
        skylightColor[ 2 ] = b;
    }

    public void setStarColor( float r, float g, float b ) {
        starColor[ 0 ] = r;
        starColor[ 1 ] = g;
        starColor[ 2 ] = b;
    }

    public void setCloudColor( float r, float g, float b ) {
        cloudColor[ 0 ] = r;
        cloudColor[ 1 ] = g;
        cloudColor[ 2 ] = b;
    }

    public void setMoonColor( float r, float g, float b ) {
        moonColor[ 0 ] = r;
        moonColor[ 1 ] = g;
        moonColor[ 2 ] = b;
    }
}
