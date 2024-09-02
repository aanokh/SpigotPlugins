package online.allcraft.gunsCore.listeners;

import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import net.md_5.bungee.api.ChatColor;
import online.allcraft.gunsCore.GunPlayer;
import online.allcraft.gunsCore.GunsCore;
import online.allcraft.gunsCore.Weapon;
import online.allcraft.gunsCore.WeaponType;

public class PlayerJoinListener implements Listener {

	private final GunsCore plugin;
	

	public PlayerJoinListener(GunsCore plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		HashMap<Material, Weapon> starterWeapons = new HashMap<Material, Weapon>();
		
		if (!plugin.gunPlayers.containsKey(player)) {
			for (WeaponType weaponType : plugin.starterWeaponTypes) {
				Weapon starterWeapon = new Weapon(weaponType, player);
				starterWeapon.givePlayerOnStart();
				starterWeapons.put(starterWeapon.weaponType.material, starterWeapon);
			}
			
			plugin.gunPlayers.put(event.getPlayer(), new GunPlayer(player, starterWeapons));
		}
	}
}
