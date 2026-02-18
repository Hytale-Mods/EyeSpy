package com.jarhax.eyespy.api.ui;


import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.codecs.EnumCodec;

/**
 * Determines how elements should be arranged within another ui element.
 */
public enum LayoutMode {

    /**
     * Elements take up the full size of their parent.
     */
    FULL,

    /**
     * Elements are placed left to right, aligned with the left of their parent.
     */
    LEFT,

    /**
     * Elements are placed left to right, aligned with the center of their parent.
     */
    CENTER,

    /**
     * Elements are placed left to right, aligned with the right of their parent.
     */
    RIGHT,

    /**
     * Elements are placed top to bottom, aligned with the top of their parent.
     */
    TOP,

    /**
     * Elements are placed top to bottom, aligned with the center of their parent.
     */
    MIDDLE,

    /**
     * Elements are placed top to bottom, aligned with the bottom of their parent.
     */
    BOTTOM,

    /**
     * Elements are placed left to right, aligned with the left of their parent. Child elements are scrollable.
     */
    LEFT_SCROLLING,

    /**
     * Elements are placed left to right, aligned with the right of their parent. Child elements are scrollable.
     */
    RIGHT_SCROLLING,

    /**
     * Elements are placed top to bottom, aligned with the top of their parent. Child elements are scrollable.
     */
    TOP_SCROLLING,

    /**
     * Elements are placed top to bottom, aligned with the bottom of their parent. Child elements are scrollable.
     */
    BOTTOM_SCROLLING,

    /**
     * Elements are placed left to right, aligned with the center of their parent horizontally and vertically.
     */
    CENTER_MIDDLE,

    /**
     * Elements are placed top to bottom, aligned with the center of their parent horizontally and vertically.
     */
    MIDDLE_CENTER,

    /**
     * Elements are placed left to right, aligned with the center of their parent. Overflowing elements wrap to the next
     * row of elements.
     */
    LEFT_CENTER_WRAP;

    private final String elementName;

    LayoutMode() {
        this.elementName = EnumCodec.EnumStyle.LEGACY.formatCamelCase(this.name());
    }

    public static final Codec<LayoutMode> CODEC = new EnumCodec<>(LayoutMode.class);

    /**
     * Gets the name of the layout mode in the format used by Hytale UI files.
     *
     * @return The element name.
     */
    public String elementName() {
        return this.elementName;
    }
}