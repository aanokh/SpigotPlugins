package online.allcraft.gunsCore.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import online.allcraft.gunsCore.GunsCore;
import online.allcraft.gunsCore.Weapon;

public class PlayerItemHeldListener implements Listener {

	private final GunsCore plugin;

	public PlayerItemHeldListener(GunsCore plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent event) {
		int index = event.getNewSlot();
		if (!(event.getPlayer().getInventory().getItem(index) == null)) {
			Material material = event.getPlayer().getInventory().getItem(index).getType();
			if (plugin.weaponTypeMaterials.containsKey(event.getPlayer().getInventory().getItem(index).getType())) {
				Weapon weapon = plugin.gunPlayers.get(event.getPlayer()).getWeapons().get(material);
				weapon.updateAmmo(weapon.ammo);
			}
		}
		
	}

}
