package online.allcraft.bedwars;

import org.bukkit.scheduler.BukkitRunnable;

public class GeneratorManager extends BukkitRunnable {

	private final Bedwars plugin;
	
	public GeneratorManager(Bedwars plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		for (Generator generator : plugin.generators) { 
			int delay = plugin.generatorDelays.get(generator);
			if (delay == 0) {
				generator.generate();
				plugin.generatorDelays.put(generator, generator.delay);
			} else {
				plugin.generatorDelays.put(generator, delay - 1);
			}
		}
	}

}
