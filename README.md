# Aside
### *Chat improvements, done right*

Aside is a chat improvement plugin that adds a variety of features to server chat.

## Why Aside?
Most other chat enhancement plugins out there are clunky and unintuitive for the average user. Chat channels take forever to set up, and they divide your server community. Private messaging is single-recipient and relies on slash commands that are hard for newer users to get the hang of.
*Aside is intuitive, Aside is self-demonstrating, Aside does it better... by doing it differently.*

## Features

### Never miss a message
Messages that mention your name or a group of which you're a member will play a sound notification when they come in. If you're AFK, messages that mention you will be saved for easy viewing when you return.

### Private messaging without the hassle
Send private messages without using any commands, even if you're messaging multiple players. Create chatgroups for those people you're always talking to all at once.

## Configuration

### User-friendly installation
Installing and using Aside involves as little configuration as possible. Just download the plugin, pop it in the plugins folder, and give users a single permissions node (*aside.user*). Then start up the server and you're ready to use Aside to its full extent.

### Easy maintenance option
In the automatically-generated config file, you'll find a single option labeled "auto-update". Keep this set to true and Aside will automatically keep itself up to date with the latest stable version from BukkitDev.

## Using Aside

### Get someone's attention
Including the tag *@username* anywhere in a chat message will send a sound notification to the person you mentioned if they're online. Include as many of these (and other) tags in your messages as you want.

### Speak off the record
Including a *>username* tag will send the message privately to the person you mentioned, along with a sound notification. Specifically mentioned people will see the message, but it won't be exposed to the whole server population.

### Hold a private conference
Including the *>>groupname* tag will send the message privately to every person who's a member of the specified group, along with a sound notification to each.
To create and manage groups, use the following commands, which do exactly what you'd expect:
+ */group create  <groupname>              [list of members separated by spaces]*
+ */group delete  <groupname>*
+ */group add     <groupname> <playername> [list of additional playernames separated by spaces]*
+ */group remove  <groupname> <playername> [list of additional playernames separated by spaces]*
+ */group members <groupname>*