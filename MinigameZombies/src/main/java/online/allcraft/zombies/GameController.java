package online.allcraft.zombies;


import org.bukkit.scheduler.BukkitRunnable;

public class GameController extends BukkitRunnable {

	private final Zombies plugin;
	
	public GameController(Zombies plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		
		if (plugin.enemyMap.isEmpty()) {
			
		}
		
	}

}
