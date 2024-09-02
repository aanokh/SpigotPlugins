package online.allcraft.gunsCore.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import online.allcraft.gunsCore.GunsCore;
import online.allcraft.gunsCore.Weapon;

public class PlayerInteractListener implements Listener {

	private final GunsCore plugin;

	public PlayerInteractListener(GunsCore plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteraction(PlayerInteractEvent event) {
		Weapon weapon;
		Material material = event.getMaterial();
		Player player = event.getPlayer();

		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (plugin.weaponTypeMaterials.containsKey(material)) {
				weapon = plugin.gunPlayers.get(player).getWeapons().get(material);
				weapon.shoot(event.getItem());
				event.setCancelled(true);
			}
		} else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (plugin.weaponTypeMaterials.containsKey(material)) {
				weapon = plugin.gunPlayers.get(player).getWeapons().get(material);
				weapon.startReload();
				event.setCancelled(true);
			}
		}
	}

}
