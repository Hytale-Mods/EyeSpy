package com.jarhax.eyespy.api.context;

import com.hypixel.hytale.builtin.path.PathPlugin;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.jarhax.eyespy.api.EyeSpyConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Holds context about the game, the observer, and the entity being observed.
 */
public class EntityContext extends Context {

    private final Ref<EntityStore> entityRef;

    public EntityContext(float delta, Store<EntityStore> entityStore, CommandBuffer<EntityStore> entityBuffer, PlayerRef observer, EyeSpyConfig config, Ref<EntityStore> entity) {
        super(delta, entityStore, entityBuffer, observer, config);
        this.entityRef = entity;
    }

    /**
     * Gets a reference to the entity being observed.
     *
     * @return The reference to the observed entity.
     */
    public Ref<EntityStore> entityRef() {
        return entityRef;
    }

    /**
     * Gets a component from the observed entity, if the component is available.
     *
     * @param type The type of the component to retrieve.
     * @param <T>  The type of the component to retrieve.
     * @return If the entity has the component it will be returned, otherwise it will be null.
     */
    @Nullable
    public <T extends Component<EntityStore>> T component(@Nonnull ComponentType<EntityStore, T> type) {
        return this.entityStore().getComponent(this.entityRef(), type);
    }

    @Nullable
    public static EntityContext create(PlayerRef player, float dt, int index, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, EyeSpyConfig config) {
        final Ref<EntityStore> playerRef = player.getReference();
        if (playerRef != null) {
            final Store<EntityStore> playerStore = playerRef.getStore();
            final Ref<EntityStore> targetEntity = TargetUtil.getTargetEntity(archetypeChunk.getReferenceTo(index), commandBuffer);
            final TransformComponent transform = playerStore.getComponent(playerRef, TransformComponent.getComponentType());
            if (transform != null && canDisplayEntity(store, targetEntity)) {
                return new EntityContext(dt, store, commandBuffer, player, config, targetEntity);
            }
        }
        return null;
    }

    private static boolean canDisplayEntity(Store<EntityStore> store, Ref<EntityStore> target) {
        return target != null && store.getComponent(target, ModelComponent.getComponentType()) instanceof ModelComponent mc && !mc.getModel().equals(PathPlugin.get().getPathMarkerModel());
    }
}
