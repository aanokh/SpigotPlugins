package online.allcraft.gunsCore;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class WeaponType {

	public GunsCore plugin;
	
	public String name;
	public Material material;
	public Sound shootSound;
	public Sound reloadSound;
	public Sound shootFailSound;
	public boolean ranged;
	public int damage;
	public boolean fireAspect;
	
	// Ranged only
	public Particle particle;
	public int maxAmmo;
	public int clipSize;
	public int maxShootingDelay;
	public int maxReloadDelay;
	
	// Melee only?
	public int knockback;
	
	public WeaponType(GunsCore plugin, String name, Material material, Sound shootSound, Sound reloadSound, Sound shootFailSound, boolean ranged, int damage, 
					int knockback, int maxShootingDelay, int maxReloadDelay, boolean fireAspect, Particle particle, int maxAmmo, int clipSize) {
		this.plugin = plugin;
		this.name = name;
		this.material = material;
		this.shootSound = shootSound;
		this.reloadSound = reloadSound;
		this.shootFailSound = shootFailSound;
		this.ranged = ranged;
		this.damage = damage;
		this.knockback = knockback;
		this.maxShootingDelay = maxShootingDelay;
		this.maxReloadDelay = maxReloadDelay;
		this.fireAspect = fireAspect;
		this.particle = particle;
		this.maxAmmo = maxAmmo;
		this.clipSize = clipSize;
	}
	
	/*
	public Weapon makeWeapon(Player player) {
		return new Weapon(this, player);
	}
	*/
}
