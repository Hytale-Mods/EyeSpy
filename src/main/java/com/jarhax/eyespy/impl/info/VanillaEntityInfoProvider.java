package com.jarhax.eyespy.impl.info;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entity.component.DisplayNameComponent;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.jarhax.eyespy.api.context.EntityContext;
import com.jarhax.eyespy.api.info.InfoBuilder;
import com.jarhax.eyespy.api.info.InfoProvider;
import com.jarhax.eyespy.api.ui.Label;
import com.jarhax.eyespy.api.util.Identifier;
import com.jarhax.eyespy.impl.util.Owners;

import java.util.Objects;

public class VanillaEntityInfoProvider implements InfoProvider<EntityContext> {

    public static final Identifier NAME = Identifier.EYE_SPY.of("Name");
    public static final Identifier HEALTH = Identifier.EYE_SPY.of("Health");
    public static final Identifier OWNER = Identifier.EYE_SPY.of("Owner");

    @Override
    public void provideInfo(EntityContext context, InfoBuilder info) {
        final DisplayNameComponent displayName = context.component(DisplayNameComponent.getComponentType());
        if (displayName != null) {
            info.header(new Label(NAME, displayName.getDisplayName(), 24));
        }
        final EntityStatMap stats = context.component(EntityStatMap.getComponentType());
        if (stats != null) {
            final EntityStatValue health = stats.get(EntityStatType.getAssetMap().getIndex("Health"));
            if (health != null) {
                info.body(new Label(HEALTH, Message.translation("server.eyespy.health").param("current", ROUNDED_1.format(health.get())).param("max", ROUNDED_1.format(health.getMax())).color(INFO_COLOR)));
            }
        }
        if (context.component(Objects.requireNonNull(NPCEntity.getComponentType())) instanceof NPCEntity entity) {
            // Owner Info
            final PluginIdentifier owner = Owners.npcOwner(entity.getNPCTypeId());
            if (owner != null) {
                info.footer(new Label(OWNER, Message.raw(owner.getName()).color(OWNER_COLOR).bold(true)));
            }
        }
    }
}