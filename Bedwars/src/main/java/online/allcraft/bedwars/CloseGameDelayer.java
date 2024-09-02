package online.allcraft.bedwars;

import org.bukkit.scheduler.BukkitRunnable;

public class CloseGameDelayer extends BukkitRunnable {
	public final Bedwars plugin;
	
	public CloseGameDelayer(Bedwars plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		plugin.closeGame();
		
		cancel();
	}

}
