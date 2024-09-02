package online.allcraft.guiCore;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class InventoryOpenItem extends GuiItem {
	
	public InventoryGui targetInventory;

	public InventoryOpenItem(GuiCore plugin, String name, ArrayList<String> lore, Material material, int index, InventoryGui targetInventory) {
		super(plugin, name, lore, material, index);
		this.targetInventory = targetInventory;
	}

	public InventoryOpenItem(GuiCore plugin, String name, String description, ArrayList<String> lore, Material material, int index, InventoryGui targetInventory) {
		super(plugin, name, description, lore, material, index);
		this.targetInventory = targetInventory;
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		event.getWhoClicked().openInventory(targetInventory.inventory);
	}
	
	
}
