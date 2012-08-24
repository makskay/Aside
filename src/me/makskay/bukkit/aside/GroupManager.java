package me.makskay.bukkit.aside;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GroupManager {
	private HashMap<String, ChatGroup> groups = new HashMap<String, ChatGroup>(); // mapping of group names to group objects
	private HashMap<String, String> chatTo = new HashMap<String, String>(); // mapping of player names to group names
	
	@SuppressWarnings("unused")
	private AsidePlugin plugin;
	
	public GroupManager(AsidePlugin plugin, HashMap<String, ChatGroup> groups) {
		this.plugin = plugin;
		this.groups = groups;
	}
	
	public HashSet<Player> getChannelRecipientsForMessageBy(String playername) {
		if (chatTo.get(playername) == null) {
			return null;
		}
		
		ChatGroup group = groups.get(chatTo.get(playername));
		
		if (group == null) {
			return null;
		}
		
		HashSet<Player> recipients = new HashSet<Player>();
		
		for (String recipient : group.getMembers()) {
			if (Bukkit.getPlayer(recipient) != null) {
				recipients.add(Bukkit.getPlayer(recipient));
			}
		}
		
		return recipients;
	}
	
	public ChatGroup getGroupByName(String groupname) {
		ChatGroup group = groups.get(groupname);
		
		if (group != null) {
			return group;
		}
		
		else {
			// TODO Try to find a group in config.yml; if one is found, deserialize, cache and return it
			return null;
		}
	}
}
