package com.jarhax.eyespy.impl.command;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.impl.ui.ConfigUI;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * The EyeSpy command opens a UI that allows players to configure the EyeSpy mod. Configuration options are unique to
 * each player, and are not shared.
 * <p>
 * To access the UI run /eyespy or /es in game.
 */
public class ConfigCommand extends AbstractAsyncCommand {

    @Nonnull
    private static final Message MESSAGE_COMMANDS_ERRORS_PLAYER_NOT_IN_WORLD = Message.translation("server.commands.errors.playerNotInWorld");

    public ConfigCommand() {
        super("eyespy", "server.eyespy.command");
        this.addAliases("es");
        this.setPermissionGroup(GameMode.Adventure);
    }

    @Nonnull
    @Override
    protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext context) {
        if (context.isPlayer()) {
            final Ref<EntityStore> playerRef = context.senderAsPlayerRef();
            if (playerRef != null && playerRef.isValid()) {
                final Store<EntityStore> store = playerRef.getStore();
                final World world = store.getExternalData().getWorld();
                return CompletableFuture.runAsync(() -> {
                    final Player sendingPlayer = store.getComponent(playerRef, Player.getComponentType());
                    final PlayerRef sendingPlayerRef = store.getComponent(playerRef, PlayerRef.getComponentType());
                    if (sendingPlayer != null && sendingPlayerRef != null) {
                        sendingPlayer.getPageManager().openCustomPage(playerRef, store, new ConfigUI(sendingPlayerRef));
                    }
                }, world);
            }
            else {
                context.sendMessage(MESSAGE_COMMANDS_ERRORS_PLAYER_NOT_IN_WORLD);
                return CompletableFuture.completedFuture(null);
            }
        }
        else {
            return CompletableFuture.completedFuture(null);
        }
    }
}
