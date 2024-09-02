package online.allcraft.zombies;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import online.allcraft.gunsCore.GunPlayer;
import online.allcraft.gunsCore.Weapon;
import online.allcraft.gunsCore.WeaponType;

public class WeaponStation {
	
	Zombies plugin;
	
	public Location location;
	public WeaponType weaponType;
	public int cost;
	
	public WeaponStation(Zombies plugin, Location location, WeaponType weaponType, int cost) {
		this.plugin = plugin;
		this.location = location;
		this.weaponType = weaponType;
		this.cost = cost;
	}
	
	public void giveItem(Player player) {
		GunPlayer gunPlayer = plugin.gunsCore.gunPlayers.get(player);
		for (Weapon weapon : gunPlayer.getWeapons().values()) {
			if (weapon.weaponType.equals(weaponType)) {
				return;
			}
		}
		
		Weapon weapon = new Weapon(weaponType, player);
		gunPlayer.getWeapons().put(weapon.weaponType.material, weapon);
		weapon.givePlayerOnStart();
	}
}
