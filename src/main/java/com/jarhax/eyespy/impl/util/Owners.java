package com.jarhax.eyespy.impl.util;

import com.hypixel.hytale.assetstore.AssetPack;
import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.server.core.asset.AssetModule;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.jarhax.eyespy.EyeSpy;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * This class provides information about which pack/plugin added a given piece of a content.
 */
public class Owners {

    private static final Map<String, PluginIdentifier> BLOCKS = new HashMap<>();
    private static final Map<String, PluginIdentifier> NPCS = new HashMap<>();

    /**
     * Reloads the ownership cache, adding support for new entries.
     */
    public static void reload() {
        reloadBlocks();
        reloadNPCS();
    }

    private static Set<String> findNPCFiles(AssetPack pack) {
        final Path path = pack.getPackLocation();
        final Set<String> fileIds = new HashSet<>();
        try {
            if (Files.isRegularFile(path) && path.toString().toLowerCase(Locale.ROOT).endsWith(".zip")) {
                try (FileSystem zipFs = FileSystems.newFileSystem(path, (ClassLoader) null)) {
                    findNPCFiles(zipFs.getPath("/Server/NPC/Roles"), fileIds);
                }
            }
            else if (Files.isDirectory(path)) {
                findNPCFiles(path.resolve("Server/NPC/Roles"), fileIds);
            }
        }
        catch (IOException e) {
            EyeSpy.LOGGER.atSevere().withCause(e).log("Failed to read NPCs from pack '%s'.");
            throw new RuntimeException("Failed to scan path: " + path, e);
        }
        return fileIds;
    }

    private static void findNPCFiles(Path root, Set<String> fileIds) throws IOException {
        if (Files.exists(root)) {
            try (Stream<Path> paths = Files.walk(root)) {
                paths.filter(Files::isRegularFile).filter(p -> p.toString().toLowerCase(Locale.ROOT).endsWith(".json")).forEach(p -> {
                    final String fileName = p.getFileName().toString();
                    fileIds.add(fileName.substring(0, fileName.length() - 5));
                });
            }
        }
    }

    private static void reloadBlocks() {
        final long start = System.nanoTime();
        BLOCKS.clear();
        for (AssetPack pack : AssetModule.get().getAssetPacks()) {
            final PluginIdentifier identifier = new PluginIdentifier(pack.getManifest().getGroup(), pack.getManifest().getName());
            final Set<String> blockTypeKeys = BlockType.getAssetMap().getKeysForPack(pack.getName());
            if (blockTypeKeys != null) {
                for (String entry : blockTypeKeys) {
                    BLOCKS.put(entry, identifier);
                }
            }
        }
        final long end = System.nanoTime();
        EyeSpy.LOGGER.atInfo().log("Determined owners for %d blocks. Took %fms", BLOCKS.size(), (end - start) / 1_000_000f);
    }

    private static void reloadNPCS() {
        final long start = System.nanoTime();
        NPCS.clear();
        for (AssetPack pack : AssetModule.get().getAssetPacks()) {
            final PluginIdentifier identifier = new PluginIdentifier(pack.getManifest().getGroup(), pack.getManifest().getName());
            for (String npc : findNPCFiles(pack)) {
                NPCS.put(npc, identifier);
            }
        }
        final long end = System.nanoTime();
        EyeSpy.LOGGER.atInfo().log("Determined owners for %d NPCs. Took %fms", NPCS.size(), (end - start) / 1_000_000f);
    }

    /**
     * Gets the owner for a block if we know it.
     *
     * @param blockId The block ID to lookup.
     * @return The owner of the block, if it is known.
     */
    @Nullable
    public static PluginIdentifier blockOwner(String blockId) {
        return BLOCKS.get(blockId);
    }

    /**
     * Gets the owner of an NPC if we know it.
     *
     * @param npcId The NPC role ID to lookup.
     * @return The owner of the NPC, if it is known.
     */
    @Nullable
    public static PluginIdentifier npcOwner(String npcId) {
        return NPCS.get(npcId);
    }
}