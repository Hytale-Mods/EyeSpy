package com.jarhax.eyespy;

import com.hypixel.hytale.assetstore.AssetPack;
import com.hypixel.hytale.assetstore.map.BlockTypeAssetMap;
import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.AssetModule;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.jarhax.eyespy.api.hud.HudProvider;
import com.jarhax.eyespy.api.hud.MultiHudProvider;
import com.jarhax.eyespy.api.hud.VanillaHudProvider;
import com.jarhax.eyespy.commands.EyeSpyCommand;
import com.jarhax.eyespy.impl.hud.PlayerTickSystem;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EyeSpy extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    public static final Map<String, String> OWNERSHIP = new HashMap<>();

    public static HudProvider provider = new VanillaHudProvider();
    private static EyeSpy instance;
    private UserDataManager userDataManager;

    public EyeSpy(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
        userDataManager = new UserDataManager(this.getDataDirectory());
    }

    @Override
    protected void setup() {
        super.setup();
        this.getEntityStoreRegistry().registerSystem(new PlayerTickSystem());
        this.getCommandRegistry().registerCommand(new EyeSpyCommand(this));
    }

    @Override
    protected void start() {
        PluginBase plugin = PluginManager.get().getPlugin(PluginIdentifier.fromString("Buuz135:MultipleHUD"));
        if (plugin != null) {
            EyeSpy.provider = new MultiHudProvider();
        }
        final long start = System.nanoTime();
        OWNERSHIP.clear();
        final BlockTypeAssetMap<String, BlockType> blockTypes = BlockType.getAssetMap();
        for (AssetPack pack : AssetModule.get().getAssetPacks()) {
            final String ownerName = pack.getManifest().getGroup() + ":" + pack.getManifest().getName();
            final Set<String> blockTypeKeys = blockTypes.getKeysForPack(pack.getName());
            if (blockTypeKeys != null) {
                for (String entry : blockTypes.getKeysForPack(pack.getName())) {
                    OWNERSHIP.put(entry, ownerName);
                }
            }
        }
        final long end = System.nanoTime();
        LOGGER.atInfo().log("Determined owners for %d blocks. Took %fms", OWNERSHIP.size(), (end - start) / 1_000_000f);
    }
    
    public static EyeSpy getInstance() {
        return instance;
    }
    
    public UserDataManager getUserDataManager() {
        return userDataManager;
    }
}