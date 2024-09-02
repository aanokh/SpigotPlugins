package online.allcraft.bedwars.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import online.allcraft.bedwars.Bedwars;
import online.allcraft.bedwars.SpectatorDelayer;

public class PlayerJoinListener implements Listener {

	private final Bedwars plugin;

	public PlayerJoinListener(Bedwars plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (plugin.playerTeams.containsKey(player)) {
			new SpectatorDelayer(plugin, player, true).runTaskLater(plugin, plugin.SPECTATOR_DELAYER_DELAY);
			return;
		}
		
		if (!plugin.gameRunning) {
			int playersPerGame = plugin.playersPerTeam * plugin.teams.size();
			playersPerGame = 2;
			int playersOnline = plugin.getServer().getOnlinePlayers().size();
			
			if (playersOnline > playersPerGame) {
				plugin.sendPlayerToFallback(player);
				return;
			} else if (playersOnline == playersPerGame) {
				plugin.startCountdown();
				return;
			} else {
				//wait
				return;
			}
		}
		
		plugin.sendPlayerToFallback(player);
	}
}
