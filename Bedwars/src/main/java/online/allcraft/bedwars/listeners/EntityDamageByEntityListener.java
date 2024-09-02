package online.allcraft.bedwars.listeners;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand.EnumClientCommand;
import online.allcraft.bedwars.Bedwars;

public class EntityDamageByEntityListener implements Listener {

	private final Bedwars plugin;

	public EntityDamageByEntityListener(Bedwars plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Player player;
		
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		
		player = (Player) event.getEntity();
		
		if (!(player.getHealth() < 1)) {
			return;
		}
		
		if (plugin.playerTeams.containsKey(player)) {
			if (plugin.playerTeams.get(player).bed != null) {

				// AUTO RESPAWN
		        PacketPlayInClientCommand in = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
		        EntityPlayer craftPlayer = ((CraftPlayer) player).getHandle();
		        craftPlayer.playerConnection.a(in);
		        
		        player.teleport(plugin.playerTeams.get(player).spawn);
			}
		}
	}
}
