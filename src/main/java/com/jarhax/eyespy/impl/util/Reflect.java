package com.jarhax.eyespy.impl.util;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.entity.entities.player.hud.HudManager;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.jarhax.eyespy.EyeSpy;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class Reflect {

    public static class UICommandBuilder_ {
        private static final MethodHandles.Lookup LOOKUP = lookup(UICommandBuilder.class);
        private static final VarHandle _CODEC_MAP = handleStatic(LOOKUP, UICommandBuilder.class, "CODEC_MAP", Map.class);
        public static final Supplier<Map<Class<?>, Codec<?>>> CODEC_MAP = () -> (Map<Class<?>, Codec<?>>) _CODEC_MAP.get();
    }

    public static class HudManager_ {
        private static final MethodHandles.Lookup LOOKUP = lookup(HudManager.class);
        private static final VarHandle _CUSTOM_HUD = handle(LOOKUP, HudManager.class, "customHud", CustomUIHud.class);
        public static final BiConsumer<HudManager, CustomUIHud> CUSTOM_HUD = _CUSTOM_HUD::set;
    }

    private static VarHandle handle(MethodHandles.Lookup lookup, Class<?> clazz, String name, Class<?> type) {
        try {
            return lookup.findVarHandle(clazz, name, type);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            EyeSpy.LOGGER.atSevere().withCause(e).log("Unable to access field '%s' for class '%s'!", name, clazz.getCanonicalName());
            throw new RuntimeException(e);
        }
    }

    private static VarHandle handleStatic(MethodHandles.Lookup lookup, Class<?> clazz, String name, Class<?> type) {
        try {
            return lookup.findStaticVarHandle(clazz, name, type);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            EyeSpy.LOGGER.atSevere().withCause(e).log("Unable to access static field '%s' for class '%s'!", name, clazz.getCanonicalName());
            throw new RuntimeException(e);
        }
    }

    private static MethodHandles.Lookup lookup(Class<?> clazz) {
        try {
            return MethodHandles.privateLookupIn(clazz, MethodHandles.lookup());
        }
        catch (IllegalAccessException e) {
            EyeSpy.LOGGER.atSevere().withCause(e).log("Unable to create lookup for class '%s'!", clazz.getCanonicalName());
            throw new RuntimeException(e);
        }
    }
}
