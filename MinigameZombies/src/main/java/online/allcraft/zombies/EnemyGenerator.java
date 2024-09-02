package online.allcraft.zombies;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.scheduler.BukkitRunnable;

public class EnemyGenerator extends BukkitRunnable {

	private final Zombies plugin;
	private int enemiesLeft;
	private RoundData roundData;
	
	public EnemyGenerator(Zombies plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		if (roundData != plugin.enemySpawningData.get(plugin.roundNumber)) {
			roundData = plugin.enemySpawningData.get(plugin.roundNumber);
			enemiesLeft = roundData.enemyAmount;
		}
		
		if (enemiesLeft <= 0) {
			plugin.getServer().broadcastMessage("finished");
			plugin.enemyGeneratorFinished = true;
			cancel();
			plugin.getServer().broadcastMessage("canceled");
			return;
		}
		
		double random = Math.random();
		double cumProb = 0.0;
		for (EnemyType enemy : roundData.enemyProbabilities.keySet()) {
			cumProb += roundData.enemyProbabilities.get(enemy);
			if (random <= cumProb) {
				// random room
				Room room = plugin.staticOpenRooms.get(new Random().nextInt(plugin.staticOpenRooms.size()));
			    // random spawner
				Location loc = room.spawners.get(new Random().nextInt(room.spawners.size()));
				
				// spawn
				Creature c = enemy.spawn(plugin.getServer().getWorld("world"), loc);
				plugin.enemyMap.put(c, enemy);
				
				plugin.log.info("DEBUG LOG: Enemy spawned in room " + room.name + " at " + loc.toString());
				enemiesLeft--;
				break;
			}
		}
	}
}
