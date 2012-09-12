package me.makskay.bukkit.aside;

import java.util.ArrayList;
import java.util.HashSet;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
	private AsidePlugin plugin;
	
	public PlayerListener(AsidePlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		
		String[] words = message.split(" "); // split the message into separate words to be processed individually
		
		HashSet<Player> privateRecipients = new HashSet<Player>();
		HashSet<Player> directRecipients = new HashSet<Player>();
		HashSet<Player> channelRecipients = new HashSet<Player>();
		
		ArrayList<String> newMessage = new ArrayList<String>();
		
		for (Player recipient : Bukkit.getOnlinePlayers()) {
			channelRecipients.add(recipient); // TODO Implement proper chat-to-group functionality
		}
		
		for (String word : words) {
			if (word.startsWith(">>")) { // if the word is a >>mention tag
				ChatGroup group = plugin.getGroupManager().getGroupByName(word.substring(2));
				
				if (group != null) {
					ArrayList<String> members = group.getMembers();
				
					for (String name : members) {
						Player recipient = Bukkit.getPlayer(name);
					
						if (recipient != null) {
							privateRecipients.add(recipient);
						}
					}
				
					if (!privateRecipients.isEmpty()) {
						newMessage.add(ChatColor.GRAY + word + ChatColor.WHITE); // add color formatting
					}
				
					else {
						newMessage.add(word);
					}
				}
				
				else {
					newMessage.add(word);
				}
			}
			
			else if (word.startsWith(">")) { // if the word is a >mention tag
				Player recipient = plugin.getPlayerByNameSubstring(word.substring(1));
				
				if (recipient != null) {
					privateRecipients.add(recipient);
					newMessage.add(ChatColor.GRAY + word + ChatColor.WHITE); // add color formatting
				}
				
				else {
					newMessage.add(word);
				}
			} 
			
			else if (word.startsWith("@")) { // if the word is an @mention tag
				Player recipient = plugin.getPlayerByNameSubstring(word.substring(1));
				
				if (recipient != null) {
					directRecipients.add(recipient);
					newMessage.add(ChatColor.YELLOW + word + ChatColor.WHITE); // add color formatting
				}
				
				else {
					newMessage.add(word);
				}
			}
			
			else {
				newMessage.add(word);
			}
		}
		
		String newMessageText = "";
		for (String word : newMessage) {
			newMessageText = newMessageText + word + " "; // add color formatting to the outgoing message
		}
		
		event.setMessage(newMessageText.trim());
		
		if (privateRecipients.isEmpty()) { // if there are no >mention or >>mention tags
			for (Player recipient : directRecipients) {
				recipient.playEffect(recipient.getLocation(), Effect.CLICK2, 0); // play sound notification for players @mentioned
				//plugin.getPlayerManager().saveMessage(recipient, player.getName() + ": " + newMessageText); // TODO Only do this if recip is AFK
			}
		}
		
		else { // if there are one or more >mention or >>mention tags
			event.getRecipients().clear();
			event.getRecipients().add(event.getPlayer());
			
			
			for (Player recipient : privateRecipients) {
				event.getRecipients().add(recipient);
				
				recipient.playEffect(recipient.getLocation(), Effect.CLICK2, 0); // play sound notification for players >mentioned or >>mentioned
				//plugin.getPlayerManager().saveMessage(recipient, player.getName() + ": " + newMessageText); // TODO Only do this if recip is AFK
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.getPlayerManager().registerPlayer(event.getPlayer());
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.getPlayerManager().releasePlayer(event.getPlayer());
	}
}