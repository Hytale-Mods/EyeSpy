package com.jarhax.eyespy.api.ui;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.jarhax.eyespy.api.util.Identifier;

import javax.annotation.Nonnull;

/**
 * A reusable component that represents a visual element within the EyeSpy HUD. UIElement are responsible for appending
 * content to the HUD and growing the HUD to the correct size.
 */
public abstract class UIElement {

    /**
     * A unique ID for the element.
     */
    public final Identifier id;

    public UIElement(Identifier id) {
        this.id = id;
    }

    /**
     * Appends the UI element to the EyeSpy HUD.
     * <p>
     * This method provides raw access to the UI builder, which can be very powerful but also very dangerous if you
     * don't know what you're doing. The API has been designed in such a way that custom UIElement should not need this,
     * but access has been provided to give creators more options.
     *
     * @param ui     The raw UI builder.
     * @param anchor The anchor for the EyeSpy HUD itself.
     * @param parent A UI selector for the element that the element should be appended to.
     */
    public void appendUI(@Nonnull UICommandBuilder ui, @Nonnull AnchorProperties anchor, String parent) {
        ui.appendInline(parent, this.build());
        anchor.addHeight(this.height());
    }

    /**
     * Builds a String representation of the element that will be appended to the HUD.
     *
     * @return A String representation of the element.
     */
    public abstract String build();

    /**
     * Determines the height of the element. This does not need to be a constant value, and will be automatically added
     * to the height of the HUD when it is built.
     *
     * @return The height of the element.
     */
    public abstract int height();
}