package com.jarhax.eyespy.api.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.ui.Anchor;
import com.hypixel.hytale.server.core.ui.Value;

import javax.annotation.Nullable;

/**
 * Defines the properties of an Anchor element and can be used to build an Anchor instance.
 */
public class AnchorProperties {

    /**
     * A codec for anchor properties. This is primarily intended for reading anchors from save/config files.
     */
    public static final BuilderCodec<AnchorProperties> CODEC = BuilderCodec.builder(AnchorProperties.class, AnchorProperties::new)
            .append(new KeyedCodec<>("Left", Codec.INTEGER), (p, t) -> p.left = t, p -> p.left)
            .add()
            .append(new KeyedCodec<>("Right", Codec.INTEGER), (p, t) -> p.right = t, p -> p.right)
            .add()
            .append(new KeyedCodec<>("Top", Codec.INTEGER), (p, t) -> p.top = t, p -> p.top)
            .add()
            .append(new KeyedCodec<>("Bottom", Codec.INTEGER), (p, t) -> p.bottom = t, p -> p.bottom)
            .add()
            .append(new KeyedCodec<>("Height", Codec.INTEGER), (p, t) -> p.height = t, p -> p.height)
            .add()
            .append(new KeyedCodec<>("Full", Codec.INTEGER), (p, t) -> p.full = t, p -> p.full)
            .add()
            .append(new KeyedCodec<>("Horizontal", Codec.INTEGER), (p, t) -> p.horizontal = t, p -> p.horizontal)
            .add()
            .append(new KeyedCodec<>("Vertical", Codec.INTEGER), (p, t) -> p.vertical = t, p -> p.vertical)
            .add()
            .append(new KeyedCodec<>("Width", Codec.INTEGER), (p, t) -> p.width = t, p -> p.width)
            .add()
            .append(new KeyedCodec<>("MinWidth", Codec.INTEGER), (p, t) -> p.minWidth = t, p -> p.minWidth)
            .add()
            .append(new KeyedCodec<>("MaxWidth", Codec.INTEGER), (p, t) -> p.maxWidth = t, p -> p.maxWidth)
            .add()
            .build();

    private Integer left = null;
    private Integer right = null;
    private Integer top = null;
    private Integer bottom = null;
    private Integer height = null;
    private Integer full = null;
    private Integer horizontal = null;
    private Integer vertical = null;
    private Integer width = null;
    private Integer minWidth = null;
    private Integer maxWidth = null;
    private int minHeight = 0;

    /**
     * Adds to the height of the element.
     *
     * @param height The amount of height to add.
     */
    public void addHeight(int height) {
        this.height = this.height == null ? height : this.height + height;
    }

    /**
     * Sets the minimum height of the element, ensuring the element will always be at least this tall. This does not add
     * height, it just sets the minimum. This may be useful if you want to append an element that may not add any
     * height, on its own.
     *
     * @param height The minimum height for the HUD.
     */
    public void ensureHeight(int height) {
        if (this.minHeight < height) {
            this.minHeight = height;
        }
    }

    /**
     * Sets the offset from the left side of the screen.
     *
     * @param left The offset to add.
     * @return The same anchor properties instance.
     */
    public AnchorProperties setLeft(@Nullable Integer left) {
        this.left = left;
        return this;
    }

    /**
     * Sets the offset from the right side of the screen.
     *
     * @param right The offset to add.
     * @return The same anchor properties instance.
     */
    public AnchorProperties setRight(@Nullable Integer right) {
        this.right = right;
        return this;
    }

    /**
     * Sets the offset from the top of the screen.
     *
     * @param top The offset to add.
     * @return The same anchor properties instance.
     */
    public AnchorProperties setTop(@Nullable Integer top) {
        this.top = top;
        return this;
    }

    /**
     * Sets the offset from the bottom of the screen.
     *
     * @param bottom The offset to add.
     * @return The same anchor properties instance.
     */
    public AnchorProperties setBottom(@Nullable Integer bottom) {
        this.bottom = bottom;
        return this;
    }

    /**
     * Sets the height of the element.
     *
     * @param height The height of the element.
     * @return The same anchor properties instance.
     */
    public AnchorProperties setHeight(@Nullable Integer height) {
        this.height = height;
        return this;
    }

    /**
     * Sets an offset on all sides of the element.
     *
     * @param full The offset to add.
     * @return The same anchor properties instance.
     */
    public AnchorProperties setFull(@Nullable Integer full) {
        this.full = full;
        return this;
    }

    /**
     * Sets an offset on the left and right side of the element.
     *
     * @param horizontal The offset to add.
     * @return The same anchor properties instance.
     */
    public AnchorProperties setHorizontal(@Nullable Integer horizontal) {
        this.horizontal = horizontal;
        return this;
    }

    /**
     * Sets an offset on the top and bottom of the element.
     *
     * @param vertical The offset to add.
     * @return The same anchor properties instance.
     */
    public AnchorProperties setVertical(@Nullable Integer vertical) {
        this.vertical = vertical;
        return this;
    }

    /**
     * Sets the width of the element.
     *
     * @param width The width of the element.
     * @return The same anchor properties instance.
     */
    public AnchorProperties setWidth(@Nullable Integer width) {
        this.width = width;
        return this;
    }

    /**
     * Sets the minimum width of the element.
     *
     * @param minWidth The minimum width of the element.
     * @return The same anchor properties instance.
     */
    public AnchorProperties setMinWidth(@Nullable Integer minWidth) {
        this.minWidth = minWidth;
        return this;
    }

    /**
     * Sets the maximum width of the element.
     *
     * @param maxWidth The maximum width of the element.
     * @return The same anchor properties instance.
     */
    public AnchorProperties setMaxWidth(@Nullable Integer maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    /**
     * Gets the offset on the left side of the element.
     *
     * @return The offset on the left side of the element.
     */
    @Nullable
    public Integer left() {
        return left;
    }

    /**
     * Gets the offset on the right side of the element.
     *
     * @return The offset on the right side of the element.
     */
    @Nullable
    public Integer right() {
        return right;
    }

    /**
     * Gets the offset on the top side of the element.
     *
     * @return The offset on the top side of the element.
     */
    @Nullable
    public Integer top() {
        return top;
    }

    /**
     * Gets the offset on the bottom side of the element.
     *
     * @return The offset on the bottom side of the element.
     */
    @Nullable
    public Integer bottom() {
        return bottom;
    }

    /**
     * Gets the height of the element.
     *
     * @return The height of the element.
     */
    @Nullable
    public Integer height() {
        return height;
    }

    /**
     * Gets the offset applied to all sides of the element.
     *
     * @return The full offset applied to all sides.
     */
    @Nullable
    public Integer full() {
        return full;
    }

    /**
     * Gets the offset applied to the left and right sides of the element.
     *
     * @return The horizontal offset.
     */
    @Nullable
    public Integer horizontal() {
        return horizontal;
    }

    /**
     * Gets the offset applied to the top and bottom of the element.
     *
     * @return The vertical offset.
     */
    @Nullable
    public Integer vertical() {
        return vertical;
    }

    /**
     * Gets the width of the element.
     *
     * @return The width of the element.
     */
    @Nullable
    public Integer width() {
        return width;
    }

    /**
     * Gets the minimum width of the element.
     *
     * @return The minimum width of the element.
     */
    @Nullable
    public Integer minWidth() {
        return minWidth;
    }

    /**
     * Gets the maximum width of the element.
     *
     * @return The maximum width of the element.
     */
    @Nullable
    public Integer maxWidth() {
        return maxWidth;
    }

    /**
     * Builds an anchor using the defined anchor properties.
     *
     * @return The Anchor that was built using the properties.
     */
    public Anchor build() {
        final Anchor anchor = new Anchor();
        if (this.left != null) {
            anchor.setLeft(Value.of(this.left));
        }
        if (this.right != null) {
            anchor.setRight(Value.of(this.right));
        }
        if (this.top != null) {
            anchor.setTop(Value.of(this.top));
        }
        if (this.bottom != null) {
            anchor.setBottom(Value.of(this.bottom));
        }
        if (this.height != null) {
            anchor.setHeight(Value.of(Math.max(this.height, this.minHeight)));
        }
        if (this.full != null) {
            anchor.setFull(Value.of(this.full));
        }
        if (this.horizontal != null) {
            anchor.setHorizontal(Value.of(this.horizontal));
        }
        if (this.vertical != null) {
            anchor.setVertical(Value.of(this.vertical));
        }
        if (this.width != null) {
            anchor.setWidth(Value.of(this.width));
        }
        if (this.minWidth != null) {
            anchor.setMinWidth(Value.of(this.minWidth));
        }
        if (this.maxWidth != null) {
            anchor.setMaxWidth(Value.of(this.maxWidth));
        }
        return anchor;
    }

    /**
     * Resets all properties of the anchor back to their defaults.
     */
    public void reset() {
        this.left = null;
        this.right = null;
        this.top = null;
        this.bottom = null;
        this.height = null;
        this.full = null;
        this.horizontal = null;
        this.vertical = null;
        this.width = null;
        this.minWidth = null;
        this.maxWidth = null;
        this.minHeight = 0;
    }

    /**
     * Creates a copy of bounds and returns them as a new object.
     *
     * @return A newly created AnchorProperties instance.
     */
    public AnchorProperties copy() {
        final AnchorProperties bounds = new AnchorProperties();
        bounds.left = this.left;
        bounds.right = this.right;
        bounds.top = this.top;
        bounds.bottom = this.bottom;
        bounds.height = this.height;
        bounds.full = this.full;
        bounds.horizontal = this.horizontal;
        bounds.vertical = this.vertical;
        bounds.width = this.width;
        bounds.minWidth = this.minWidth;
        bounds.maxWidth = this.maxWidth;
        bounds.minHeight = this.minHeight;
        return bounds;
    }
}