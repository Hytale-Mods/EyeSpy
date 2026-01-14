package com.jarhax.eyespy.impl.info;

import com.hypixel.hytale.builtin.crafting.state.BenchState;
import com.hypixel.hytale.builtin.crafting.state.ProcessingBenchState;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemTranslationProperties;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.meta.state.ItemContainerState;
import com.jarhax.eyespy.EyeSpy;
import com.jarhax.eyespy.api.MessageHelpers;
import com.jarhax.eyespy.api.context.BlockContext;
import com.jarhax.eyespy.api.info.InfoBuilder;
import com.jarhax.eyespy.api.info.InfoProvider;
import com.jarhax.eyespy.api.info.InfoValue;
import com.jarhax.eyespy.api.info.values.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VanillaBlockInfoProvider implements InfoProvider<BlockContext> {

    private static final Color color = new Color(85, 85, 255);
    private static final Color color2 = new Color(170, 170, 170);

    @Override
    public void updateDescription(BlockContext context, InfoBuilder infoBuilder) {
        if (!context.getBlock().getId().equals("Empty")) {
            infoBuilder.set("Header", s -> new LabelValue(s, getDisplayName(context.getBlock()), 24));

            final Item item = context.getBlock().getItem();
            if (item != null) {
                infoBuilder.setIcon(new IconValue(item.getId()));
            }

            if (context.getState() != null) {
                infoBuilder.set("Body", s -> switch (context.getState()) {
                    case ProcessingBenchState processor -> {
                        List<InfoValue> values = new ArrayList<>();
                        values.add(new LabelValue(s + "Tier", MessageHelpers.tier(processor.getTierLevel()).color(color2)));
                        if (processor.isActive() && processor.getRecipe() != null && !Float.isNaN(processor.getInputProgress()) && !Float.isNaN(processor.getRecipe().getTimeSeconds())) {
                            float value = processor.getInputProgress() / processor.getRecipe().getTimeSeconds();
                            // If we don't drop tiny values the client crashes
                            values.add(new ProgressBarValue(s + "Progress", Math.round(value * 1000) / 1000f));
                        }
                        yield new GroupValue(s, values);
                    }
                    case BenchState bench -> new LabelValue(s, MessageHelpers.tier(bench.getTierLevel()).color(color2));
                    case ItemContainerState container -> {

                        List<ItemStack> stacks = new ArrayList<>();
                        outer:
                        for (short i = 0; i < container.getItemContainer().getCapacity(); i++) {
                            ItemStack stack = container.getItemContainer().getItemStack(i);
                            if (stack == null) {
                                continue;
                            }
                            for (int j = 0; j < stacks.size(); j++) {
                                ItemStack itemStack = stacks.get(j);
                                if (itemStack.isEquivalentType(stack)) {
                                    stacks.set(j, itemStack.withQuantity(itemStack.getQuantity() + stack.getQuantity()));
                                    continue outer;
                                }
                            }
                            // Crash workaround by setting Durability of broken items to 1.0 and removing any Metadata
                            // TODO Find actual fix :)
                            ItemStack safeStack = stack.withMetadata(null)
                                    .withDurability(stack.getMaxDurability() > 0 ? Math.max(1.0, stack.getDurability()) : stack.getDurability());

                            stacks.add(safeStack);
                        }
                        yield new ItemGridValue(s, stacks);
                    }
                    default -> InfoValue.EMPTY;
                });
            }

            final String owner = EyeSpy.OWNERSHIP.get(context.getBlock().getId());
            if (owner != null) {
                infoBuilder.set("Footer", s -> new LabelValue(s, Message.raw(owner).color(color).bold(true)));
            }
        }
    }

    private static Message getDisplayName(BlockType type) {
        final Item item = type.getItem();
        if (item != null) {
            final ItemTranslationProperties translations = item.getTranslationProperties();
            if (translations != null) {
                final String nameKey = translations.getName();
                if (nameKey != null) {
                    return Message.translation(nameKey);
                }
            }
        }
        return Message.raw(type.getId());
    }
}
