package com.jarhax.eyespy.impl.component;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentAccessor;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.api.EyeSpyConfig;
import com.jarhax.eyespy.api.info.AnchorBuilder;
import com.jarhax.eyespy.impl.hud.LayoutMode;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class EyeSpyPlayerData implements Component<EntityStore>, EyeSpyConfig {

    public static final BuilderCodec<EyeSpyPlayerData> CODEC = BuilderCodec.builder(EyeSpyPlayerData.class, EyeSpyPlayerData::new)
            .append(new KeyedCodec<>("Position", AnchorBuilder.CODEC), EyeSpyPlayerData::position, EyeSpyPlayerData::position)
            .documentation("The position of the hud on the screen")
            .add()
            .append(new KeyedCodec<>("LayoutMode", LayoutMode.CODEC), EyeSpyPlayerData::layoutMode, EyeSpyPlayerData::layoutMode)
            .documentation("The layout of the hud")
            .add()
            .append(new KeyedCodec<>("Visible", Codec.BOOLEAN), EyeSpyPlayerData::visible, EyeSpyPlayerData::visible)
            .documentation("Is the hud visible")
            .add()
            .append(new KeyedCodec<>("ShowInBackground", Codec.BOOLEAN), EyeSpyPlayerData::showInBackground, EyeSpyPlayerData::showInBackground)
            .documentation("Should the hud be shown if the player has a page / container open")
            .add()
            .append(new KeyedCodec<>("ShowContainers", Codec.BOOLEAN), EyeSpyPlayerData::showContainers, EyeSpyPlayerData::showContainers)
            .documentation("Should container contents be shown")
            .add()
            .append(new KeyedCodec<>("ShowProcessingTimes", Codec.BOOLEAN), EyeSpyPlayerData::showProcessingTimes, EyeSpyPlayerData::showProcessingTimes)
            .documentation("Should processing times be shown")
            .add()
            .build();


    private static ComponentType<EntityStore, EyeSpyPlayerData> saveDataComponentType;

    private AnchorBuilder position = new AnchorBuilder().setLeft(20).setTop(20);
    private LayoutMode layoutMode = LayoutMode.LEFT;
    private boolean visible = true;
    private boolean showInBackground = true;
    private boolean showContainers = true;
    private boolean showProcessingTimes = true;

    public AnchorBuilder position() {
        return position;
    }

    public void position(AnchorBuilder position) {
        this.position = position;
    }

    public LayoutMode layoutMode() {
        return layoutMode;
    }

    public void layoutMode(LayoutMode layout) {
        this.layoutMode = layout;
    }

    public boolean visible() {
        return visible;
    }

    public void visible(boolean visible) {
        this.visible = visible;
    }

    public boolean showContainers() {
        return showContainers;
    }

    public void showContainers(boolean showContainers) {
        this.showContainers = showContainers;
    }

    public boolean showProcessingTimes() {
        return showProcessingTimes;
    }

    public void showProcessingTimes(boolean showProcessingTimes) {
        this.showProcessingTimes = showProcessingTimes;
    }

    public boolean showInBackground() {
        return showInBackground;
    }

    public void showInBackground(boolean showInBackground) {
        this.showInBackground = showInBackground;
    }

    @NullableDecl
    @Override
    public EyeSpyPlayerData clone() {
        EyeSpyPlayerData comp = new EyeSpyPlayerData();
        comp.position(this.position);
        comp.layoutMode(this.layoutMode);
        comp.visible(this.visible);
        comp.showInBackground(this.showInBackground);
        comp.showContainers(this.showContainers);
        comp.showProcessingTimes(this.showProcessingTimes);
        return comp;
    }

    public static void init(PluginBase plugin) {
        if (saveDataComponentType != null) {
            throw new IllegalStateException("EyeSpy player save data component has already been initialized!");
        }
        saveDataComponentType = plugin.getEntityStoreRegistry().registerComponent(EyeSpyPlayerData.class, "EyeSpy", EyeSpyPlayerData.CODEC);
    }

    public static EyeSpyPlayerData getSaveData(Holder<EntityStore> holder) {
        return holder.ensureAndGetComponent(saveDataComponentType);
    }

    public static EyeSpyPlayerData getSaveData(ComponentAccessor<EntityStore> buffer, Ref<EntityStore> ref) {
        return buffer.ensureAndGetComponent(ref, saveDataComponentType);
    }
}
