package me.makskay.bukkit.aside;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import me.makskay.bukkit.aside.listener.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AsidePlugin extends JavaPlugin {
	private GroupManager groupManager;
	private Logger log;
	
	public void onEnable() {
		log = this.getLogger();
		
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		
		log.info("Loading saved groups from groups.yml...");
		
	    File groupsFile = new File(getDataFolder(), "groups.yml");
	    FileConfiguration groupsConfig = YamlConfiguration.loadConfiguration(groupsFile);
	    
	    HashMap<String, ChatGroup> groups = new HashMap<String, ChatGroup>();
		
		for (String groupName : groupsConfig.getConfigurationSection("groups").getKeys(false)) {
			groups.put(groupName, new ChatGroup(groupsConfig.getString("groups." + groupName + ".owner"),
					groupsConfig.getStringList("groups." + groupName + ".members")));
		} 
		
		groupManager = new GroupManager(this, groups);
		
		log.info("Saved groups loaded.");
	}
	
	public void onDisable() {
		// TODO Gracefully save the loaded groups to groups.yml on disk
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equalsIgnoreCase("group")) {
			if (!(args.length > 0)) {
				sender.sendMessage("/group <add|remove|make|delete> <group-name> [command-specific arguments]");
				return true;
			}
			
			// TODO Write actual implementations for /group subcommands
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
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getName().toLowerCase().contains(s.toLowerCase())) {
				return p;
			}
		}
		
		return null;
	}
	
	public GroupManager getGroupManager() {
		return groupManager;
	}
}
