package online.allcraft.bedwars.listeners;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import online.allcraft.bedwars.Bedwars;
import online.allcraft.bedwars.SpectatorDelayer;
import online.allcraft.bedwars.Team;

public class PlayerDeathListener implements Listener {

	private final Bedwars plugin;
	
	public PlayerDeathListener(Bedwars plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Team team = plugin.playerTeams.get(player);
		
		if (team.bed == null) {

			player.getWorld().strikeLightningEffect(player.getLocation());
			
			event.setDeathMessage(ChatColor.YELLOW + "Final Kill! " + event.getDeathMessage());
			
			ArrayList<Player> onlinePlayers = new ArrayList<Player>(plugin.getServer().getOnlinePlayers());
			
			// 1 is the player that just died since we can't remove from map
			if (!(team.players.size() <= 1)) {
				for (Player onlinePlayer : onlinePlayers) {
					onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 0);
					onlinePlayer.sendTitle(ChatColor.YELLOW + team.name + " Team Eliminated!", "", 4, 15, 4);
					plugin.teams.remove(team);
				}
				
				if (plugin.teams.size() <= 1) {
					plugin.endGame();
				}
			}
			
		} else {
			event.setDeathMessage(ChatColor.YELLOW + event.getDeathMessage());
		}
		event.setDroppedExp(0);
	}
}
