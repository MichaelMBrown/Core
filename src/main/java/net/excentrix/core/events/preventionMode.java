package net.excentrix.core.events;

import net.excentrix.core.Central;
import net.excentrix.core.utils.coreUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class preventionMode implements Listener {
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void preventBuild(BlockBreakEvent event) {
		if (Central.buildDenied.contains(event.getPlayer())) {
			event.setCancelled(true);
			event.setDropItems(false);
			if (event.getPlayer().hasPermission("atom.command.build"))
				coreUtils.errorMessage(event.getPlayer(), "You cannot break blocks as your build mode is disabled!");
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void preventPlace(BlockPlaceEvent event) {
		if (Central.buildDenied.contains(event.getPlayer())) {
			event.setCancelled(true);
			if (event.getPlayer().hasPermission("atom.command.build"))
				coreUtils.errorMessage(event.getPlayer(), "You cannot place blocks as your build mode is disabled!");
		}
	}
	
	@EventHandler
	public void onWorld(PlayerChangedWorldEvent event) {
		switch (event.getPlayer().getWorld().getName().toLowerCase()) {
			case "skyblock":
				Central.buildDenied.remove(event.getPlayer());
				break;
			case "hub":
				Central.buildDenied.add(event.getPlayer());
				break;
		}
	}
}

