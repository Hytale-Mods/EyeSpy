package com.jarhax.eyespy.api.context;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.meta.BlockState;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.FillerBlockUtil;
import com.hypixel.hytale.server.core.util.TargetUtil;

import javax.annotation.Nullable;

public class BlockContext extends Context {

    private final BlockType block;
    private final BlockState state;
    private final Vector3i targetPos;
    private final Vector3i offsetPos;

    public BlockContext(float delta, int index, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, Player observer, WorldChunk chunk, Vector3i targetPos, Vector3i offsetPos, BlockType block, BlockState state) {
        super(delta, index, archetypeChunk, store, commandBuffer, observer, chunk);
        this.targetPos = targetPos;
        this.offsetPos = offsetPos;
        this.block = block;
        this.state = state;
    }

    public BlockType getBlock() {
        return block;
    }

    @Nullable
    public BlockState getState() {
        return state;
    }

    public Vector3i getTargetPos() {
        return targetPos;
    }

    public Vector3i getOffsetPos() {
        return offsetPos;
    }

    @Nullable
    public static BlockContext create(Player player, float dt, int index, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer) {
        final TransformComponent transform = archetypeChunk.getComponent(index, TransformComponent.getComponentType());
        if (transform != null) {
            final WorldChunk chunk = transform.getChunk();
            if (chunk != null) {
                final Vector3i targetBlockPos = TargetUtil.getTargetBlock(archetypeChunk.getReferenceTo(index), 5, commandBuffer);
                if (targetBlockPos != null) {
                    final Vector3i offsetBlockPos = targetBlockPos.clone();
                    final int blockTypeId = chunk.getBlock(targetBlockPos);
                    // TODO should we offset this by filler?
                    final BlockType block = BlockType.getAssetMap().getAsset(blockTypeId);
                    if (block != null) {
                        int packedFiller = chunk.getFiller(targetBlockPos.getX(), targetBlockPos.getY(), targetBlockPos.getZ());
                        if (packedFiller != 0) {
                            offsetBlockPos.subtract(FillerBlockUtil.unpackX(packedFiller), FillerBlockUtil.unpackY(packedFiller), FillerBlockUtil.unpackZ(packedFiller));
                        }
                        final BlockState state = chunk.getState(offsetBlockPos.x, offsetBlockPos.y, offsetBlockPos.z);
                        return new BlockContext(dt, index, archetypeChunk, store, commandBuffer, player, chunk, targetBlockPos, offsetBlockPos, block, state);
                    }
                }
            }
        }
        return null;
    }
}