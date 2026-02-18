package com.jarhax.eyespy.impl.info;

import com.hypixel.hytale.builtin.adventure.teleporter.component.Teleporter;
import com.hypixel.hytale.builtin.crafting.state.BenchState;
import com.hypixel.hytale.builtin.crafting.state.ProcessingBenchState;
import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemTranslationProperties;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.meta.state.ItemContainerState;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.jarhax.eyespy.api.context.BlockContext;
import com.jarhax.eyespy.api.info.InfoBuilder;
import com.jarhax.eyespy.api.info.InfoProvider;
import com.jarhax.eyespy.api.ui.Group;
import com.jarhax.eyespy.api.ui.ItemGrid;
import com.jarhax.eyespy.api.ui.ItemIcon;
import com.jarhax.eyespy.api.ui.Label;
import com.jarhax.eyespy.api.ui.ProgressBar;
import com.jarhax.eyespy.api.ui.UIElement;
import com.jarhax.eyespy.api.util.Identifier;
import com.jarhax.eyespy.impl.util.Owners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VanillaBlockInfoProvider implements InfoProvider<BlockContext> {

    public static final Identifier ICON = Identifier.EYE_SPY.of("Icon");
    public static final Identifier NAME = Identifier.EYE_SPY.of("Name");
    public static final Identifier TELEPORTER_NAME = Identifier.EYE_SPY.of("TeleporterName");
    public static final Identifier TELEPORTER_POS = Identifier.EYE_SPY.of("TeleporterPos");
    public static final Identifier BENCH = Identifier.EYE_SPY.of("BenchInfo");
    public static final Identifier BENCH_TIER = Identifier.EYE_SPY.of("BenchTier");
    public static final Identifier BENCH_PROGRESS = Identifier.EYE_SPY.of("BenchProgress");
    public static final Identifier CONTAINER_INVENTORY = Identifier.EYE_SPY.of("ContainerInventory");
    public static final Identifier OWNER = Identifier.EYE_SPY.of("Owner");

    @Override
    public void provideInfo(BlockContext context, InfoBuilder info) {
        if (!context.blockType().getId().equals("Empty")) {
            // Block Name
            info.header(new Label(NAME, getDisplayName(context.blockType()), 24));

            // Block Icon
            final Item item = context.blockType().getItem();
            if (item != null) {
                info.icon(new ItemIcon(ICON, item.getId()));
            }
            
            // Teleporter Logic (Fixing context access)
            BlockComponentChunk blockComponentChunk = context.chunk().getBlockComponentChunk();
            // Assuming BlockContext has blockPos() which returns the position
            int blockIndex = ChunkUtil.indexBlockInColumn(context.blockPos().x, context.blockPos().y, context.blockPos().z);
            Ref<ChunkStore> blockRef = blockComponentChunk.getEntityReference(blockIndex);
            
            if (blockRef != null && blockRef.isValid()) {
                World world = context.world(); // context.world() existiert in der Superklasse
                ChunkStore chunkStore = world.getChunkStore();

                if (chunkStore.getStore().getComponent(blockRef, Teleporter.getComponentType()) instanceof Teleporter teleporter) {
                    if (teleporter.getOwnedWarp() != null) {
                        info.body(new Label(TELEPORTER_NAME, Message.translation("server.eyespy.name").param("name", teleporter.getOwnedWarp()).color(INFO_COLOR)));
                    }
                    if (teleporter.getWarp() != null) {
                        info.body(new Label(TELEPORTER_POS, Message.translation("server.eyespy.destination").param("destination", teleporter.getWarp()).color(INFO_COLOR)));
                    }
                }
            }

            // Bench Info
            if (context.blockState() instanceof BenchState bench) {
                final List<UIElement> values = new ArrayList<>();
                values.add(new Label(BENCH_TIER, Message.translation("server.eyespy.tier").param("tier", bench.getTierLevel()).color(INFO_COLOR)));
                if (bench instanceof ProcessingBenchState processor && context.config().showProcessingTimes() && processor.isActive() && processor.getRecipe() != null && !Float.isNaN(processor.getInputProgress()) && !Float.isNaN(processor.getRecipe().getTimeSeconds())) {
                    values.add(new ProgressBar(BENCH_PROGRESS, processor.getInputProgress() / processor.getRecipe().getTimeSeconds()));
                }
                info.body(new Group(BENCH, values));
            }

            // Container Info
            if (context.blockState() instanceof ItemContainerState containerState && context.config().showContainers()) {
                final ItemContainer container = containerState.getItemContainer();
                final Map<String, Integer> stackCounts = new HashMap<>();
                for (short slot = 0; slot < container.getCapacity(); slot++) {
                    final ItemStack stack = container.getItemStack(slot);
                    if (stack != null && stack.isValid()) {
                        stackCounts.merge(stack.getItemId(), stack.getQuantity(), Integer::sum);
                    }
                }
                info.body(new ItemGrid(CONTAINER_INVENTORY, stackCounts.entrySet().stream().map(e -> new ItemStack(e.getKey(), e.getValue())).toList()));
            }

            // Owner Info
            final PluginIdentifier owner = Owners.blockOwner(context.blockType().getId());
            if (owner != null) {
                // Fixed getName() call. PluginIdentifier.getName() returns a String, not an object with getName().
                info.footer(new Label(OWNER, Message.raw(owner.getName()).color(OWNER_COLOR).bold(true)));
            }
        }
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
