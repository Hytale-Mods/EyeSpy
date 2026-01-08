package com.jarhax.eyespy.api.context;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class Context {

    private final float delta;
    private final int index;
    private final ArchetypeChunk<EntityStore> archetypeChunk;
    private final Store<EntityStore> store;
    private final CommandBuffer<EntityStore> commandBuffer;

    private final Player observer;
    private final WorldChunk chunk;

    public Context(float delta, int index, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, Player observer, WorldChunk chunk) {
        this.delta = delta;
        this.index = index;
        this.archetypeChunk = archetypeChunk;
        this.store = store;
        this.commandBuffer = commandBuffer;
        this.observer = observer;
        this.chunk = chunk;
    }

    public float getDelta() {
        return delta;
    }

    public ArchetypeChunk<EntityStore> getArchetypeChunk() {
        return archetypeChunk;
    }

    public int getIndex() {
        return index;
    }

    public Store<EntityStore> getStore() {
        return store;
    }

    public Player getObserver() {
        return observer;
    }

    public CommandBuffer<EntityStore> getCommandBuffer() {
        return commandBuffer;
    }

    public Player observer() {
        return this.observer;
    }

    public WorldChunk getChunk() {
        return chunk;
    }
}
