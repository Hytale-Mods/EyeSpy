package com.jarhax.eyespy.impl.hud.provider;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.EyeSpy;
import com.jarhax.eyespy.api.util.CachedSupplier;

import javax.annotation.Nonnull;

/**
 * Hud providers allow us to show or hide the EyeSpy HUD. This allows us to support alternative approaches to displaying
 * a HUD like the system added by the MHUD mod.
 */
public interface HudProvider {

    /**
     * The provider currently used by EyeSpy. This value is lazy loaded, and will be cached after the first time it is
     * accessed.
     */
    CachedSupplier<HudProvider> PROVIDER = CachedSupplier.cache(() -> {
        HudProvider provider;
        if (PluginManager.get().getPlugin(PluginIdentifier.fromString("Buuz135:MultipleHUD")) != null) {
            provider = new MultiHudProvider();
        }
        else {
            provider = new VanillaHudProvider();
        }
        EyeSpy.LOGGER.atInfo().log("HUD Provider: %s class='%s'", provider.name(), provider.getClass().getCanonicalName());
        return provider;
    });

    /**
     * Appends the EyeSpy HUD to the game screen.
     *
     * @param delta          The amount of time since the observer was last ticked, in partial ticks.
     * @param index          The index of the observer.
     * @param archetypeChunk The archetype chunk that the observer is part of.
     * @param store          The store of entity components.
     * @param entityBuffer   A buffer for interacting with the entity store.
     */
    void showHud(float delta, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> entityBuffer);

    /**
     * Removes the EyeSpy HUD to the game screen.
     *
     * @param delta          The amount of time since the observer was last ticked, in partial ticks.
     * @param index          The index of the observer.
     * @param archetypeChunk The archetype chunk that the observer is part of.
     * @param store          The store of entity components.
     * @param entityBuffer   A buffer for interacting with the entity store.
     */
    void hideHud(float delta, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> entityBuffer);

    /**
     * Gets the name of the HudProvider. This is used to help debug which provider is used, and may also be used in the
     * future to force which provider is active.
     *
     * @return The name of the HudProvider.
     */
    String name();
}