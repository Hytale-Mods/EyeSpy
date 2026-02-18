package com.jarhax.eyespy;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.jarhax.eyespy.impl.command.ConfigCommand;
import com.jarhax.eyespy.impl.component.EyeSpyPlayerData;
import com.jarhax.eyespy.impl.hud.PlayerTickSystem;
import com.jarhax.eyespy.impl.util.Owners;

import javax.annotation.Nonnull;

public class EyeSpy extends JavaPlugin {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
    // Das Feld 'provider' muss vermutlich irgendwo definiert sein, 
    // oder es war statisch in HudProvider gedacht (siehe unten).
    // Wenn HudProvider.PROVIDER ein CachedSupplier ist, können wir ihn hier nicht einfach überschreiben.
    // Aber basierend auf deinem vorherigen Code in HudProvider.java wird das dort intern gelöst.
    // Falls du es hier setzen willst, müsste HudProvider.PROVIDER wohl verändert werden oder du nutzt Reflection/einen Setter.
    // Da HudProvider.PROVIDER ein `CachedSupplier` ist, wird die Logik "welches HUD nutzen wir" eigentlich DORT entschieden.
    
    // Ich nehme an, du wolltest hier die Logik aus HudProvider.java manuell steuern oder vorbereiten.
    // Da HudProvider.PROVIDER cached ist, brauchen wir hier eigentlich keinen Code in start(), 
    // außer wir wollen sicherstellen, dass die Klassen geladen sind.
    
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
        // Die Logik für MultiHUD ist bereits in HudProvider.PROVIDER eingebaut (siehe src/main/java/com/jarhax/eyespy/impl/hud/provider/HudProvider.java).
        // Der Code hier ist redundant oder führt zu Fehlern, wenn das Feld 'provider' nicht in EyeSpy existiert.
        // Ich kommentiere es aus, da die Logik im CachedSupplier von HudProvider liegt.
        /*
        PluginBase plugin = PluginManager.get().getPlugin(PluginIdentifier.fromString("Buuz135:MultipleHUD"));
        if (plugin != null) {
             // EyeSpy.provider = new MultiHudProvider(); // Dieses Feld existiert hier nicht statisch
        }
        */
        Owners.reload();
    }
}
