package com.jarhax.eyespy.api.hud;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.impl.util.CachedSupplier;

import javax.annotation.Nonnull;

public interface HudProvider {

    CachedSupplier<HudProvider> PROVIDER = CachedSupplier.cache(() -> {
        if (PluginManager.get().getPlugin(PluginIdentifier.fromString("Buuz135:MultipleHUD")) != null) {
            return new MultiHudProvider();
        }
        return new VanillaHudProvider();
    });

    void showHud(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer);

    void hideHud(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer);

}
