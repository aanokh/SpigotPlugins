package online.allcraft.gunsCore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import online.allcraft.gunsCore.GunsCore;
import online.allcraft.gunsCore.Weapon;

public class PlayerDropItemListener implements Listener {

	private final GunsCore plugin;

	public PlayerDropItemListener(GunsCore plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		
		if (!plugin.preventWeaponDrops) {
			return;
		}
		
		if (!plugin.gunPlayers.containsKey(player)) {
			return;
		}
		
		for (Weapon weapon : plugin.gunPlayers.get(player).getWeapons().values()) {
			if (event.getItemDrop().getItemStack().getType() == weapon.weaponType.material) {
				event.setCancelled(true);
				break;
			}
		}
	}

}
