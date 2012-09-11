package me.makskay.bukkit.aside;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class PlayerManager {
	//private AsidePlugin plugin;
	private HashMap<String, ArrayList<String>> savedMessages;
	
	public PlayerManager(AsidePlugin plugin) {
		//this.plugin = plugin;
		savedMessages = new HashMap<String, ArrayList<String>>();
	}
	
	public void registerPlayer(Player player) {
		savedMessages.put(player.getName(), new ArrayList<String>());
	}
	
	public void releasePlayer(Player player) {
		savedMessages.remove(player.getName());
	}
	
	public void saveMessage(Player player, String message) {
		savedMessages.get(player.getName()).add(message);
	}
	
	public ArrayList<String> getSavedMessages(Player player) {
		return savedMessages.get(player.getName());
	}
	
	public void clearSavedMessages(Player player) {
		savedMessages.get(player.getName()).clear();
	}
}
