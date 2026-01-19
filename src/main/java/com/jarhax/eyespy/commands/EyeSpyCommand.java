package com.jarhax.eyespy.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jarhax.eyespy.EyeSpy;

public class EyeSpyCommand extends AbstractPlayerCommand {
    
    private final EyeSpy plugin;
    
    public EyeSpyCommand(EyeSpy plugin) {
        super("eyespy", "Toggle EyeSpy HUD", false);
        this.plugin = plugin;
        requirePermission("eyespy.use");
        addAliases("es");
    }
    
    @Override
    public void execute(CommandContext ctx, Store<EntityStore> store, Ref<EntityStore> ref, PlayerRef playerRef, World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        
        if (player == null) {
            ctx.sendMessage(Message.raw("Error: Player not found!").color("#FF0000"));
            return;
        }
        
        boolean newState = plugin.getUserDataManager().toggleEnabled(playerRef.getUuid(), playerRef.getUsername());
        
        Message prefix = Message.raw("[EyeSpy] ").color("#55FFFF");
        
        if (newState) {
            Message content = Message.raw("HUD has been enabled!").color("#00FF00");
            ctx.sendMessage(Message.join(prefix, content));
        } else {
            Message content = Message.raw("HUD has been disabled!").color("#FF5555");
            ctx.sendMessage(Message.join(prefix, content));
        }
    }
}
