package net.excentrix.core.events;

import net.excentrix.core.Core;
import net.excentrix.core.utils.staffUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.query.QueryOptions;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class authEvent implements Listener {
	
	private static final Plugin plugin = Core.getPlugin(Core.class);
	LuckPerms api = LuckPermsProvider.get();
	
	@EventHandler
	public void authOnJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
			LuckPerms api = LuckPermsProvider.get();
			String group = api.getUserManager().getUser(player.getName()).getPrimaryGroup();
			String grant = api.getGroupManager().getGroup(group).getDisplayName();
			if (plugin.getConfig().getString("server-name").equalsIgnoreCase("hub"))
				staffUtils.informativeMessage(player, "Please hold while I verify your grants...");
			Core.freezeList.add(player);
			if (plugin.getConfig().getString("server-name").equalsIgnoreCase("hub"))
				if (grant == null) {
					staffUtils.errorMessage(player, "WARN: Something went wrong in identifying your grants, playing it safe and locking you.");
					staffUtils.scNotify("console", "&c&lWARN: &7" + player.getName() + "&c&l has an invalid grant setup, please notify the System Administrator immediately.");
					Core.freezeList.add(player);
				} else
					staffUtils.informativeMessage(player, "&aVerified&7 Applying your " + grant + " &7grant to you now.");
			Core.freezeList.remove(player);
		} else {
			staffUtils.errorMessage(player, "WARN: Something went wrong in identifying your grants, playing it safe and locking you.");
			staffUtils.scNotify("console", "&c&lWARN: &7" + player.getName() + "&c&l has an invalid grant setup, please notify the System Administrator immediately.");
			Core.freezeList.add(player);
		}
		QueryOptions queryOptions = api.getContextManager().getQueryOptions(player);
		CachedMetaData metaData = api.getUserManager().getUser(player.getName()).getCachedData().getMetaData(queryOptions);
		if (metaData.getMetaValue("prison_rank") == null || metaData.getMetaValue("prison_rank").equals("A")) {
			try {
				if (metaData.getMetaValue("prison_rank").equals("A")) {
					User user = api.getUserManager().getUser(player.getUniqueId());
					user.data().remove(MetaNode.builder("prison_rank", "A").build());
				}
			} catch (NullArgumentException | NullPointerException ignored) {
			}
			User user = api.getUserManager().getUser(player.getUniqueId());
			user.data().add(MetaNode.builder("prison_rank", "A1").build());
			api.getUserManager().saveUser(user);
			
		}
	}
	
	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (staffUtils.getRankInteger(event.getPlayer().getName()) >= 1) {
			event.setJoinMessage("");
		} else event.setJoinMessage(ChatColor.DARK_GRAY + "Join> " + ChatColor.GRAY + event.getPlayer().getName());
	}
	
	@EventHandler
	public void buildMode(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		switch (plugin.getConfig().getString("server-name").toLowerCase()) {
			case "skyblock":
			case "creative":
			case "prison":
				Core.buildDenied.remove(player);
				break;
			default:
				Core.buildDenied.add(player);
		}
	}
	
	@EventHandler
	public void leaveEvent(PlayerQuitEvent event) {
		if (staffUtils.getRankInteger(event.getPlayer().getName()) >= 1) {
			event.setQuitMessage("");
		} else event.setQuitMessage(ChatColor.DARK_GRAY + "Quit> " + ChatColor.GRAY + event.getPlayer().getName());
	}
}