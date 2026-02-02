package com.jarhax.eyespy.impl.hud;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.api.EyeSpyConfig;
import com.jarhax.eyespy.api.context.BlockContext;
import com.jarhax.eyespy.api.context.EntityContext;
import com.jarhax.eyespy.api.info.AnchorBuilder;
import com.jarhax.eyespy.api.info.InfoBuilder;
import com.jarhax.eyespy.api.info.InfoProvider;
import com.jarhax.eyespy.api.info.InfoValue;
import com.jarhax.eyespy.impl.component.EyeSpyPlayerData;
import com.jarhax.eyespy.impl.info.VanillaBlockInfoProvider;
import com.jarhax.eyespy.impl.info.VanillaEntityInfoProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class EyeSpyHud extends CustomUIHud {

    // TODO Allow plugins to register new providers;
    private static final List<InfoProvider<BlockContext>> blockInfoProviders = new LinkedList<>();
    private static final List<InfoProvider<EntityContext>> entityInfoProviders = new LinkedList<>();


    static {
        blockInfoProviders.add(new VanillaBlockInfoProvider());
        entityInfoProviders.add(new VanillaEntityInfoProvider());
    }

    @Nullable
    private InfoBuilder info;

    @Nullable
    private BlockContext blockContext;

    @Nullable
    private EntityContext entityContext;

    @Nullable
    private EyeSpyConfig config;

    public EyeSpyHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    public void updateHud(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        this.info = new InfoBuilder();
        PlayerRef playerRef = archetypeChunk.getComponent(index, PlayerRef.getComponentType());
        World world = store.getExternalData().getWorld();
        Ref<EntityStore> referenceTo = archetypeChunk.getReferenceTo(index);
        EyeSpyConfig config = EyeSpyPlayerData.getSaveData(commandBuffer, referenceTo);
        if (playerRef != null && world != null) {
            this.config = config;
            this.entityContext = EntityContext.create(playerRef, dt, index, archetypeChunk, store, commandBuffer, config);
            if (this.entityContext != null) {
                for (InfoProvider<EntityContext> provider : entityInfoProviders) {
                    provider.updateDescription(this.entityContext, this.info);
                }
            }
            else {
                this.blockContext = BlockContext.create(playerRef, dt, index, archetypeChunk, store, commandBuffer, config);
                if (this.blockContext != null) {
                    for (InfoProvider<BlockContext> provider : blockInfoProviders) {
                        provider.updateDescription(this.blockContext, this.info);
                    }
                }
            }
        }

    }

    @Override
    protected void build(@Nonnull UICommandBuilder ui) {
        if (this.info != null && this.info.canDisplay() && this.config != null) {
            ui.append("EyeSpy/Hud/EyeSpy.ui");
            AnchorBuilder anchor = this.config.position().clone();
            this.info.values()
                    .filter(infoValue -> infoValue != InfoValue.EMPTY)
                    .forEach(infoValue -> {
                        infoValue.build(ui, anchor, "#Info");
                    });
            this.info.getIcon().build(ui, anchor, "#IconContainer");
            ui.setObject("#EyeSpyHud.LayoutMode", this.config.layoutMode());
            ui.setObject("#EyeSpyHud.Anchor", anchor.build());
        }
        if (this.entityContext != null) {
            for (InfoProvider<EntityContext> provider : entityInfoProviders) {
                provider.modifyUI(this.entityContext, ui);
            }
        }
        else if (this.blockContext != null) {
            for (InfoProvider<BlockContext> provider : blockInfoProviders) {
                provider.modifyUI(this.blockContext, ui);
            }
        }
    }
}
