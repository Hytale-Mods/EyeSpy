package com.jarhax.eyespy.api.info;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.jarhax.eyespy.EyeSpy;
import com.jarhax.eyespy.api.ui.AnchorProperties;
import com.jarhax.eyespy.api.ui.UIElement;
import com.jarhax.eyespy.api.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builds info for the HUD. Plugins will be given an instance that they can populate using the available methods.
 */
public class InfoBuilder {

    private UIElement icon = null;
    private final Map<Identifier, UIElement> header = new LinkedHashMap<>();
    private final Map<Identifier, UIElement> body = new LinkedHashMap<>();
    private final Map<Identifier, UIElement> footer = new LinkedHashMap<>();

    /**
     * Adds a UI element to the header section of the EyeSpy HUD. This is where the item name is displayed.
     *
     * @param element The element to add to the HUD.
     */
    public void header(UIElement element) {
        this.header(element, false);
    }

    /**
     * Adds a UI element to the header section of the EyeSpy HUD. This is where the item name is displayed.
     *
     * @param element    The element to add to the HUD.
     * @param canReplace Determines if existing UI elements with the same ID should be overridden. This should only be
     *                   done if you intentionally want to replace a HUD element, not just adding new ones. Conflicting
     *                   element IDs can often indicate a plugin is not using the correct ID.
     */
    public void header(UIElement element, boolean canReplace) {
        addElement(this.header, element, canReplace);
    }

    /**
     * Adds a UI element to the body section of the EyeSpy HUD. This is where most information is displayed, like bench
     * tiers, chest inventories, and processing bench progress.
     *
     * @param element The element to add to the HUD.
     */
    public void body(UIElement element) {
        this.body(element, false);
    }

    /**
     * Adds a UI element to the body section of the EyeSpy HUD. This is where most information is displayed, like bench
     * tiers, chest inventories, and processing bench progress.
     *
     * @param element    The element to add to the HUD.
     * @param canReplace Determines if existing UI elements with the same ID should be overridden. This should only be
     *                   done if you intentionally want to replace a HUD element, not just adding new ones. Conflicting
     *                   element IDs can often indicate a plugin is not using the correct ID.
     */
    public void body(UIElement element, boolean canReplace) {
        addElement(this.body, element, canReplace);
    }

    /**
     * Adds a UI element to the footer section of the EyeSpy HUD. This is where the mod/owner of a block is displayed.
     *
     * @param element The element to add to the HUD.
     */
    public void footer(UIElement element) {
        this.footer(element, false);
    }

    /**
     * Adds a UI element to the footer section of the EyeSpy HUD. This is where the mod/owner of a block is displayed.
     *
     * @param element    The element to add to the HUD.
     * @param canReplace Determines if existing UI elements with the same ID should be overridden. This should only be
     *                   done if you intentionally want to replace a HUD element, not just adding new ones. Conflicting
     *                   element IDs can often indicate a plugin is not using the correct ID.
     */
    public void footer(UIElement element, boolean canReplace) {
        this.addElement(this.footer, element, canReplace);
    }

    /**
     * Sets the icon of the HUD. This is a special element that sits on the left side of the information. For example,
     * when looking at some blocks you will see the item icon of the block.
     *
     * @param icon The icon to display on the HUD.
     */
    public void icon(UIElement icon) {
        this.icon = icon;
    }

    public boolean canDisplay() {
        return this.icon != null || !header.isEmpty() || !body.isEmpty() || !footer.isEmpty();
    }

    /**
     * Defines an element for a section of the HUD. If the element already exists, canReplace must be set to true
     * otherwise errors will be logged and the element will NOT be added.
     *
     * @param elements   The map for the section of elements to map to.
     * @param element    The element to add to the section.
     * @param canReplace Should an existing element be replaced if it exists?
     */
    private void addElement(Map<Identifier, UIElement> elements, UIElement element, boolean canReplace) {
        if (!canReplace && elements.containsKey(element.id)) {
            EyeSpy.LOGGER.atWarning().withCause(new Throwable()).log("Element with ID '%s' already exists on the HUD. If this is intentional, set canReplace to true!", element.id.toString());
            return;
        }
        elements.put(element.id, element);
    }

    /**
     * Builds the info and appends it to the UI.
     *
     * @param anchor The position of the HUD.
     * @param ui     Builder for the UI.
     */
    public void build(AnchorProperties anchor, UICommandBuilder ui) {
        this.header.values().forEach(e -> e.appendUI(ui, anchor, "#Info"));
        this.body.values().forEach(e -> e.appendUI(ui, anchor, "#Info"));
        this.footer.values().forEach(e -> e.appendUI(ui, anchor, "#Info"));
        if (this.icon != null) {
            ui.appendInline("#IconContainer", this.icon.build());
        }
        anchor.ensureHeight(120);
    }
}