package me.makskay.bukkit.aside;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.entity.Player;

public class PlayerManager {
	//private AsidePlugin plugin;
	private HashMap<String, ArrayList<String>> savedMessages;
	private HashMap<String, HashSet<String>> replyTags;
	private HashSet<String> afkPlayers;
	
	public PlayerManager(AsidePlugin plugin) {
		//this.plugin = plugin;
		savedMessages = new HashMap<String, ArrayList<String>>();
		afkPlayers = new HashSet<String>();
	}
	
	public void registerPlayer(Player player) {
		savedMessages.put(player.getName(), new ArrayList<String>());
		replyTags.put(player.getName(), new HashSet<String>());
	}
	
	public void releasePlayer(Player player) {
		String playername = player.getName();
		savedMessages.remove(playername);
		replyTags.remove(playername);
		this.releaseAfkPlayer(playername);
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
	
	public void registerAfkPlayer(Player player) {
		afkPlayers.add(player.getName());
	}
	
	public void releaseAfkPlayer(Player player) {
		afkPlayers.remove(player.getName());
	}
 	
	public void releaseAfkPlayer(String playername) {
		afkPlayers.remove(playername);
	}
	
	public boolean playerIsAfk(Player player) {
		return afkPlayers.contains(player.getName());
	}
}
