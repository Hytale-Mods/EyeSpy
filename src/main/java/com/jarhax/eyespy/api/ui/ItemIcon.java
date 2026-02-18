package com.jarhax.eyespy.api.ui;

import com.jarhax.eyespy.api.util.Identifier;

/**
 * The item icon element visually represents an item.
 */
public class ItemIcon extends UIElement {

    /**
     * The item to display on the screen.
     */
    private final String itemId;

    /**
     * The width of the item icon.
     */
    private final int width;

    /**
     * The height of the item icon.
     */
    private final int height;

    /**
     * @param id     A unique ID for the element.
     * @param itemId The ID of the item to display.
     */
    public ItemIcon(Identifier id, String itemId) {
        this(id, 64, 64, itemId);
    }

    /**
     * @param id     A unique ID for the element.
     * @param size   The size of the icon. Sets the width and height.
     * @param itemId The ID of the item to display.
     */
    public ItemIcon(Identifier id, int size, String itemId) {
        this(id, size, size, itemId);
    }

    /**
     * @param id     A unique ID for the element.
     * @param width  The width of the item icon.
     * @param height The height of the item icon.
     * @param itemId The ID of the item to display.
     */
    public ItemIcon(Identifier id, int width, int height, String itemId) {
        super(id);
        this.width = width;
        this.height = height;
        this.itemId = itemId;
    }

    @Override
    public String build() {
        return """
                ItemIcon #%s {
                    Anchor: (Width: %d, Height: %d);
                    ItemId: "%s";
                }
                """.formatted(this.id.ui(), this.width, this.height, this.itemId);
    }

    @Override
    public int height() {
        return this.height;
    }
}
