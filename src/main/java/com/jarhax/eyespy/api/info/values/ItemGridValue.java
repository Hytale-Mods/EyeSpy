package com.jarhax.eyespy.api.info.values;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.jarhax.eyespy.api.info.AnchorBuilder;
import com.jarhax.eyespy.api.info.InfoValue;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemGridValue implements InfoValue {

    private final String id;
    private final List<ItemStack> stacks;

    public ItemGridValue(String id, List<ItemStack> stacks) {
        this.id = id;
        this.stacks = stacks;
    }

    @Override
    public void build(@Nonnull UICommandBuilder ui, @Nonnull AnchorBuilder anchorBuilder, String selector) {

        ui.appendInline(selector, """
                ItemGrid #%s {
                        SlotsPerRow: 9;
                        RenderItemQualityBackground: true;
                        InfoDisplay: None;
                        Style: (
                          SlotSize: 32,
                          SlotSpacing: 0,
                          SlotIconSize: 32
                        );
                      }""".formatted(this.id));
        ui.set("#%s.ItemStacks".formatted(this.id), stacks);
        int rows = (int) Math.ceil((double) stacks.size() / 9);
        anchorBuilder.addHeight(rows * 40);
    }
}
