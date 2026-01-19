package com.jarhax.eyespy.api.context;

import com.hypixel.hytale.builtin.path.PathPlugin;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;

import javax.annotation.Nullable;

public class EntityContext extends Context {

    private final Ref<EntityStore> entity;

    public EntityContext(float delta, int index, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, PlayerRef observer, WorldChunk chunk, Ref<EntityStore> entity) {
        super(delta, index, archetypeChunk, store, commandBuffer, observer, chunk);
        this.entity = entity;
    }

    public Ref<EntityStore> entity() {
        return entity;
    }

    @Nullable
    public static EntityContext create(PlayerRef player, float dt, int index, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer) {
        Ref<EntityStore> playerRef = player.getReference();
        if (playerRef == null) {
            return null;
        }
        Store<EntityStore> playerStore = playerRef.getStore();
        World world = store.getExternalData().getWorld();
        final Ref<EntityStore> targetEntity = TargetUtil.getTargetEntity(archetypeChunk.getReferenceTo(index), commandBuffer);
        TransformComponent transform = playerStore.getComponent(playerRef, TransformComponent.getComponentType());

        if (transform == null) {
            return null;
        }
        Vector3d position = transform.getPosition();

        if (targetEntity != null && store.getComponent(targetEntity, ModelComponent.getComponentType()) instanceof ModelComponent mc) {
            if (mc.getModel().equals(PathPlugin.get().getPathMarkerModel())) {
                return null;
            }
            int X = (int) Math.floor(position.x);
            int Z = (int) Math.floor(position.z);
            long chunkIndex = ChunkUtil.indexChunkFromBlock(X, Z);
            WorldChunk chunk = world.getChunkIfLoaded(chunkIndex);
            return new EntityContext(dt, index, archetypeChunk, store, commandBuffer, player, chunk, targetEntity);
        }

        return null;
    }
}
