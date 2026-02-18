package com.jarhax.eyespy.api.ui;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.jarhax.eyespy.api.util.Identifier;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a container element that groups multiple elements under a single parent.
 */
public class Group extends UIElement {

    /**
     * The mode to use when arranging child elements within the group.
     */
    private final LayoutMode layout;

    /**
     * A collection of elements within this group.
     */
    private final Collection<UIElement> children;

    /**
     * @param id       A unique ID for the element.
     * @param children The elements to put inside the group.
     */
    public Group(Identifier id, UIElement... children) {
        this(id, LayoutMode.TOP, Arrays.asList(children));
    }

    /**
     * @param id       A unique ID for the element.
     * @param children The elements to put inside the group.
     */
    public Group(Identifier id, Collection<UIElement> children) {
        this(id, LayoutMode.TOP, children);
    }

    /**
     * @param id       A unique ID for the element.
     * @param layout   Determines how child elements should be visually arranged.
     * @param children The elements to put inside the group.
     */
    public Group(Identifier id, LayoutMode layout, Collection<UIElement> children) {
        super(id);
        this.layout = layout;
        this.children = children;
    }

    @Override
    public String build() {
        return """
                Group #%s {
                    LayoutMode: %s;
                }
                """.formatted(this.id.ui(), this.layout.elementName());
    }

    @Override
    public void appendUI(@Nonnull UICommandBuilder ui, @Nonnull AnchorProperties anchor, String parent) {
        super.appendUI(ui, anchor, parent);
        for (UIElement child : this.children) {
            child.appendUI(ui, anchor, "#" + this.id.ui());
        }
    }

    @Override
    public int height() {
        // Height is determined by the children elements.
        return 0;
    }
}
