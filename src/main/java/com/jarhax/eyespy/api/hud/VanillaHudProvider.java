package com.jarhax.eyespy.api.hud;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomHud;
import com.hypixel.hytale.protocol.packets.interface_.CustomUICommand;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.impl.component.EyeSpyPlayerData;
import com.jarhax.eyespy.impl.hud.EyeSpyHud;
import com.jarhax.eyespy.impl.util.Reflect;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.HashMap;
import java.util.Map;

public class VanillaHudProvider implements HudProvider {

    private final Map<PlayerRef, EyeSpyHud> huds = new HashMap<>();

    @Override
    public void showHud(float dt, int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        final Holder<EntityStore> holder = EntityUtils.toHolder(index, archetypeChunk);
        final Player player = holder.getComponent(Player.getComponentType());
        final PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
        if (player == null || playerRef == null) {
            return;
        }
        EyeSpyPlayerData eyeSpyComponent = EyeSpyPlayerData.getSaveData(holder);
        boolean canShow = eyeSpyComponent.visible() && (eyeSpyComponent.showInBackground() || (player.getWindowManager().getWindows().isEmpty() && player.getPageManager().getCustomPage() == null));

        if (canShow) {
            if (!huds.containsKey(playerRef)) {
                EyeSpyHud value = new EyeSpyHud(playerRef);
                huds.put(playerRef, value);
                value.updateHud(dt, index, archetypeChunk, store, commandBuffer);
                player.getHudManager().setCustomHud(playerRef, value);
            } else {
                EyeSpyHud customUIHud = huds.get(playerRef);
                customUIHud.updateHud(dt, index, archetypeChunk, store, commandBuffer);
                customUIHud.show();
            }
        } else {
            if (huds.containsKey(playerRef)) {
                this.hideHud(dt, index, archetypeChunk, store, commandBuffer);
            }
        }

    }

    @Override
    public void hideHud(float dt, int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        final Holder<EntityStore> holder = EntityUtils.toHolder(index, archetypeChunk);
        final Player player = holder.getComponent(Player.getComponentType());
        final PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
        if (player == null || playerRef == null) {
            return;
        }
        if (huds.containsKey(playerRef)) {
            // Using resetHud or setCustomHud with null appears to crash the client, so we have to do it ourselves
            Reflect.HudManager_.CUSTOM_HUD.accept(player.getHudManager(), null);
            playerRef.getPacketHandler().writeNoCache(new CustomHud(true, new CustomUICommand[0]));
            huds.remove(playerRef);
        }
    }
}
