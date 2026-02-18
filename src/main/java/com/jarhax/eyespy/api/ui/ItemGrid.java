package com.jarhax.eyespy.api.ui;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.jarhax.eyespy.api.util.Identifier;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The item grid element represents an inventory being displayed on the screen.
 */
public class ItemGrid extends UIElement {

    /**
     * The list of items to display.
     */
    private final List<ItemStack> stacks;

    /**
     * The amount of items for each row.
     */
    private final int rowSize;

    /**
     * The amount of rows being displayed.
     */
    private final int rows;

    /**
     * Determines if item quality backgrounds should be displayed.
     */
    private final boolean showQuality;

    /**
     * @param stacks The items to display in the grid.
     */
    public ItemGrid(Identifier id, ItemStack... stacks) {
        this(id, 9, true, Arrays.asList(stacks));
    }

    /**
     * @param id     A unique ID for the element.
     * @param stacks The items to display in the grid.
     */
    public ItemGrid(Identifier id, List<ItemStack> stacks) {
        this(id, 9, true, stacks);
    }

    /**
     * @param id          A unique ID for the element.
     * @param rowSize     The amount of items to display on each row.
     * @param showQuality Should quality backgrounds be displayed?
     * @param stacks      The items to display in the grid.
     */
    public ItemGrid(Identifier id, int rowSize, boolean showQuality, List<ItemStack> stacks) {
        super(id);
        this.stacks = sanitizeStacks(stacks);
        this.rowSize = rowSize;
        this.rows = (int) Math.ceil((double) stacks.size() / this.rowSize);
        this.showQuality = showQuality;
    }

    @Override
    public String build() {
        return """
                ItemGrid #%s {
                    SlotsPerRow: %d;
                    RenderItemQualityBackground: %s;
                    InfoDisplay: None;
                    Style: (SlotSize: 32, SlotSpacing: 0, SlotIconSize: 32);
                }
                """.formatted(this.id.ui(), this.rowSize, Boolean.toString(this.showQuality));
    }

    @Override
    public void appendUI(@Nonnull UICommandBuilder ui, @Nonnull AnchorProperties anchor, String parent) {
        super.appendUI(ui, anchor, parent);
        ui.set("#%s.ItemStacks".formatted(this.id.ui()), stacks);
    }

    @Override
    public int height() {
        return this.rows * 40;
    }

    private static List<ItemStack> sanitizeStacks(List<ItemStack> input) {
        final List<ItemStack> items = new LinkedList<>();
        for (ItemStack stack : input) {
            // Items with metadata and durability can not be properly displayed by the Hytale client yet?
            items.add(stack.withMetadata(null).withDurability(stack.getMaxDurability() > 0 ? Math.max(1.0, stack.getDurability()) : stack.getDurability()));
        }
        return items;
    }
}
