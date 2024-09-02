package online.allcraft.bedwars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import online.allcraft.bedwars.Bedwars;
import online.allcraft.bedwars.SpectatorDelayer;
import online.allcraft.bedwars.Team;

public class PlayerRespawnListener implements Listener {

	private final Bedwars plugin;
	
	public PlayerRespawnListener(Bedwars plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		Team team = plugin.playerTeams.get(player);

		if (team == null) {
			plugin.getServer().broadcastMessage("DEBUG MESSAGE! ERROR: NULL #32");
		}

		if (team.bed == null) {
			new SpectatorDelayer(plugin, player, false).runTaskLater(plugin, plugin.SPECTATOR_DELAYER_DELAY);
			// So when player joins again, they do not get a shortcut
			plugin.playerTeams.remove(player);
			team.players.remove(player);
		} else {
			new SpectatorDelayer(plugin, player, true).runTaskLater(plugin, plugin.SPECTATOR_DELAYER_DELAY);
		}
	}
}
