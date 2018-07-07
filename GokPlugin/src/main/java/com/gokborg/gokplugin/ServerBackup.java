package com.gokborg.gokplugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ServerBackup {

	
	/**
	 * Holds a list of backups. Each backup is like a clip board. It stores a list of clip boards.
	 * List<List<Block>> - First list holds a group of clip boards.
	 * 					 - Second list is a single clip board.
	 */
	private HashMap<Player, List<List<Block>>> serverBackup = new HashMap<Player, List<List<Block>>>();
	
	public List<Block> getEntry(Player p, int index) {
		return serverBackup.get(p).get(index);	
	}
	
	public void addFullEntry(Player p, List<List<Block>> b) {
		serverBackup.put(p, b);
	}
	
	
	/**
	 * Add a block to a player's backup.
	 * 
	 * @param p - To access a player's backup
	 * @param b - A block to add to the player's backup
	 * @param index - An index for which player backup to access 
	 */		
	
	public void addEntry(Player p, Block b, int index) {
		Block newBlock = b;
		
		if(serverBackup.containsKey(p) && serverBackup.get(p).size() >= 0 && index >= 0) {
			serverBackup.get(p).get(index).add(newBlock);	
		}
		else {
			
			serverBackup.put(p, new ArrayList<List<Block>>());
			serverBackup.get(p).add(new ArrayList<Block>());
			serverBackup.get(p).get(0).add(newBlock);	
		}
	}
	

	
	public boolean exists(Player p) {
		if (serverBackup.containsKey(p)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void removeEntry(Player p) {
		serverBackup.get(p).remove(this.getSize(p)-1);
	}
	
	
	/**
	 * To get the size of a players backup.
	 * 
	 * @param p - To get a specific player's backup size 
	 */
	
	public int getSize(Player p) {
		if (serverBackup.containsKey(p))
			return serverBackup.get(p).size();
		else
			return 0;
	}
	
	
	public HashMap<Player, List<List<Block>>> getServerBackup(){
		return serverBackup;
	}
	
	public void setServerBackup(HashMap<Player, List<List<Block>>> block) {
		serverBackup = block;
	}
}
