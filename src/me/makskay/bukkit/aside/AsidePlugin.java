package me.makskay.bukkit.aside;

import me.makskay.bukkit.aside.util.ConfigAccessor;
//import me.makskay.bukkit.aside.util.Updater;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AsidePlugin extends JavaPlugin {
	private GroupManager groupManager;
	private PlayerManager playerManager;
	
	ConfigAccessor configYml;
	ConfigAccessor groupsYml;
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		
		configYml = new ConfigAccessor(this, "config.yml");
		groupsYml = new ConfigAccessor(this, "groups.yml");
		
		configYml.reloadConfig();
		groupsYml.reloadConfig();
		
		configYml.saveDefaultConfig();
		groupsYml.saveDefaultConfig();
		
		/*if (configYml.getConfig().getBoolean("auto-update")) { // auto-updater stuff
			@SuppressWarnings("unused")
			Updater updater = new Updater(this, "aside", this.getFile(), Updater.UpdateType.DEFAULT, false);
		}*/
		
		groupManager = new GroupManager(this);
		playerManager = new PlayerManager(this);
	}
	
	public void onDisable() {
		for (ChatGroup group : groupManager.getLoadedGroups()) {
			String path = "groups." + group.getName() + ".";
			
			groupsYml.getConfig().set(path + "owner", group.getOwner());
			groupsYml.getConfig().set(path + "members", group.getMembers());
		}
		
		groupsYml.saveConfig();
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equalsIgnoreCase("group")) {
			if (args.length == 0) { // if there were no arguments
				return false;
			}
			
			ChatGroup group = groupManager.getGroupByName(args[0]);
			if (group == null) {
				group = new ChatGroup(args[0], sender.getName());
				groupManager.registerGroup(group);
				
				sender.sendMessage(ChatColor.GREEN + "Created group \"" + args[0] + "\"");
			}
			
			if (args[1] == null) { // if the command was just "/group <group-name>"
				return true; 
			}
			
			else if (args[1].equalsIgnoreCase("members")) {
				String list = ChatColor.GREEN + "Members: " + ChatColor.WHITE;
				
				if (group.getMembers().isEmpty()) {
					list = list + ChatColor.RED + "none";
				}
				
				else {
					for (String name : group.getMembers()) {
						list = list + name + " ";
					}
					
					list = list.trim();
				}
				
				sender.sendMessage(list);
				return true;
			}
			
			boolean senderCanPerformOp = false;
			
			if (!(sender instanceof Player)) {
				senderCanPerformOp = true;
			}
			
			else {
				Player player  = (Player) sender;
				
				if (player.hasPermission("aside.admin")) {
					senderCanPerformOp = true;
				}
					
				else if (player.getName().equals(group.getOwner())) {
					senderCanPerformOp = true;
				}
			}
			
			if (!senderCanPerformOp) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to edit that group!");
				return true;
			}
			
			if (args[1].equalsIgnoreCase("delete")) {
				groupManager.deleteGroup(args[0]);

				groupsYml.getConfig().set("groups." + args[0], null);
				groupsYml.saveConfig();
				groupsYml.reloadConfig();
				
				sender.sendMessage(ChatColor.GREEN + "Successfully deleted group \"" + args[0] + "\"");
				return true;
			}
			
			else {
				for (int i = 1; i < args.length; i++) {
					String playername = args[i].substring(1).toLowerCase();
					
					if (args[i].startsWith("+")) {
						if (!group.getMembers().contains(playername)) {
							groupManager.addMemberToGroup(args[0], playername);
							sender.sendMessage(ChatColor.GREEN + "Added \"" + playername + "\" to group \"" + args[0] + "\"");
						}
						
						else {
							sender.sendMessage(ChatColor.RED + "\"" + playername + "\" is already a member of group \"" + args[0] + "\"!");
						}
					}
					
					else if (args[i].startsWith("-")) {
						if (group.getMembers().contains(playername)) {
							groupManager.removeMemberFromGroup(args[0], playername);
							sender.sendMessage(ChatColor.GREEN + "Removed \"" + playername + "\" from group \"" + args[0] + "\"");
						}
						
						else {
							sender.sendMessage(ChatColor.RED + "\"" + playername + "\" isn't a member of group \"" + args[0] + "\"!");
						}
					}
				}
				
				return true;
			}
		}
		
		else if (command.getName().equals("groups")) {
			String list = ChatColor.GREEN + "Groups: " + ChatColor.WHITE;
			
			if (groupManager.getAllGroupNames().isEmpty()) {
				list = list + ChatColor.RED + "none";
			}
			
			else {
				for (String name : groupManager.getAllGroupNames()) {
					list = list + name + " ";
				}
				
				list = list.trim();
			}
			
			sender.sendMessage(list);
			return true;
		}
		
		return false;
	}
	
	public Player getPlayerByNameSubstring(String s) { // utility method used to match partial usernames to actual player objects
		Player player = Bukkit.getPlayerExact(s);
		if (player != null) {
			return player;
		}
		
		if (s.length() < 3) {
			return null;
		}
		
		s = s.toLowerCase();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getName().toLowerCase().contains(s)) {
				return p;
			}
		}
		
		return null;
	}
	
	public GroupManager getGroupManager() {
		return groupManager;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}
}