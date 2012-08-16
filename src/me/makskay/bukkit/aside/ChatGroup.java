package me.makskay.bukkit.aside;

import java.util.List;
import java.util.HashSet;

public class ChatGroup {
	private String owner;
	private HashSet<String> members;
	
	public ChatGroup(String owner, List<String> membersFromFile) {
		this.owner = owner;
		
		this.members = new HashSet<String>();
		for (String member : membersFromFile) {
			this.members.add(member);
		}
	}
	
	public String getOwner() {
		return this.owner;
	}
	
	public HashSet<String> getMembers() {
		return this.members;
	}
}
