package com.jarhax.eyespy;

import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.jarhax.eyespy.api.hud.HudProvider;
import com.jarhax.eyespy.api.hud.MultiHudProvider;
import com.jarhax.eyespy.api.hud.VanillaHudProvider;
import com.jarhax.eyespy.impl.hud.PlayerTickSystem;
import com.jarhax.eyespy.impl.util.Owners;

import javax.annotation.Nonnull;

public class EyeSpy extends JavaPlugin {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static HudProvider provider = new VanillaHudProvider();

    public EyeSpy(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        super.setup();
        this.getEntityStoreRegistry().registerSystem(new PlayerTickSystem());
    }

    @Override
    protected void start() {
        PluginBase plugin = PluginManager.get().getPlugin(PluginIdentifier.fromString("Buuz135:MultipleHUD"));
        if (plugin != null) {
            EyeSpy.provider = new MultiHudProvider();
        }
        Owners.reload();
    }
}