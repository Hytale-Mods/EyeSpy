package com.jarhax.eyespy.api.hud;

import com.buuz135.mhud.MultipleHUD;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.impl.hud.EyeSpyHud;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.HashMap;
import java.util.Map;

public class MultiHudProvider implements HudProvider {

    private final Map<PlayerRef, EyeSpyHud> huds = new HashMap<>();

    @Override
    public void showHud(float dt, int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        final Holder<EntityStore> holder = EntityUtils.toHolder(index, archetypeChunk);
        final Player player = holder.getComponent(Player.getComponentType());
        final PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
        if (player == null || playerRef == null) {
            return;
        }
        if (!huds.containsKey(playerRef)) {
            EyeSpyHud value = new EyeSpyHud(playerRef);
            huds.put(playerRef, value);
            value.updateHud(dt, index, archetypeChunk, store, commandBuffer);
            MultipleHUD.getInstance().setCustomHud(player, playerRef, "EyeSpy_HUD", value);
        } else {
            EyeSpyHud customUIHud = huds.get(playerRef);
            customUIHud.updateHud(dt, index, archetypeChunk, store, commandBuffer);
            MultipleHUD.getInstance().setCustomHud(player, playerRef, "EyeSpy_HUD", customUIHud);
        }
    }

    @Override
    public void hideHud(int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store) {
        final Holder<EntityStore> holder = EntityUtils.toHolder(index, archetypeChunk);
        final PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
        if (playerRef == null) {
            return;
        }
        // Clear by simulating empty/air view
        if (huds.containsKey(playerRef)) {
            EyeSpyHud hud = huds.get(playerRef);
            hud.clearHud();
        }
    }
}
