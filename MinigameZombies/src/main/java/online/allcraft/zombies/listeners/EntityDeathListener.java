package online.allcraft.zombies.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import online.allcraft.zombies.EnemyType;
import online.allcraft.zombies.Zombies;

public class EntityDeathListener implements Listener {

	private final Zombies plugin;

	public EntityDeathListener(Zombies plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity livingEntity = event.getEntity();
		if (plugin.enemyMap.containsKey(livingEntity)) {
			EnemyType enemy = plugin.enemyMap.get(livingEntity);
			event.setDroppedExp(enemy.xpDropped);
			if (!enemy.drops) {
				event.getDrops().clear();
			}
			
			plugin.enemyMap.remove(livingEntity);
			if (plugin.enemyMap.isEmpty() && plugin.enemyGeneratorFinished) {
				plugin.nextRound();
			}
		}
	}

}
