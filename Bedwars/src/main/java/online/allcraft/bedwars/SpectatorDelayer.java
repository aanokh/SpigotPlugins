package online.allcraft.bedwars;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpectatorDelayer extends BukkitRunnable {

	public final Bedwars plugin;
	public final Player player;
	public final boolean launchSpawnCountdown;
	
	public SpectatorDelayer(Bedwars plugin, Player player, boolean launchSpawnCountdown) {
		this.plugin = plugin;
		this.player = player;
		this.launchSpawnCountdown = launchSpawnCountdown;
	}
	
	@Override
	public void run() {
		player.setGameMode(GameMode.SPECTATOR);
		if (launchSpawnCountdown) {
			new SpawnCountdown(plugin, player, 15).runTaskTimer(plugin, 0, plugin.SPAWN_COUNTDOWN_SPEED);
		}
		
		cancel();
	}

}
