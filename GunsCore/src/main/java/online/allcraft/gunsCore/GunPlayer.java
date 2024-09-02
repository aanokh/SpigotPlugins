package online.allcraft.gunsCore;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GunPlayer {
	
	GunsCore plugin;
	
	private Player player;
	private HashMap<Material, Weapon> weapons;
	
	public GunPlayer(Player player, HashMap<Material, Weapon> starterWeapons) {
		this.player = player;
		weapons = starterWeapons;
	}
	
	public HashMap<Material, Weapon> getWeapons() {
		return weapons;
	}

}
