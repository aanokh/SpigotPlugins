package online.allcraft.gunsCore.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import online.allcraft.gunsCore.GunPlayer;
import online.allcraft.gunsCore.GunsCore;
import online.allcraft.gunsCore.ShooterMinigame;
import online.allcraft.gunsCore.Weapon;

public class EntityDamageByEntityListener implements Listener {

	private final GunsCore plugin;

	public EntityDamageByEntityListener(GunsCore plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Snowball) {
			if (event.getEntity() instanceof LivingEntity) {
				if (plugin.snowballToWeapon.containsKey(event.getDamager())) {
					Weapon weapon = plugin.snowballToWeapon.get(event.getDamager());
					int damage = weapon.weaponType.damage;
					LivingEntity entity = (LivingEntity) event.getEntity();
					entity.damage(damage);
					
					GunPlayer gamePlayer = plugin.gunPlayers.get(weapon.player);
					
					for (ShooterMinigame minigame : plugin.shooterMinigames) {
						minigame.onHit(event, weapon);
					}
				}
			}
		}
	}

}
