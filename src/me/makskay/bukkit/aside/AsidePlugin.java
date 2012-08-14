package me.makskay.bukkit.aside;

import me.makskay.bukkit.aside.listener.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AsidePlugin extends JavaPlugin {
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
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
}
