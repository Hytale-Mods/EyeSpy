package com.jarhax.eyespy.api.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.jarhax.eyespy.api.util.Identifier;

import javax.annotation.Nonnull;

/**
 * The label element represents formatted text that is displayed on the screen.
 */
public class Label extends UIElement {

    private final Message value;
    private final int fontSize;
    private final int height;

    /**
     * @param id    A unique ID for the element.
     * @param value The text to display on the screen.
     */
    public Label(Identifier id, Message value) {
        this(id, value, 18);
    }

    /**
     * @param id       A unique ID for the element.
     * @param value    The text to display on the screen.
     * @param fontSize The font size used for the text. The height of the label is 2x the font size.
     */
    public Label(Identifier id, Message value, int fontSize) {
        this(id, value, fontSize, fontSize * 2);
    }

    /**
     * @param id       A unique ID for the element.
     * @param value    The text to display on the screen.
     * @param fontSize The font size used for the text.
     * @param height   The height of the label element.
     */
    public Label(Identifier id, Message value, int fontSize, int height) {
        super(id);
        this.value = value;
        this.fontSize = fontSize;
        this.height = height;
    }

    @Override
    public String build() {
        return """
                Label #%s {
                  Style: LabelStyle(FontSize: %d);
                }
                """.formatted(this.id.ui(), this.fontSize);
    }

    @Override
    public void appendUI(@Nonnull UICommandBuilder ui, @Nonnull AnchorProperties anchor, String parent) {
        super.appendUI(ui, anchor, parent);
        ui.set(parent + " #%s.TextSpans".formatted(this.id.ui()), value);
    }

    @Override
    public int height() {
        return this.height;
    }
}