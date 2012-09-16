package me.makskay.bukkit.aside;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GroupManager {
	private HashMap<String, ChatGroup> groups; // mapping of group names to group objects
	
	private AsidePlugin plugin;
	
	public GroupManager(AsidePlugin plugin) {
		this.plugin = plugin;
		groups = new HashMap<String, ChatGroup>();
	}
	
	public ChatGroup getGroupByName(String groupname) {
		ChatGroup group = groups.get(groupname);
		
		if (group != null) {
			return group;
		}
		
		String path = "groups." + groupname;
		String ownername = plugin.groupsYml.getConfig().getString(path + ".owner");
		List<String> members = plugin.groupsYml.getConfig().getStringList(path + ".members");
		
		if ((ownername != null) && (members != null)) {
			group = new ChatGroup(groupname, ownername, members);
			groups.put(groupname, group);
			
			return group;
		}
		
		return null;
	}
	
	public void registerGroup(ChatGroup group) {
		groups.put(group.getName(), group);
	}

	public void deleteGroup(String groupname) {
		groups.remove(groupname);
	}

	public void addMemberToGroup(String groupname, String playername) {
		getGroupByName(groupname).addMember(playername);
	}

	public void removeMemberFromGroup(String groupname, String playername) {
		getGroupByName(groupname).removeMember(playername);	
	}
	
	public Collection<ChatGroup> getLoadedGroups() {
		return groups.values();
	}
	
	public HashSet<String> getAllGroupNames() {
		HashSet<String> groupNames = new HashSet<String>();
		
		for (ChatGroup group : getLoadedGroups()) {
			groupNames.add(group.getName());
		}
		
		for (String groupName : plugin.groupsYml.getConfig().getConfigurationSection("groups").getKeys(false)) {
			groupNames.add(groupName);
		}
		
		return groupNames;
	}
}
