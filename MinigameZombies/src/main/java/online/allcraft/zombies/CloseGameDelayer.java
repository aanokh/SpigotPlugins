package online.allcraft.zombies;

import org.bukkit.scheduler.BukkitRunnable;

public class CloseGameDelayer extends BukkitRunnable {
	public final Zombies plugin;
	
	public CloseGameDelayer(Zombies plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		plugin.closeGame();
		
		cancel();
	}

}
