package online.allcraft.guiCore;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryGui {

	public ArrayList<GuiItem> items;
	public Inventory inventory;
	public String name;
	public Material material;
	public boolean giveOnJoin;
	GuiCore plugin;

	public InventoryGui (GuiCore plugin, String name, Material material, boolean giveOnJoin) {
		this.plugin = plugin;
		this.name = name;
		this.material = material;
		this.giveOnJoin = giveOnJoin;
		registerInventory();
	}
	
	public InventoryGui (GuiCore plugin, String name, Material material, boolean giveOnJoin, ArrayList<GuiItem> items) {
		this.plugin = plugin;
		this.name = name;
		this.material = material;
		this.giveOnJoin = giveOnJoin;
		this.items = new ArrayList<GuiItem>(items);
		
		registerInventory();
	}

	public void registerInventory() {
		inventory = Bukkit.createInventory(null, 27, name);
		for (GuiItem item : items) {
			inventory.setItem(item.index, item.getItemStack());
		}
	}

	public ItemStack getItemStack() {
		ItemStack stack = new ItemStack(material);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		return stack;
	}

	public void updateItem(GuiItem item) {
		inventory.setItem(item.index, item.getItemStack());
		items.add(item);
	}
	
	public void setItems(ArrayList<GuiItem> items) {
		this.items = new ArrayList<GuiItem>(items);
	}
}
