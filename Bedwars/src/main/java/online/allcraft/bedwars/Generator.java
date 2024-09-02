package online.allcraft.bedwars;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Generator {
	public Material material;
	public Location location;
	public Bedwars plugin;
	
	//ticks
	public int delay;
	public int delay_1;
	public int delay_2;
	public int delay_3;
	
	public Generator(Bedwars plugin, Material material, Location location, int delay_ticks_1, int delay_ticks_2, int delay_ticks_3) {
		this.plugin = plugin;
		this.material = material;
		this.location = location.getBlock().getLocation().add(+0.5, +1, +0.5);
		this.delay_1 = delay_ticks_1;
		this.delay_2 = delay_ticks_2;
		this.delay_3 = delay_ticks_3;
		this.delay = this.delay_1;
	}
	
	public void levelUp() {
		if (delay == delay_1) {
			delay = delay_2;
		} else if (delay == delay_2) {
			delay = delay_3;
		}
	}
	
	public void generate() {
		World world = location.getWorld();
		ItemStack stack = new ItemStack(material, 1);
		Item item = world.dropItem(location, stack);
		item.setVelocity(new Vector(0, 0, 0));
	}
}