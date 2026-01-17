package com.jarhax.eyespy.impl.info;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entity.component.DisplayNameComponent;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.api.context.EntityContext;
import com.jarhax.eyespy.api.info.InfoBuilder;
import com.jarhax.eyespy.api.info.InfoProvider;
import com.jarhax.eyespy.api.info.values.LabelValue;

public class VanillaEntityInfoProvider implements InfoProvider<EntityContext> {

    @Override
    public void updateDescription(EntityContext context, InfoBuilder infoBuilder) {

        final Store<EntityStore> store = context.getStore();

        //Get Model Component and Asset ID to filter out invisible path marker entities
        ModelComponent modelComponent = store.getComponent(context.entity(), ModelComponent.getComponentType());
        String modelAssetId = modelComponent.getModel().getModelAssetId();
        if (modelAssetId.equals("NPC_Path_Marker")) {
            return;
        }

        final DisplayNameComponent displayName = store.getComponent(context.entity(), DisplayNameComponent.getComponentType());
        if (displayName != null) {
            infoBuilder.set("Header", s -> new LabelValue(s, displayName.getDisplayName(), 24));
        }
        final EntityStatMap stats = store.getComponent(context.entity(), EntityStatMap.getComponentType());
        if (stats != null) {
            final int statIndex = EntityStatType.getAssetMap().getIndex("Health");
            final EntityStatValue entityStatValue = stats.get(statIndex);
            if (entityStatValue != null) {
                infoBuilder.set("Health", s -> new LabelValue(s, Message.join(Message.translation("client.itemTooltip.stats.Health"), Message.raw(" %s/%s".formatted(entityStatValue.get(), entityStatValue.getMax())))).setHeight(18 * 3));
            }
        }
    }
}
