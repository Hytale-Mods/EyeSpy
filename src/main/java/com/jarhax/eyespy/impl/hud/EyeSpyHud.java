package com.jarhax.eyespy.impl.hud;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.FormattedMessage;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.api.info.InfoProvider;
import com.jarhax.eyespy.api.context.BlockContext;
import com.jarhax.eyespy.api.info.InfoBuilder;
import com.jarhax.eyespy.impl.info.VanillaBlockInfoProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class EyeSpyHud extends CustomUIHud {

    // TODO Allow plugins to register new providers;
    private static final List<InfoProvider<BlockContext>> blockInfoProviders = new LinkedList<>();

    static {
        blockInfoProviders.add(new VanillaBlockInfoProvider());
    }

    @Nullable
    private InfoBuilder info;

    @Nullable
    private BlockContext blockContext;

    public EyeSpyHud(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    public void updateHud(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        this.info = new InfoBuilder();
        final Holder<EntityStore> holder = EntityUtils.toHolder(index, archetypeChunk);
        final Player player = holder.getComponent(Player.getComponentType());
        if (player != null && player.getWorld() != null) {
            this.blockContext = BlockContext.create(player, dt, index, archetypeChunk, store, commandBuffer);
            if (this.blockContext != null) {
                for (InfoProvider<BlockContext> provider : blockInfoProviders) {
                    provider.updateDescription(this.blockContext, this.info);
                }
            }
        }
    }

    @Override
    protected void build(@Nonnull UICommandBuilder ui) {
        if (this.info != null) {
            ui.append("Hud/EyeSpy.ui");
            setText(ui, "#Header", this.info.getHeader());
            setText(ui, "#Body", this.info.getBody());
            setText(ui, "#Footer", this.info.getFooter());
            setIcon(ui, this.info.getIcon());
        }
        if (this.blockContext != null) {
            for (InfoProvider<BlockContext> provider : blockInfoProviders) {
                provider.modifyUI(this.blockContext, ui);
            }
        }
    }

    @Override
    public void update(boolean clear, @Nonnull UICommandBuilder commandBuilder) {
        super.update(clear, commandBuilder);
    }

    private static boolean isValid(Message message) {
        if (message != null) {
            final FormattedMessage formattedMessage = message.getFormattedMessage();
            return formattedMessage != null;
        }
        return false;
    }

    private static void setText(@Nonnull UICommandBuilder ui, @Nonnull String selector, @Nullable Message value) {
        if (!isValid(value)) {
            ui.set(selector + ".Visible", false);
        }
        else {
            ui.set(selector + ".Visible", true);
            ui.set(selector + ".TextSpans", value);
        }
    }

    private static void setIcon(@Nonnull UICommandBuilder ui, @Nullable String iconId) {
        if (iconId == null) {
            ui.set("#Icon.Visible", false);
        }
        else {
            ui.set("#Icon.Visible", true);
            ui.set("#Icon.ItemId", iconId);
        }
    }
}
