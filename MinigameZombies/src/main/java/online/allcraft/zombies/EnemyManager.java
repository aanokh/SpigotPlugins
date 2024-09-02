package online.allcraft.zombies;

import org.bukkit.scheduler.BukkitRunnable;

public class EnemyManager extends BukkitRunnable {

	private final Zombies plugin;
	
	public EnemyManager(Zombies plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		//TODO
		// generate particles at enemies
		// reset enemy targeting
		// barricade destruction (here?)
		// advanced enemy ai
	}
}
