package com.jarhax.eyespy.impl.hud;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.EyeSpy;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class PlayerTickSystem extends EntityTickingSystem<EntityStore> {

    @Nonnull
    private final Query<EntityStore> query;

    private final Map<PlayerRef, EyeSpyHud> huds = new HashMap<>();

    public PlayerTickSystem() {
        this.query = Query.and(Player.getComponentType());
    }

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        final Player player = archetypeChunk.getComponent(index, Player.getComponentType());
        final PlayerRef playerRef = archetypeChunk.getComponent(index, PlayerRef.getComponentType());
        if (player == null || playerRef == null) {
            return;
        }
        EyeSpy.provider.showHud(dt, index, archetypeChunk, store, commandBuffer);
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return query;
    }
}
