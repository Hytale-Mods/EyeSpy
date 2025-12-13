package com.jarhax.eyespy;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemTranslationProperties;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.meta.BlockState;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.FillerBlockUtil;
import com.hypixel.hytale.server.core.util.TargetUtil;

import javax.annotation.Nonnull;

public class EyeSpyHud extends CustomUIHud {

    private Message labelText = null;

    public EyeSpyHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    public void updateHud(float dt,
                          int index,
                          @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
                          @Nonnull Store<EntityStore> store,
                          @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        labelText = null;
        Holder<EntityStore> holder = EntityUtils.toHolder(index, archetypeChunk);
        Player player = holder.getComponent(Player.getComponentType());

        if (player != null && player.getWorld() != null) {
            Vector3i targetBlock = TargetUtil.getTargetBlock(archetypeChunk.getReferenceTo(index), 5, commandBuffer);

            if (targetBlock != null) {
                WorldChunk chunk = archetypeChunk.getComponent(index, TransformComponent.getComponentType()).getChunk();
                int block = chunk.getBlock(targetBlock);

                int x = targetBlock.getX();
                int y = targetBlock.getY();
                int z = targetBlock.getZ();

                int filler = chunk.getFiller(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ());
                if (filler != 0) {
                    x -= FillerBlockUtil.unpackX(filler);
                    y -= FillerBlockUtil.unpackY(filler);
                    z -= FillerBlockUtil.unpackZ(filler);
                }
                BlockState state = chunk.getState(x, y, z);
                BlockType asset = BlockType.getAssetMap().getAsset(block);
                if (asset != null) {
                    labelText = getDisplayName(asset);
                }
            }
        }
    }

    @Override
    protected void build(@Nonnull UICommandBuilder commandBuilder) {
        // TODO we shouldn't need the empty document or rawtext check, but upstream issues cause a crash.
        if (this.labelText != null && !this.labelText.toDocument().isEmpty() && !this.labelText.toDocument().isString("RawText")) {
            commandBuilder.append("Hud/Test.ui");
            commandBuilder.set("#MyLabel.Text", this.labelText);
        }
    }

    @Override
    public void update(boolean clear, @Nonnull UICommandBuilder commandBuilder) {
        super.update(clear, commandBuilder);

    }

    private static Message getDisplayName(BlockType type) {
        final Item item = type.getItem();
        if (item != null) {
            final ItemTranslationProperties translations = item.getTranslationProperties();
            if (translations != null) {
                final String nameKey = translations.getName();
                if (nameKey != null) {
                    return Message.translation(nameKey);
                }
            }
        }
        return Message.raw(type.getId());
    }
}
