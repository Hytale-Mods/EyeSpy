package com.jarhax.eyespy.api.context;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.api.EyeSpyConfig;

/**
 * Holds context about the game, the observer, and what is being observed.
 */
public class Context {

    private final float delta;
    private final Store<EntityStore> entityStore;
    private final CommandBuffer<EntityStore> entityBuffer;
    private final PlayerRef observer;
    private final EyeSpyConfig config;

    public Context(float delta, Store<EntityStore> entityStore, CommandBuffer<EntityStore> entityBuffer, PlayerRef observer, EyeSpyConfig config) {
        this.delta = delta;
        this.entityStore = entityStore;
        this.entityBuffer = entityBuffer;
        this.observer = observer;
        this.config = config;
    }

    /**
     * The amount of time that has passed since the last update, provided as partial amount of game ticks.
     *
     * @return The delta time since the last HUD update.
     */
    public float delta() {
        return delta;
    }

    /**
     * The entity store is responsible for managing entities and their components.
     *
     * @return The entity store.
     */
    public Store<EntityStore> entityStore() {
        return entityStore;
    }

    /**
     * A buffer for read and write operations related to entities.
     *
     * @return The entity buffer.
     */
    public CommandBuffer<EntityStore> entityBuffer() {
        return entityBuffer;
    }

    /**
     * A reference to the observing player. This is the player that the HUD is being built for.
     *
     * @return A reference to the observing player.
     */
    public PlayerRef observer() {
        return this.observer;
    }

    /**
     * An instance of the game world that the observer is currently within.
     *
     * @return The current world instance.
     */
    public World world() {
        return this.entityStore().getExternalData().getWorld();
    }

    /**
     * Access to the EyeSpy configuration system for the observing player.
     *
     * @return The current configuration system.
     */
    public EyeSpyConfig config() {
        return config;
    }
}