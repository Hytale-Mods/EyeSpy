package com.jarhax.eyespy.api.ui;

import com.jarhax.eyespy.api.util.Identifier;

/**
 * The progress bar element represents a visual indicator that shows how much of a process has been completed. Progress
 * bars have a background texture that is slowly filled in with a fill texture as the progress approaches completion.
 */
public class ProgressBar extends UIElement {

    private final float value;
    private final int width;
    private final int height;
    private final String background;
    private final String fill;

    /**
     * @param id    A unique ID for the element.
     * @param value The progress of the bar, as a float between 0 and 1.
     */
    public ProgressBar(Identifier id, float value) {
        this(id, value, ProgressBarStyle.CLEAN.orange());
    }

    /**
     * @param id    A unique ID for the element.
     * @param value The progress of the bar, as a float between 0 and 1.
     * @param fill  The fill texture for the bar.
     */
    public ProgressBar(Identifier id, float value, String fill) {
        this(id, value, 110, 16, ProgressBarStyle.CLEAN.bg(), fill);
    }

    /**
     * @param id         A unique ID for the element.
     * @param value      The progress of the bar, as a float between 0 and 1.
     * @param width      The width of the bar.
     * @param height     The height of the bar.
     * @param background The background texture for the bar.
     * @param fill       The fill texture for the bar.
     */
    public ProgressBar(Identifier id, float value, int width, int height, String background, String fill) {
        super(id);
        if (value < 0 || value > 1) {
            throw new IllegalStateException("Element '" + id + "' has a value of '" + value + "'. Progress bars display percentages and must be between 0 and 1.");
        }
        // For some reason, the Hytale client will crash if the value is too
        // precise. So we reduce it to three decimal places of precision.
        this.value = Math.round(value * 1000) / 1000f;
        this.width = width;
        this.height = height;
        this.background = background;
        this.fill = fill;
    }

    @Override
    public String build() {
        return """
                Group {
                  Anchor: (Width: %d, Height: %d);
                  Background: "%s";
                  ProgressBar #%s {
                    Value: %s;
                    BarTexturePath: "%s";
                  }
                }
                """.formatted(this.width, this.height, this.background, this.id.ui(), this.value, this.fill);
    }

    @Override
    public int height() {
        return this.height;
    }
}