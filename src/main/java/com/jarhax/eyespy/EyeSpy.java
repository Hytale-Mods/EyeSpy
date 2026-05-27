package com.jarhax.eyespy;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.jarhax.eyespy.api.hud.HudProvider;
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
        Owners.reload();
    }
}