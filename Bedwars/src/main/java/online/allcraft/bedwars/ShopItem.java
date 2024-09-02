package online.allcraft.bedwars;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import online.allcraft.guiCore.GuiCore;
import online.allcraft.guiCore.GuiItem;

public class ShopItem extends GuiItem {
	
	public ArrayList<ItemStack> product;
	public ArrayList<ItemStack> cost;
	
	public ShopItem(GuiCore plugin, String name, ArrayList<String> lore, Material material, int index, ArrayList<ItemStack> cost, ArrayList<ItemStack> product) {
		super(plugin, name, lore, material, index);
		this.cost = cost;
		this.product = product;	
	}
	
	public ShopItem(GuiCore plugin, String name, String description, ArrayList<String> lore, Material material, int index, ArrayList<ItemStack> cost, ArrayList<ItemStack> product) {
		super(plugin, name, description, lore, material, index);
		this.cost = cost;
		this.product = product;
	}
	
	@Override
	public void onClick(InventoryClickEvent event) {
		HumanEntity whoClicked = event.getWhoClicked();
		
		if (!(whoClicked instanceof Player)) {
			return;
		}
		
		Player player = (Player) whoClicked;
		Inventory playerInventory = player.getInventory();
		for (ItemStack costStack : cost) {
			if (playerInventory.containsAtLeast(new ItemStack(costStack.getType()), 10)) {
				playerInventory.removeItem(costStack);
			} else {
				return;
			}
		}
		
		for (ItemStack productStack : product) {
			playerInventory.addItem(productStack);
		}
	}
	
	public ItemStack getItemStack() {
		ItemStack stack = new ItemStack(material);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
} 
