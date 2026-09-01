package com.jarhax.eyespy.impl.info;

import com.hypixel.hytale.builtin.adventure.teleporter.component.Teleporter;
import com.hypixel.hytale.builtin.crafting.component.BenchBlock;
import com.hypixel.hytale.builtin.crafting.component.ProcessingBenchBlock;
import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemTranslationProperties;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.modules.block.components.ItemContainerBlock;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.jarhax.eyespy.api.MessageHelpers;
import com.jarhax.eyespy.api.context.BlockContext;
import com.jarhax.eyespy.api.info.InfoBuilder;
import com.jarhax.eyespy.api.info.InfoProvider;
import com.jarhax.eyespy.api.info.values.IconValue;
import com.jarhax.eyespy.api.info.values.ItemGridValue;
import com.jarhax.eyespy.api.info.values.LabelValue;
import com.jarhax.eyespy.api.info.values.ProgressBarValue;
import com.jarhax.eyespy.impl.util.Owners;

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
            Ref<ChunkStore> blockRef = context.getChunk().getBlockComponentEntity(context.getOffsetPos().x, context.getOffsetPos().y, context.getOffsetPos().z);
            if (blockRef != null && blockRef.isValid()) {
                World world = context.getStore().getExternalData().getWorld();
                ChunkStore chunkStore = world.getChunkStore();

                if (chunkStore.getStore().getComponent(blockRef, Teleporter.getComponentType()) instanceof Teleporter teleporter) {
                    if (teleporter.getOwnedWarp() != null) {
                        infoBuilder.set("TeleporterName", s -> new LabelValue(s, Message.raw("Name: " + teleporter.getOwnedWarp())));
                    }
                    if (teleporter.getWarp() != null) {
                        infoBuilder.set("TeleporterDestination", s -> new LabelValue(s, Message.raw("Destination: " + teleporter.getWarp())));
                    }
                }

                if (chunkStore.getStore().getComponent(blockRef, ProcessingBenchBlock.getComponentType()) instanceof ProcessingBenchBlock processor) {
                    if (processor.isActive() && processor.getRecipe() != null && !Float.isNaN(processor.getInputProgress()) && !Float.isNaN(processor.getRecipe().getTimeSeconds())) {
                        float value = processor.getInputProgress() / processor.getRecipe().getTimeSeconds();
                        // If we don't drop tiny values the client crashes
                        infoBuilder.set("ProcessingBenchProgress", s -> new ProgressBarValue(s, Math.round(value * 1000) / 1000f));
                    }
                }
                if (chunkStore.getStore().getComponent(blockRef, BenchBlock.getComponentType()) instanceof BenchBlock bench) {
                    infoBuilder.set("BenchTier", s -> new LabelValue(s, MessageHelpers.tier(bench.getTierLevel()).color(color2)));
                }

                if (chunkStore.getStore().getComponent(blockRef, ItemContainerBlock.getComponentType()) instanceof ItemContainerBlock container) {
                    infoBuilder.set("ItemContainerItems", s -> new ItemGridValue(s, getStacks(container.getItemContainer())));
                }

            }

            final PluginIdentifier owner = Owners.blockOwner(context.getBlock().getId());
            if (owner != null) {
                infoBuilder.set("Footer", s -> new LabelValue(s, Message.raw(owner.getName()).color(color).bold(true)));
            }
        }
    }

    private static List<ItemStack> getStacks(ItemContainer container) {
        List<ItemStack> stacks = new ArrayList<>();
        if (container != null) {
            outer:
            for (short i = 0; i < container.getCapacity(); i++) {
                ItemStack stack = container.getItemStack(i);
                if (stack == null) {
                    continue;
                }
                for (int j = 0; j < stacks.size(); j++) {
                    ItemStack itemStack = stacks.get(j);
                    if (itemStack.isEquivalentType(stack)) {
                        stacks.set(j, itemStack.withQuantity(Math.max(1, saturatingAdd(itemStack.getQuantity(), stack.getQuantity()))));
                        continue outer;
                    }
                }
                // Crash workaround by setting Durability of broken items to 1.0 and removing any Metadata
                // TODO Find actual fix :)
                ItemStack safeStack = stack.withMetadata(null)
                        .withDurability(stack.getMaxDurability() > 0 ? Math.max(1.0, stack.getDurability()) : stack.getDurability());

                stacks.add(safeStack);
            }
        }
        return stacks;
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

    private static int saturatingAdd(int a, int b) {
        long sum = (long) a + b;
        return (sum > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (sum < Integer.MIN_VALUE) ? Integer.MIN_VALUE : (int) sum;
    }
}
