package online.allcraft.gunsCore.listeners;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import online.allcraft.gunsCore.GunsCore;

public class ProjectileHitListener implements Listener {

	private final GunsCore plugin;

	public ProjectileHitListener(GunsCore plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		plugin.snowballToWeapon.remove(event.getEntity());
	}

}
