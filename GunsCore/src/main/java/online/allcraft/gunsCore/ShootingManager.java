package online.allcraft.gunsCore;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ShootingManager extends BukkitRunnable {

	private final GunsCore plugin;
	//private ArrayList<Integer> toRemove;
	
	public ShootingManager(GunsCore plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		//toRemove = new ArrayList<Integer>();
		/*
		Weapon weapon;
		int index;
		ListIterator<Weapon> iterator = plugin.shootingDelays.listIterator();
		while (iterator.hasNext()) {
			index = iterator.nextIndex();
			weapon = plugin.shootingDelays.get(index);
			weapon.setShootingDelay(weapon.getShootingDelay() - 1);
			if (weapon.getShootingDelay() <= 0) {
				plugin.shootingDelays.remove(weapon);
			}
		}
		*/
		for (Weapon weapon : plugin.shootingDelays) {
			weapon.shootingDelay -= 1;
			if (weapon.shootingDelay <= 0) {
				plugin.shootingDelays.remove(weapon);
			}
		}

		for (Weapon weapon : plugin.reloadDelays) {
			weapon.reloadDelay -= 1;
			if (weapon.reloadDelay <= 0) {
				plugin.reloadDelays.remove(weapon);
				weapon.reload();
				break;
			}
			int perIteration = weapon.weaponType.material.getMaxDurability() / weapon.weaponType.maxReloadDelay;
			weapon.updateReload(weapon.reloadDelay * perIteration);
		}
		
		/*
		for (int i : toRemove) {
			plugin.shootingDelays.remove(i);
		}
		*/
	}

}
