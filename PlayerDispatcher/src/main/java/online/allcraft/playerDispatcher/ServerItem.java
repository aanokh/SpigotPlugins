package online.allcraft.playerDispatcher;


import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import online.allcraft.guiCore.GuiCore;
import online.allcraft.guiCore.GuiItem;

public class ServerItem extends GuiItem {
	
	public ServerType serverType;
	
	public ServerItem(GuiCore plugin, String name, ArrayList<String> lore, Material material, int index, ServerType serverType) {
		super(plugin, name, lore, material, index);
		this.serverType = serverType;
	}

	public ServerItem(GuiCore plugin, String name, String description, ArrayList<String> lore, Material material, int index, ServerType serverType) {
		super(plugin, name, description, lore, material, index);
		this.serverType = serverType;
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		/*Player player = (Player) event.getWhoClicked();
				 ByteArrayDataOutput out = ByteStreams.newDataOutput();
				 out.writeUTF("Connect");
				 out.writeUTF(server);    
				 //applies to the player you send it to aka Kick To Server.
				 player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
				 
				 player.closeInventory();
		*/
		
		Player player = (Player) event.getWhoClicked();
		serverType.playerQueue.add(player);
		player.sendMessage(ChatColor.GOLD + "Added to queue");
	}
	
	
}
