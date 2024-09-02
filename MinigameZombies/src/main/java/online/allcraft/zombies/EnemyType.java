package online.allcraft.zombies;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public class EnemyType {
	public Zombies plugin;
	public EntityType entityType;
	public int health;
	public int xpDropped;
	public boolean baby;
	public boolean drops;
	public Material helmet;
	public float helmetDropChance;
	public Material chestplate;
	public float chestplateDropChance;
	public Material leggings;
	public float leggingsDropChance;
	public Material boots;
	public float bootsDropChance;
	public Material itemInHand;
	public float itemInHandDropChance;
	
	// enemy is a static type - it is NOT a wrapper/referencer like Weapon
	public EnemyType(Zombies plugin,  EntityType entityType, int health, int xpDropped, boolean baby,
				 Material helmet, float helmetDropChance, 
				 Material chestplate, float chestplateDropChance, 
				 Material leggings, float leggingsDropChance,
				 Material boots, float bootsDropChance,
				 Material itemInHand, float itemInHandDropChance) {
		this.plugin = plugin;
		this.entityType = entityType;
		this.xpDropped = xpDropped;
		this.health = health;
		this.baby = baby;
		this.drops = true;
		this.helmet = helmet;
		this.helmetDropChance = helmetDropChance;
		this.chestplate = chestplate;
		this.chestplateDropChance = chestplateDropChance;
		this.leggings = leggings;
		this.leggingsDropChance = leggingsDropChance;
		this.boots = boots;
		this.bootsDropChance = bootsDropChance;
		this.itemInHand = itemInHand;
		this.itemInHandDropChance = itemInHandDropChance;
	}
	
	public EnemyType(Zombies plugin,  EntityType entityType, int health, boolean drops, 
				 Material helmet,   Material chestplate,  
				 Material leggings,  Material boots,  Material itemInHand) {
	this.plugin = plugin;
	this.entityType = entityType;
	this.health = health;
	this.xpDropped = 0;
	this.baby = false;
	this.drops = drops;
	this.helmet = helmet;
	this.helmetDropChance = 0;
	this.chestplate = chestplate;
	this.chestplateDropChance = 0;
	this.leggings = leggings;
	this.leggingsDropChance = 0;
	this.boots = boots;
	this.bootsDropChance = 0;
	this.itemInHand = itemInHand;
	this.itemInHandDropChance = 0;
}
	
	public Creature spawn(World world, Location location) {
		Creature c = (Creature) world.spawnEntity(location, entityType);
		// helmet
		if (!(helmet == null)) {
			c.getEquipment().setHelmet(new ItemStack(helmet));
			c.getEquipment().setHelmetDropChance(helmetDropChance);
		}
		
		// chestplate
		if (!(chestplate == null)) {
			c.getEquipment().setChestplate(new ItemStack(chestplate));
			c.getEquipment().setChestplateDropChance(chestplateDropChance);
		}
		
		// leggings
		if (!(leggings == null)) {
			c.getEquipment().setLeggings(new ItemStack(leggings));
			c.getEquipment().setLeggingsDropChance(leggingsDropChance);
		}
		
		// boots
		if (!(boots == null)) {
			c.getEquipment().setBoots(new ItemStack(boots));
			c.getEquipment().setBootsDropChance(bootsDropChance);
		}
		
		// item
		if (!(itemInHand == null)) {
			c.getEquipment().setItemInMainHand(new ItemStack(itemInHand));
			c.getEquipment().setItemInMainHandDropChance(itemInHandDropChance);
		}
		
		c.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
		if (entityType.equals(EntityType.ZOMBIE)) {
			c.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(0);
			Zombie zombie = (Zombie) c;
			zombie.setBaby(baby);
		}
		
		c.setTarget(getClosestPlayer(c));
		return c;
	}
	
	public Player getClosestPlayer(Entity entity) {
		ArrayList<Player> onlinePlayers = new ArrayList<Player>(plugin.getServer().getOnlinePlayers());
		double lastDistance = Double.MAX_VALUE;
		double currentDistance;
		Player player = null;
		for (Player p : onlinePlayers) {
			currentDistance = p.getLocation().distance(entity.getLocation());
			if (currentDistance < lastDistance) {
				player = p;
			}
		}
		
		if (player == null) {
			plugin.getServer().broadcastMessage("DEBUG MESSAGE: ERROR! NULL #50 - NO PLAYER");
		}
		
		return player;
	}
}
