package net.excentrix.core.events;

import net.excentrix.core.Core;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

public class mobSpawn implements Listener {
    private static Plugin plugin = Core.getPlugin(Core.class);

    @EventHandler
    public void disableAI(CreatureSpawnEvent event) {
        if (plugin.getConfig().getBoolean("mobAI")) {
            event.getEntity().setAI(true);
        } else {
            event.getEntity().setAI(false);
        }
    }

    @EventHandler
    public void whitelistZombies(CreatureSpawnEvent event) {
        if (plugin.getConfig().getBoolean("zombies-whitelisted")) {
            if (!(event.getEntity() instanceof Zombie)) {
                event.getEntity().remove();
            }
        }
    }

    @EventHandler
    public void disableDrowned(CreatureSpawnEvent event) {
        if (plugin.getConfig().getBoolean("disableDrowned")) {
            if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.DROWNED) {
                event.getEntity().remove();
            }
        }
    }

    @EventHandler
    public void disableWanderingTrader(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof WanderingTrader) {
            event.setCancelled(true);
        }
    }
}
