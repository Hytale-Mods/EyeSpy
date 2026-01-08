package com.jarhax.eyespy.impl.info;

import com.hypixel.hytale.builtin.crafting.state.BenchState;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemTranslationProperties;
import com.hypixel.hytale.server.core.universe.world.meta.state.ItemContainerState;
import com.jarhax.eyespy.api.info.InfoProvider;
import com.jarhax.eyespy.api.context.BlockContext;
import com.jarhax.eyespy.api.info.InfoBuilder;

import java.awt.*;

public class VanillaBlockInfoProvider implements InfoProvider<BlockContext> {
    @Override
    public void updateDescription(BlockContext context, InfoBuilder infoBuilder) {
        infoBuilder.setHeader(getDisplayName(context.getBlock()));
        infoBuilder.setIcon(context.getBlock().getId());
        infoBuilder.setFooter(Message.raw(context.getBlock().getId()).color(Color.blue.darker()));

        if (context.getState() instanceof BenchState bench) {
            infoBuilder.setBody(Message.raw("Tier: " + bench.getTierLevel()));
        }

        if (context.getState() instanceof ItemContainerState container) {
            infoBuilder.setBody(Message.raw("Slots: " + container.getItemContainer().getCapacity()));
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
