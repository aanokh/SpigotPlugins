package online.allcraft.bedwars.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import online.allcraft.bedwars.Bedwars;
import online.allcraft.bedwars.Team;

public class BlockPlaceListener implements Listener {

	private final Bedwars plugin;

	public BlockPlaceListener(Bedwars plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		plugin.placedBlocks.add(event.getBlock());
	}
}
