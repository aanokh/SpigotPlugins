package online.allcraft.customTab;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;

public class TabUpdater extends BukkitRunnable {
	public final CustomTab plugin;
	public PacketPlayOutPlayerListHeaderFooter packet;
	public int headerIndex;
	public int footerIndex;
	
	public TabUpdater(CustomTab plugin, PacketPlayOutPlayerListHeaderFooter packet) {
		this.plugin = plugin;
		this.packet = packet;
		headerIndex = 0;
		footerIndex = 0;
	}
	
	@Override
    public void run() {
        try {
            Field a = packet.getClass().getDeclaredField("a");
            a.setAccessible(true);
            Field b = packet.getClass().getDeclaredField("b");
            b.setAccessible(true);
            
            a.set(packet, plugin.headers.get(headerIndex));
            if (headerIndex == plugin.headers.size() - 1) {
            	headerIndex = 0;
            } else {
            	headerIndex++;
            }
            
            b.set(packet, plugin.footers.get(footerIndex));
            if (footerIndex == plugin.footers.size() - 1) {
            	footerIndex = 0;
            } else {
            	footerIndex++;
            }
            
            
            if (Bukkit.getOnlinePlayers().size() == 0) return;
            for (Player player : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
