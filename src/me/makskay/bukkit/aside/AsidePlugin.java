package me.makskay.bukkit.aside;

import java.util.ArrayList;

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
		
		if (configYml.getConfig().getBoolean("auto-update")) {
			//@SuppressWarnings("unused")
			//Updater updater = new Updater(this, "aside", this.getFile(), Updater.UpdateType.DEFAULT, false);
		}
		
		groupManager = new GroupManager(this);
		playerManager = new PlayerManager(this);
	}
	
	public void onDisable() {
		for (ChatGroup group : groupManager.getLoadedGroups()) {
			String path = "groups." + group.getName() + ".";
			
			groupsYml.getConfig().set(path + "owner", group.getOwner());
			groupsYml.getConfig().set(path + "members", group.getMembers());
		}
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equalsIgnoreCase("group")) {
			if (args.length < 2) {
				return false;
			}
			
			if ((args[0].equalsIgnoreCase("create")) || (args[0].equalsIgnoreCase("c"))) {
				ArrayList<String> members = new ArrayList<String>();
				
				if (sender instanceof Player) {
					members.add(sender.getName());
				}
				
				sender.sendMessage(ChatColor.GREEN + "Created group \"" + args[1] + "\"");
				
				for (int i = 2; i < args.length; i++) {
					members.add(args[i]);
					sender.sendMessage(ChatColor.GREEN + "Added \"" + args[i] + "\" to group \"" + args[1] + "\"");
				}

				ChatGroup group = new ChatGroup(args[1], sender.getName(), members);
				groupManager.registerGroup(group);
				
				return true;
			}
			
			ChatGroup group = groupManager.getGroupByName(args[1]);
			if (group == null) {
				sender.sendMessage(ChatColor.RED + "There's no group named \"" + args[1] + "\"!");
				return true;
			}
			
			boolean senderCanPerformOp = false;
			
			Player player = (Player) sender;
			if (player == null) {
				senderCanPerformOp = true;
			}
			
			else {
				if (player.hasPermission("aside.group.admin")) {
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
			
			if ((args[0].equalsIgnoreCase("delete")) || (args[0].equalsIgnoreCase("d"))) {
				groupManager.deleteGroup(args[1]);

				groupsYml.getConfig().set("groups." + args[1], null);
				groupsYml.saveConfig();
				groupsYml.reloadConfig();
				
				sender.sendMessage(ChatColor.GREEN + "Successfully deleted group \"" + args[1] + "\"");
				return true;
			}
			
			else if ((args[0].equalsIgnoreCase("add")) || (args[0].equalsIgnoreCase("a"))) {
				for (int i = 2; i < args.length; i++) {
					groupManager.addMemberToGroup(args[1], args[i]);
					sender.sendMessage(ChatColor.GREEN + "Added \"" + args[i] + "\" to group \"" + args[1] + "\"");
				}
				
				// TODO Make changes to the copy on file if "always-backup-changes" is set to true
			}
			
			else if ((args[0].equalsIgnoreCase("remove")) || (args[0].equalsIgnoreCase("r"))) {
				for (int i = 2; i < args.length; i++) {
					groupManager.removeMemberFromGroup(args[1], args[i]);
					sender.sendMessage(ChatColor.GREEN + "Removed \"" + args[i] + "\" from group \"" + args[1] + "\"");
				}
				
				// TODO Make changes to the copy on file if "always-backup-changes" is set to true
			}
			
		}
		
		else if (command.getName().equals("groups")) {
			String list = ChatColor.GREEN + "Groups: " + ChatColor.WHITE;
			
			for (String name : groupManager.getAllGroupNames()) {
				list.concat(name);
				list.concat(" ");
			}
			
			sender.sendMessage(list.trim());
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
