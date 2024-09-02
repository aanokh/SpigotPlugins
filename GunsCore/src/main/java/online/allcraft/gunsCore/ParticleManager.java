package online.allcraft.gunsCore;

import org.bukkit.Particle;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleManager extends BukkitRunnable {

	private final GunsCore plugin;
	
	public ParticleManager(GunsCore plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for (Snowball snowball : plugin.snowballToWeapon.keySet()) {
			//snowball.getWorld().playEffect(snowball.getLocation(), Effect., 0);
			snowball.getWorld().spawnParticle(Particle.REDSTONE, snowball.getLocation(), 1);

			//ParticleEffect.FIREWORKS_SPARK.display(0, 0, 0, 0, 3, snowball.getLocation(), 100);
		}
	}

}
