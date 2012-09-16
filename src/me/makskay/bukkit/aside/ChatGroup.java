package me.makskay.bukkit.aside;

import java.util.ArrayList;
import java.util.List;

public class ChatGroup {
	private String name;
	private String owner;
	private ArrayList<String> members;
	
	public ChatGroup(String name, String owner, List<String> membersFromFile) {
		this.name = name;
		this.owner = owner;
		
		this.members = new ArrayList<String>();
		for (String member : membersFromFile) {
			this.members.add(member);
		}
	}
	
	public ChatGroup(String name, String owner) {
		this.name = name;
		this.owner = owner;
		this.members = new ArrayList<String>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getOwner() {
		return this.owner;
	}
	
	public ArrayList<String> getMembers() {
		return this.members;
	}
	
	public void addMember(String playername) {
		members.add(playername);
	}

	public void removeMember(String playername) {
		members.remove(playername);
	}
}
