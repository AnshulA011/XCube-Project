package com.gokborg.gokplugin;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.ecconia.rsisland.plugin.selection.api.ISelection;
import com.ecconia.rsisland.plugin.selection.api.SelectionAPI;

public class GokPlugin extends JavaPlugin {

	private SelectionAPI selection;
	// Cool Msg Class
	private Feedback f;
	private ServerBackup serverBackup = new ServerBackup();

	@Override
	public void onEnable() {
		f = new Feedback(ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "GokEdit" + ChatColor.GRAY + "] ");
		RegisteredServiceProvider<SelectionAPI> provider = getServer().getServicesManager()
				.getRegistration(SelectionAPI.class);
		if (provider != null) {
			selection = provider.getProvider();
		} else {
			getLogger().severe(
					"Download Selection Plugin on server: https://github.com/RSIsland/SelectionPlugin OR contact Gokborg");
			onDisable();
			return;
		}

		getServer().getPluginManager().registerEvents(new Events(this), this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (label.equalsIgnoreCase("ifloss") && sender instanceof Player
				&& sender.hasPermission("gokplugin.permission")) {
			sender.sendMessage("uraliar " + sender.getName());
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("up") && sender instanceof Player
				&& sender.hasPermission("gokplugin.permission")) {
			Player player = (Player) sender;
			Block block = player.getLocation().subtract(0, 1, 0).getBlock();
			block.setType(Material.GLASS);

			return true;
		}
		// FILL COMMAND
		// TODO: Add backup to fill also
		if (label.equalsIgnoreCase("fill") && sender instanceof Player
				&& sender.hasPermission("gokplugin.permission")) {
			Player player = (Player) sender;
			serverBackup.exists(player);

			ISelection selinterface = selection.getPlayer(player).getSelectionOrCurrent("gokplugin");
			if (selinterface == null) {
				f.e(player, "You must make a selection!");
			} else if (args.length != 1) {
				f.e(player, "Please put a material and thats it!");
			}

			else {

				Material fillMaterial = Material.matchMaterial(args[0]);

				if (fillMaterial == null) {
					f.e(player, "Invalid block type!");
				} else {

					// Debug MSG: System.out.println(serverBackup.getEntry(player,
					// index).toString());

					World world = player.getWorld();
					Location pos1 = selinterface.getMinPoint();
					Location pos2 = selinterface.getMaxPoint();
					int x1 = pos1.getBlockX();
					int y1 = pos1.getBlockY();
					int z1 = pos1.getBlockZ();
					int x2 = pos2.getBlockX();
					int y2 = pos2.getBlockY();
					int z2 = pos2.getBlockZ();
					//new Thread() {
						//public void run() {
							for (int x = x1; x <= x2; x++) {
								for (int z = z1; z <= z2; z++) {
									for (int y = y1; y <= y2; y++) {
										Block block = world.getBlockAt(x, y, z);
										block.setType(fillMaterial);

									}
								}
							}
						//}
					//};
					f.n(player, "Area has been filled!");
				}

			}

			return true;
		}

		if (label.equalsIgnoreCase("replace") && sender instanceof Player
				&& sender.hasPermission("gokplugin.permission")) {
			// TODO: index changes to -1 [NULL EXCEPTION!]

			Player player = (Player) sender;
			serverBackup.exists(player);
			ISelection selinterface = selection.getPlayer(player).getSelectionOrCurrent("gokplugin");
			int index = serverBackup.getSize(player) - 1;
			if (args.length != 2) {
				f.e(player, "Two arguments required!");
			} else if (Material.matchMaterial(args[0]) == null || Material.matchMaterial(args[1]) == null) {
				f.e(player, "Invalid arguments!");
			} else if (selinterface == null) {
				f.e(player, "You must make a selection!");
			} else {
				World world = player.getWorld();
				Location pos1 = selinterface.getMinPoint();
				Location pos2 = selinterface.getMaxPoint();
				Block block;
				int x1 = pos1.getBlockX();
				int y1 = pos1.getBlockY();
				int z1 = pos1.getBlockZ();
				int x2 = pos2.getBlockX();
				int y2 = pos2.getBlockY();
				int z2 = pos2.getBlockZ();
				for (int x = x1; x <= x2; x++) {
					for (int z = z1; z <= z2; z++) {
						for (int y = y1; y <= y2; y++) {
							block = world.getBlockAt(x, y, z);
							if (block.getType() == Material.matchMaterial(args[0])) {
								serverBackup.addEntry(player, block, index);
								block.setType(Material.matchMaterial(args[1]));
							}
						}
					}
				}

				f.n(player, "Replace complete!");
			}
			return true;
		}

		if (label.equalsIgnoreCase("undo") && sender instanceof Player
				&& sender.hasPermission("gokplugin.permission")) {
			Player player = (Player) sender;
			int index = serverBackup.getSize(player) - 1;

			// Means that the player does have a backup

			if (serverBackup.exists(player) && index >= 0) {

				World world = player.getWorld();
				List<Block> blocks = serverBackup.getEntry(player, index);

				for (int i = 0; i != blocks.size(); i++) {
					Block block = blocks.get(i);
					world.getBlockAt(block.getLocation()).setType(block.getType());
				}
				serverBackup.removeEntry(player);
				f.n(player, "Undo complete!");

			} else {
				f.n(player, "No operation to undo.");
			}
			return true;
		}

		// TODO: Copy command
		if (label.equalsIgnoreCase("copy") && sender instanceof Player
				&& sender.hasPermission("gokplugin.permission")) {

			Player player = (Player) sender;
			ISelection selinterface = selection.getPlayer(player).getSelectionOrCurrent("gokplugin");
			if (selinterface == null) {
				f.e(player, "%v , you must make a selection!", player.getName());
			} else {
				f.n(player, "Your good!");
			}

			return true;
		}
		// TODO: Paste command
		if (label.equalsIgnoreCase("paste") && sender instanceof Player
				&& sender.hasPermission("gokplugin.permission")) {
			Player player = (Player) sender;
			f.n(player, "This command has not be set up yet!");
			return true;
		}

		if (label.equalsIgnoreCase("ss") && sender instanceof Player && sender.hasPermission("gokplugin.permssion")) {
			Player p = (Player) sender;
			ItemStack is = new ItemStack(Material.GOLD_AXE);
			ItemMeta i = is.getItemMeta();
			i.setDisplayName("SS Detector");
			is.setItemMeta(i);
			p.getInventory().addItem(is);
			return true;
		}

		return false;
	}

}
