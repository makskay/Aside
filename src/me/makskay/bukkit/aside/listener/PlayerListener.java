package me.makskay.bukkit.aside.listener;

import java.util.ArrayList;
import java.util.HashSet;

import me.makskay.bukkit.aside.AsidePlugin;
import me.makskay.bukkit.aside.ChatGroup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerListener implements Listener {
	private AsidePlugin plugin;
	
	public PlayerListener(AsidePlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		Player player = event.getPlayer();
		
		String[] words = message.split(" "); // split the message into separate words to be processed individually
		
		HashSet<Player> privateRecipients = new HashSet<Player>();
		HashSet<Player> directRecipients = new HashSet<Player>();
		HashSet<Player> channelRecipients = new HashSet<Player>();
		
		ArrayList<String> newMessage = new ArrayList<String>();
		
		for (Player recipient : Bukkit.getOnlinePlayers()) {
			channelRecipients.add(recipient); // TODO Implement proper chat-to-group functionality
		}
		
		for (String word : words) {
			if (word.startsWith(">")) { // if the word is a >mention tag
				Player recipient = plugin.getPlayerByNameSubstring(word.substring(1));
				
				if (recipient != null) {
					privateRecipients.add(recipient);
					newMessage.add(ChatColor.GRAY + word + ChatColor.WHITE); // add color formatting
				}
				
				else {
					newMessage.add(word);
				}
			}
			
			else if (word.startsWith("<")) { // if the word is a <mention tag
				ChatGroup group = plugin.getGroupManager().getGroupByName(word.substring(1));
				
				if (group != null) {
					HashSet<String> members = group.getMembers();
				
					for (String name : members) {
						Player recipient = Bukkit.getPlayer(name);
					
						if (recipient != null) {
							privateRecipients.add(recipient);
						}
					}
				
					if (members.isEmpty()) {
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
		
		event.getRecipients().clear(); // remove message recipients (everyone by default)
		
		if (privateRecipients.isEmpty()) { // if there are no >mention tags
			event.getRecipients().addAll(channelRecipients);
			event.getRecipients().addAll(directRecipients);
			
			//event.setFormat(ChatColor.WHITE + "[" + ChatColor.YELLOW + "@" + ChatColor.WHITE + "] " + event.getFormat());
			
			for (Player recipient : directRecipients) {
				recipient.playEffect(player.getLocation(), Effect.CLICK2, 0); // play sound notification for players @mentioned
			}
		}
		
		else { // if there are one or more >mention or <mention tags
			event.getRecipients().add(player);
			event.getRecipients().addAll(privateRecipients);
			
			//event.setFormat(ChatColor.WHITE + "[" + ChatColor.GRAY + ">" + ChatColor.WHITE + "] " + event.getFormat());
			
			for (Player recipient : privateRecipients) {
				recipient.playEffect(player.getLocation(), Effect.CLICK2, 0); // play sound notification for players >mentioned
			}
		}
		
		String newMessageText = "";
		for (String word : newMessage) {
			newMessageText = newMessageText + word + " "; // add color formatting to the outgoing message
		}
		
		event.setMessage(newMessageText.trim());
	}
}