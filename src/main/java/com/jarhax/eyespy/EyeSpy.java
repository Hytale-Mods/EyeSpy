package com.jarhax.eyespy;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.jarhax.eyespy.api.hud.LayoutMode;
import com.jarhax.eyespy.impl.command.ConfigCommand;
import com.jarhax.eyespy.impl.component.EyeSpyPlayerData;
import com.jarhax.eyespy.impl.hud.PlayerTickSystem;
import com.jarhax.eyespy.impl.util.Owners;
import com.jarhax.eyespy.impl.util.Reflect;

import javax.annotation.Nonnull;

public class EyeSpy extends JavaPlugin {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public EyeSpy(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getEntityStoreRegistry().registerSystem(new PlayerTickSystem());
        this.getCommandRegistry().registerCommand(new ConfigCommand());
        EyeSpyPlayerData.init(this);
    }

    @Override
    protected void start() {
        Owners.reload();
        Reflect.UICommandBuilder_.CODEC_MAP.get().put(LayoutMode.class, LayoutMode.CODEC);
    }
}