package com.jarhax.eyespy.api.ui;

/**
 * Represents a style of progress bar that can be used by the EyeSpy HUD.
 * <p>
 * This system is used by EyeSpy to clarify which bar styles we ship with the mod. This system is completely optional,
 * and we do not register the styles anywhere. You can use this to add your own styles, or just use the texture paths
 * directly.
 *
 * @param style The name of the style. New styles will load textures from 'EyeSpy/Hud/ProgressBars/style_name/'.
 */
public record ProgressBarStyle(String style) {

    /**
     * A rounded, clean, and minimalist design. This is the standard style used in the EyeSpy HUD.
     */
    public static final ProgressBarStyle CLEAN = new ProgressBarStyle("Clean");

    /**
     * A framed box with a striped meter. This style works well for technical content like power levels and pressure.
     */
    public static final ProgressBarStyle BOXED = new ProgressBarStyle("Boxed");

    /**
     * Provides a path to the background texture for the style.
     *
     * @return The background texture for the style.
     */
    public String bg() {
        return path("BG");
    }

    /**
     * Provides a path to the white fill texture for the style.
     *
     * @return The white fill texture for the style.
     */
    public String white() {
        return path("White");
    }

    /**
     * Provides a path to the pink fill texture for the style.
     *
     * @return The pink fill texture for the style.
     */
    public String pink() {
        return path("Pink");
    }

    /**
     * Provides a path to the red fill texture for the style.
     *
     * @return The red fill texture for the style.
     */
    public String red() {
        return path("Red");
    }

    /**
     * Provides a path to the magenta fill texture for the style.
     *
     * @return The magenta fill texture for the style.
     */
    public String magenta() {
        return path("Magenta");
    }

    /**
     * Provides a path to the purple fill texture for the style.
     *
     * @return The purple fill texture for the style.
     */
    public String purple() {
        return path("Purple");
    }

    /**
     * Provides a path to the blue fill texture for the style.
     *
     * @return The blue fill texture for the style.
     */
    public String blue() {
        return path("Blue");
    }

    /**
     * Provides a path to the cyan fill texture for the style.
     *
     * @return The cyan fill texture for the style.
     */
    public String cyan() {
        return path("Cyan");
    }

    /**
     * Provides a path to the green fill texture for the style.
     *
     * @return The green fill texture for the style.
     */
    public String green() {
        return path("Green");
    }

    /**
     * Provides a path to the yellow fill texture for the style.
     *
     * @return The yellow fill texture for the style.
     */
    public String yellow() {
        return path("Yellow");
    }

    /**
     * Provides a path to the orange fill texture for the style.
     *
     * @return The orange fill texture for the style.
     */
    public String orange() {
        return path("Orange");
    }

    /**
     * Provides a path to the brown fill texture for the style.
     *
     * @return The brown fill texture for the style.
     */
    public String brown() {
        return path("Brown");
    }

    /**
     * Returns an array of every fill color texture supported by the style.
     *
     * @return An array of the fill color textures for the style.
     */
    public String[] colors() {
        return new String[]{
                white(),
                pink(),
                red(),
                magenta(),
                purple(),
                blue(),
                cyan(),
                green(),
                yellow(),
                orange(),
                brown()
        };
    }

    /**
     * Creates a path based on a texture variant name.
     *
     * @param variant The variant name of the texture.
     * @return A path to the texture variant.
     */
    private String path(String variant) {
        return "EyeSpy/Hud/ProgressBars/" + style + "/ProgressBar_" + variant + ".png";
    }
}