package net.excentrix.core.Commands;

import net.excentrix.core.Core;
import net.excentrix.core.utils.staffUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class grants implements CommandExecutor {
    private static Plugin plugin = Core.getPlugin(Core.class);


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player;
        Player target;
        player = (Player) commandSender;
        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            LuckPerms api = LuckPermsProvider.get();
            if (!(commandSender.hasPermission("clarke.command.grants"))) {
                staffUtils.noPerm((Player) commandSender);
                return true;
            } else if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
                if (strings.length != 1) {
                    staffUtils.printUsage(player, "grants", "<player>");
                } else {
                    target = Bukkit.getPlayerExact(strings[0]);
                    try {
                        String group = staffUtils.getRank(target.getName());
                        if (target != null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&l[&a&l✩&8&l] &e" + target.getName() + "&7 has the grant: " + api.getGroupManager().getGroup(group).getDisplayName() + "&7."));
                        }
                    } catch (NullPointerException e) {
                        staffUtils.playerNotFound(player);
                    }
                }
            }
        } else
            commandSender.sendMessage(ChatColor.RED + "Cannot perform any actions using grant, as the dependency is not found!");
        return true;
    }
}
