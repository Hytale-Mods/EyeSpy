package com.jarhax.eyespy.impl.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.DropdownEntryInfo;
import com.hypixel.hytale.server.core.ui.LocalizableString;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.api.info.AnchorBuilder;
import com.jarhax.eyespy.impl.component.EyeSpyPlayerData;
import com.jarhax.eyespy.impl.hud.LayoutMode;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ConfigUI extends InteractiveCustomUIPage<ConfigUI.Data> {

    public ConfigUI(@NonNullDecl PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, Data.CODEC);
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
        uiCommandBuilder.append("EyeSpy/Pages/Config.ui");
        EyeSpyPlayerData eyeSpyComponent = EyeSpyPlayerData.getSaveData(store, ref);

        uiCommandBuilder.set("#Visible #CheckBox.Value", eyeSpyComponent.visible());
        uiCommandBuilder.set("#ShowContainers #CheckBox.Value", eyeSpyComponent.showContainers());
        uiCommandBuilder.set("#ShowProcessingTimes #CheckBox.Value", eyeSpyComponent.showProcessingTimes());
        uiCommandBuilder.set("#ShowInBackground #CheckBox.Value", eyeSpyComponent.showInBackground());

        ObjectArrayList<DropdownEntryInfo> positions = new ObjectArrayList<>();
        AnchorBuilder position = eyeSpyComponent.position();
        AnchorBuilder anchorBuilder = new AnchorBuilder();
        for (HudPosition value : HudPosition.values()) {
            positions.add(new DropdownEntryInfo(LocalizableString.fromMessageId("server.eyespy.config.position." + value.name), value.name));
            anchorBuilder.clear();
            value.getBuilder().accept(anchorBuilder);
            if (position.equals(anchorBuilder)) {
                uiCommandBuilder.set("#Position #Dropdown.Value", value.name);
            }
        }

        uiCommandBuilder.set("#Position #Dropdown.Entries", positions);

        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#SaveButton",
                new EventData()
                        .append("@Visible", "#Visible #CheckBox.Value")
                        .append("@ShowContainers", "#ShowContainers #CheckBox.Value")
                        .append("@ShowProcessingTimes", "#ShowProcessingTimes #CheckBox.Value")
                        .append("@ShowInBackground", "#ShowInBackground #CheckBox.Value")
                        .append("@Position", "#Position #Dropdown.Value")
        );
    }

    @Override
    public void handleDataEvent(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Store<EntityStore> store, @NonNullDecl Data data) {
        super.handleDataEvent(ref, store, data);
        System.out.println(data);

        EyeSpyPlayerData eyeSpyComponent = EyeSpyPlayerData.getSaveData(store, ref);
        eyeSpyComponent.visible(data.visible);
        eyeSpyComponent.showContainers(data.showContainers);
        eyeSpyComponent.showProcessingTimes(data.showProcessingTimes);
        eyeSpyComponent.showInBackground(data.showInBackground);
        String hudPosition = data.position;
        if (HudPosition.from(hudPosition) instanceof HudPosition position) {
            AnchorBuilder anchorBuilder = new AnchorBuilder();
            position.getBuilder().accept(anchorBuilder);
            eyeSpyComponent.position(anchorBuilder);
            eyeSpyComponent.layoutMode(position.getMode());
        }

        UICommandBuilder commandBuilder = new UICommandBuilder();
        UIEventBuilder uiEventBuilder = new UIEventBuilder();
        this.sendUpdate(commandBuilder, uiEventBuilder, false);
    }

    public static class Data {
        public static final BuilderCodec<Data> CODEC = BuilderCodec.builder(Data.class, Data::new)
                .append(new KeyedCodec<>("@Visible", Codec.BOOLEAN), (searchGuiData, s) -> searchGuiData.visible = s, (searchGuiData) -> searchGuiData.visible)
                .add()
                .append(new KeyedCodec<>("@ShowContainers", Codec.BOOLEAN), (searchGuiData, s) -> searchGuiData.showContainers = s, (searchGuiData) -> searchGuiData.showContainers)
                .add()
                .append(new KeyedCodec<>("@ShowProcessingTimes", Codec.BOOLEAN), (searchGuiData, s) -> searchGuiData.showProcessingTimes = s, (searchGuiData) -> searchGuiData.showProcessingTimes)
                .add()
                .append(new KeyedCodec<>("@ShowInBackground", Codec.BOOLEAN), (searchGuiData, s) -> searchGuiData.showInBackground = s, (searchGuiData) -> searchGuiData.showInBackground)
                .add()
                .append(new KeyedCodec<>("@Position", Codec.STRING), (searchGuiData, s) -> searchGuiData.position = s, (searchGuiData) -> searchGuiData.position)
                .add()
                .build();

        private boolean visible;
        private boolean showContainers;
        private boolean showProcessingTimes;
        private boolean showInBackground;
        private String position;


        @Override
        public String toString() {
            return "Data{" + "visible=" + visible +
                   ", showContainers=" + showContainers +
                   ", showProcessingTimes=" + showProcessingTimes +
                   ", showInBackground=" + showInBackground +
                   ", position='" + position + '\'' +
                   '}';
        }
    }

    private enum HudPosition {
        TOP_LEFT("top_left", (builder) -> builder.setLeft(20).setTop(20), LayoutMode.LEFT),
        TOP_CENTER("top_center", (builder) -> builder.setHorizontal(0).setTop(50), LayoutMode.CENTER),
        TOP_RIGHT("top_right", (builder) -> builder.setRight(20).setTop(20), LayoutMode.RIGHT),
        BOTTOM_LEFT("bottom_left", (builder) -> builder.setLeft(20).setBottom(20), LayoutMode.LEFT),
        BOTTOM_CENTER("bottom_center", (builder) -> builder.setHorizontal(20).setBottom(150), LayoutMode.CENTER),
        BOTTOM_RIGHT("bottom_right", (builder) -> builder.setRight(20).setBottom(150), LayoutMode.RIGHT),
        ;

        private final String name;
        private final Consumer<AnchorBuilder> builder;
        private final LayoutMode mode;

        HudPosition(String name, Consumer<AnchorBuilder> builder, LayoutMode mode) {
            this.name = name;
            this.builder = builder;
            this.mode = mode;
        }

        public static HudPosition from(String name) {
            for (HudPosition value : HudPosition.values()) {
                if (value.name.equals(name)) {
                    return value;
                }
            }
            return null;
        }


        public String getName() {
            return name;
        }

        public Consumer<AnchorBuilder> getBuilder() {
            return builder;
        }

        public LayoutMode getMode() {
            return mode;
        }

        @Override
        public String toString() {
            return "HudPosition{" + "name='" + name + '\'' +
                   ", builder=" + builder +
                   ", mode=" + mode +
                   '}';
        }
    }
}
