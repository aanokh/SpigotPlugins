package online.allcraft.gunsCore;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface ShooterMinigame {
	public void onHit(EntityDamageByEntityEvent event, Weapon weapon);
}
