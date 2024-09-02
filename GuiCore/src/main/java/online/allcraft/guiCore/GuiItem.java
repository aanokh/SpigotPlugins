package online.allcraft.guiCore;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class GuiItem {

	public String name;
	public ArrayList<String> lore;
	public String description;
	public Material material;
	public GuiCore plugin;
	public int index;

	public GuiItem(GuiCore plugin, String name, ArrayList<String> lore, Material material, int index) {
		this.plugin = plugin;
		this.name = name;
		this.lore = lore;
		this.material = material;
		this.index = index;
	}

	public GuiItem(GuiCore plugin, String name, String description, ArrayList<String> lore, Material material, int index) {
		this.name = name;
		this.material = material;
		this.plugin = plugin;
		this.index = index;
		this.lore = lore;
		this.description = description;
		this.lore.add(0, description);
	}
	
	public abstract void onClick(InventoryClickEvent event);

	public ItemStack getItemStack() {
		ItemStack stack = new ItemStack(material);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
} 
