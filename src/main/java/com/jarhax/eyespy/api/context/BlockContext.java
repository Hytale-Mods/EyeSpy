package com.jarhax.eyespy.api.context;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.meta.BlockState;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.FillerBlockUtil;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.jarhax.eyespy.api.EyeSpyConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Holds context about the game, the observer, and the block that is being observed.
 */
public class BlockContext extends Context {

    private final WorldChunk chunk;
    private final BlockType block;
    private final BlockState state;
    private final Ref<ChunkStore> ref;
    private final Vector3i targetPos;
    private final Vector3i blockPos;

    public BlockContext(float delta, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, PlayerRef observer, WorldChunk chunk, EyeSpyConfig config, Vector3i targetPos, Vector3i offsetPos, BlockType block, BlockState state, Ref<ChunkStore> blockRef) {
        super(delta, store, commandBuffer, observer, config);
        this.chunk = chunk;
        this.targetPos = targetPos;
        this.blockPos = offsetPos;
        this.block = block;
        this.state = state;
        this.ref = blockRef;
    }

    /**
     * Gets the chunk that the base block is within.
     *
     * @return The chunk that the base block is within.
     */
    public WorldChunk chunk() {
        return chunk;
    }

    /**
     * Gets the block type of the base block.
     *
     * @return The chunk type of the base block.
     */
    public BlockType blockType() {
        return block;
    }

    /**
     * Gets the state of the base block if it has one.
     *
     * @return The state of the base block or null if it does not have one.
     */
    @Nullable
    public BlockState blockState() {
        return state;
    }

    /**
     * Gets a reference for the base block.
     *
     * @return The reference for the base block.
     */
    @Nullable
    public Ref<ChunkStore> blockRef() {
        return this.ref;
    }

    /**
     * Gets the position of the base block.
     *
     * @return The position of the base block.
     */
    public Vector3i blockPos() {
        return blockPos;
    }

    /**
     * Gets the position being observed by the player.
     *
     * @return The position being observed by the player.
     */
    public Vector3i targetPos() {
        return targetPos;
    }

    /**
     * Gets a component from the base block, if it has one.
     *
     * @param componentType The type of component to look for.
     * @param <T>           The type of component to look for.
     * @return The component for the base block, or null if it did not have one.
     */
    @Nullable
    public <T extends Component<ChunkStore>> T component(@Nonnull ComponentType<ChunkStore, T> componentType) {
        return this.blockRef() != null && this.blockRef().isValid() ? this.world().getChunkStore().getStore().getComponent(this.ref, componentType) : null;
    }

    @Nullable
    public static BlockContext create(PlayerRef player, float dt, int index, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, EyeSpyConfig config) {
        final Ref<EntityStore> playerRef = player.getReference();
        if (playerRef != null) {
            final Store<EntityStore> playerStore = playerRef.getStore();
            final TransformComponent transform = playerStore.getComponent(playerRef, TransformComponent.getComponentType());
            final World world = store.getExternalData().getWorld();
            final Vector3i targetBlockPos = TargetUtil.getTargetBlock(archetypeChunk.getReferenceTo(index), 5, commandBuffer);
            if (transform != null && targetBlockPos != null) {
                final long targetChunkIndex = ChunkUtil.indexChunkFromBlock(targetBlockPos.x, targetBlockPos.z);
                final WorldChunk targetChunk = world.getChunkIfLoaded(targetChunkIndex);
                if (targetChunk != null) {
                    final Vector3i rootPos = resolveBaseBlock(targetChunk, targetBlockPos);
                    final long rootChunkIndex = ChunkUtil.indexChunkFromBlock(rootPos.x, rootPos.z);
                    final WorldChunk rootChunk = rootChunkIndex == targetChunkIndex ? targetChunk : world.getChunkIfLoaded(rootChunkIndex);
                    if (rootChunk != null) {
                        final Ref<ChunkStore> blockRef = rootChunk.getBlockComponentEntity(rootPos.x, rootPos.y, rootPos.z);
                        final BlockType block = rootChunk.getBlockType(rootPos.x, rootPos.y, rootPos.z);
                        final BlockState state = rootChunk.getState(rootPos.x, rootPos.y, rootPos.z);
                        return new BlockContext(dt, store, commandBuffer, player, rootChunk, config, targetBlockPos, rootPos, block, state, blockRef);
                    }
                }
            }
        }
        return null;
    }

    private static Vector3i resolveBaseBlock(WorldChunk chunk, Vector3i pos) {
        final int filler = chunk.getFiller(pos.x, pos.y, pos.z);
        return filler == 0 ? pos.clone() : new Vector3i(pos.x - FillerBlockUtil.unpackX(filler), pos.y - FillerBlockUtil.unpackY(filler), pos.z - FillerBlockUtil.unpackZ(filler));
    }
}