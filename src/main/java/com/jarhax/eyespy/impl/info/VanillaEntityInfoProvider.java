package com.jarhax.eyespy.impl.info;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entity.component.DisplayNameComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.jarhax.eyespy.api.context.EntityContext;
import com.jarhax.eyespy.api.info.InfoBuilder;
import com.jarhax.eyespy.api.info.InfoProvider;
import com.jarhax.eyespy.api.info.values.LabelValue;
import com.jarhax.eyespy.impl.util.Owners;

import java.awt.*;
import java.util.Objects;

public class VanillaEntityInfoProvider implements InfoProvider<EntityContext> {

    private static final Color color = new Color(85, 85, 255);
    private static final Color color2 = new Color(170, 170, 170);

    @Override
    public void updateDescription(EntityContext context, InfoBuilder infoBuilder) {

        final Store<EntityStore> store = context.getStore();
        final DisplayNameComponent displayName = store.getComponent(context.entity(), DisplayNameComponent.getComponentType());
        if (displayName != null) {
            infoBuilder.set("Header", s -> new LabelValue(s, displayName.getDisplayName(), 24));
        }
        final EntityStatMap stats = store.getComponent(context.entity(), EntityStatMap.getComponentType());
        if (stats != null) {
            final int statIndex = EntityStatType.getAssetMap().getIndex("Health");
            final EntityStatValue entityStatValue = stats.get(statIndex);
            if (entityStatValue != null) {
                infoBuilder.set("Health", s -> new LabelValue(s, Message.translation("client.itemTooltip.stats.Health").param("value", Message.raw("%s/%s".formatted(entityStatValue.get(), entityStatValue.getMax())))).setHeight(18 * 3));
            }
        }

        final NPCEntity npcInfo = store.getComponent(context.entity(), Objects.requireNonNull(NPCEntity.getComponentType()));
        if (npcInfo != null) {
            // Owner Info
            final PluginIdentifier owner = Owners.npcOwner(npcInfo.getNPCTypeId());
            if (owner != null) {
                infoBuilder.set("Footer", s -> new LabelValue(s, Message.raw(owner.getName()).color(color).bold(true)));
            }
        }
    }
}
