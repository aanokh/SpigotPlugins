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

public class ServerItemWIP extends GuiItem {
	//TODO WIP - working to incorporate with GuiCore
	
	public String server;
	
	public ServerItemWIP(GuiCore plugin, String name, ArrayList<String> lore, Material material, int index, String server) {
		super(plugin, name, lore, material, index);
		this.server = server;
		plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
	}

	public ServerItemWIP(GuiCore plugin, String name, String description, ArrayList<String> lore, Material material, int index, String server) {
		super(plugin, name, description, lore, material, index);
		this.server = server;
		plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
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
		
		plugin.getServer().broadcastMessage(event.getWhoClicked().getName() + " went to " + server);
	}
	
	
}
