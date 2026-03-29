package com.jarhax.eyespy.api.hud;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.EyeSpy;
import com.jarhax.eyespy.impl.hud.EyeSpyHud;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

public class MultiHudProvider implements HudProvider {

    private final Map<PlayerRef, EyeSpyHud> huds = new HashMap<>();
    private final Object mhudInstance;
    private final MethodHandle setCustomHud;

    public MultiHudProvider(Object mhudInstance, MethodHandle setCustomHud) {
        this.mhudInstance = mhudInstance;
        this.setCustomHud = setCustomHud;
    }

    public static MultiHudProvider create(EyeSpy eyespy) {
        try {
            String className = "com.buuz135.mhud.MultipleHUD";
            // This is bad, but I don't care, Class.forName does not work, I am tired of fighting the class loader shenanigans that hytale has going on.
            Class<?> clazz = eyespy.getClassLoader().loadClass(className);
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandle getInstance = lookup.findStatic(clazz, "getInstance", MethodType.methodType(clazz));
            Object mhudInstance = getInstance.invoke();
            MethodHandle setCustomHud = lookup.findVirtual(clazz, "setCustomHud", MethodType.methodType(void.class, Player.class, PlayerRef.class, String.class, CustomUIHud.class));
            return new MultiHudProvider(mhudInstance, setCustomHud);
        } catch (Throwable t) {
            EyeSpy.LOGGER.atSevere().log("Error while creating MultipleHudProvider!", t);
            return null;
        }
    }

    @Override
    public void showHud(float dt, int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        final Holder<EntityStore> holder = EntityUtils.toHolder(index, archetypeChunk);
        final Player player = holder.getComponent(Player.getComponentType());
        final PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
        if (player == null || playerRef == null) {
            return;
        }
        if (!huds.containsKey(playerRef)) {
            EyeSpyHud value = new EyeSpyHud(playerRef);
            huds.put(playerRef, value);
            value.updateHud(dt, index, archetypeChunk, store, commandBuffer);
            setCustomHud(player, playerRef, "EyeSpy_HUD", value);
        } else {
            EyeSpyHud customUIHud = huds.get(playerRef);
            customUIHud.updateHud(dt, index, archetypeChunk, store, commandBuffer);
            setCustomHud(player, playerRef, "EyeSpy_HUD", customUIHud);
        }
    }

    private void setCustomHud(Player player, PlayerRef playerRef, String hudIdentifier, CustomUIHud customHud) {
        try {
            setCustomHud.invoke(mhudInstance, player, playerRef, hudIdentifier, customHud);
        } catch (Throwable t) {
            EyeSpy.LOGGER.atSevere().log("Unable to set custom MultipleHUD!", t);
        }
    }
}
