package com.jarhax.eyespy.impl.hud;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    public void tick(float dt,
                     int index,
                     @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
                     @Nonnull Store<EntityStore> store,
                     @Nonnull CommandBuffer<EntityStore> commandBuffer) {

        Player player = EntityUtils.toHolder(index, archetypeChunk).getComponent(Player.getComponentType());
        PlayerRef playerRef = player.getPlayerRef();

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
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return query;
    }
}
