package online.allcraft.zombies.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import online.allcraft.zombies.GamePlayer;
import online.allcraft.zombies.Zombies;

public class PlayerJoinListener implements Listener {

	private final Zombies plugin;

	public PlayerJoinListener(Zombies plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (plugin.gamePlayers.containsKey(player)) {
			return;
		}
		
		for (Location location : plugin.doorLocations.keySet()) {
			plugin.getServer().broadcastMessage(location.toString());
		}
		
		if (!plugin.gameRunning) {
			if (!plugin.gamePlayers.containsKey(player)) {
				plugin.gamePlayers.put(player, new GamePlayer(player, plugin.starterMoney, plugin.starterXp));
			}
			int playersOnline = plugin.getServer().getOnlinePlayers().size();
			if (playersOnline > plugin.playersPerGame) {
				plugin.sendPlayerToFallback(player);
				return;
			} else if (playersOnline == 1) {
				plugin.startCountdown();
				return;
			} else {
				//wait
				return;
			}
		}
		
		// Player is new
		plugin.sendPlayerToFallback(player);
	}
}
