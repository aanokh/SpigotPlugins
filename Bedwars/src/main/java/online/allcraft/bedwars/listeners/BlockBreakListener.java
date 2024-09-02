package online.allcraft.bedwars.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import online.allcraft.bedwars.Bedwars;
import online.allcraft.bedwars.Team;

public class BlockBreakListener implements Listener {

	private final Bedwars plugin;

	public BlockBreakListener(Bedwars plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		
		if (event.getBlock().getType().equals(Material.BED)) {
			// Players can't break their own bed
			if (plugin.playerTeams.get(event.getPlayer()).bed.equals(event.getBlock())) {
				event.setCancelled(true);
				return;
			}
			
			for (Team currentTeam : plugin.teams) {
				if (event.getBlock().equals(currentTeam.bed)) {
					currentTeam.setBed(null);

					for (Player player : plugin.playerTeams.keySet()) {
						player.sendTitle(ChatColor.RED + "Bed Destroyed!", currentTeam.name + " Team's Bed Destroyed!", 4, 15, 4);
						player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 10, 1);
					}
				}
			}
		} else if (!plugin.placedBlocks.contains(event.getBlock())) {
			event.setCancelled(true);
		}
		
	}
}
