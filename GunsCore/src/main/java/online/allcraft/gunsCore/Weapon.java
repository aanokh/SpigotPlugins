package online.allcraft.gunsCore;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;

import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutSetSlot;
import net.minecraft.server.v1_12_R1.Slot;

public class Weapon {
	public GunsCore plugin;
	public Player player;
	public WeaponType weaponType;
	public int shootingDelay;
	public int reloadDelay;
	public int clip;
	public int ammo;
	
	public Weapon(WeaponType weaponType, Player player) {
			this.player = player;
			this.plugin = weaponType.plugin;
			this.weaponType = weaponType;
			
			shootingDelay = this.weaponType.maxShootingDelay;
			reloadDelay = this.weaponType.maxReloadDelay;
			clip = this.weaponType.clipSize;
			ammo = this.weaponType.maxAmmo; 
	}
	
	public void givePlayer(int amount) {
		ItemStack out = new ItemStack(weaponType.material, amount);
		ItemMeta meta = out.getItemMeta();
		meta.setDisplayName(weaponType.name);
		out.setItemMeta(meta);
		
		player.getInventory().addItem(out);
	}
	
	public void givePlayerOnStart() {
		
		ItemStack out = new ItemStack(weaponType.material, weaponType.clipSize);
		ItemMeta meta = out.getItemMeta();
		meta.setDisplayName(weaponType.name);
		out.setItemMeta(meta);
		
		player.getInventory().addItem(out);
		player.setLevel(weaponType.maxAmmo);
	}
	
	public void shoot(ItemStack item) {
		if (!plugin.shootingDelays.contains(this)) {
			if (clip > 0) {
				shootingDelay = weaponType.maxShootingDelay;
				plugin.shootingDelays.add(this);
				
				Snowball snowball = (player.launchProjectile(Snowball.class));
				
				for(Player p : Bukkit.getServer().getOnlinePlayers()) {
				    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(snowball.getEntityId());
				    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
				}
                                 VVV
				Vector vector = player.getLocation().getDirection().normalize().multiply(2);
				snowball.setVelocity(vector);
				snowball.setGravity(false);
				plugin.snowballToWeapon.put(snowball, this);
				
				player.getWorld().playSound(player.getLocation(), weaponType.shootSound, 1, 1);
				
				clip--;
				updateClip(clip);
				ammo--;
				updateAmmo(ammo);
			} else {
				shootingDelay = weaponType.maxShootingDelay;
				plugin.shootingDelays.add(this);

				player.getWorld().playSound(player.getLocation(), weaponType.shootFailSound, 1, 1);
			}
		}	
	}

	public void startReload() {
		if (!plugin.reloadDelays.contains(this)) {

			plugin.reloadDelays.add(this);
			reloadDelay = weaponType.maxReloadDelay;
			clip = 0;
		}
	}
	
	public void reload() {
		if (ammo >= weaponType.clipSize) {
			clip = weaponType.clipSize;
			updateClip(clip);
		} else if (ammo < weaponType.clipSize && ammo > 0) {
			clip = ammo;
			updateClip(clip);
		}
	}
	
	public void updateClip(int amount) {
		if (amount <= 0) {
			ItemStack stack = new ItemStack(weaponType.material);
			stack.addEnchantment(Enchantment.DURABILITY, 1);
			
			ItemMeta meta = stack.getItemMeta();
			meta.setDisplayName(weaponType.name + " (Reload)");
			stack.setItemMeta(meta);
			player.getInventory().remove(weaponType.material);
			player.getInventory().addItem(stack);
			player.updateInventory();
		} else {
			player.getInventory().remove(weaponType.material);
			
			givePlayer(amount);
			
			player.updateInventory();
		}
	}

	public void updateReload(int durability) {
		ItemStack stack = new ItemStack(weaponType.material);
		// Normally enchantment can't be added to stack of tools
		stack.addEnchantment(Enchantment.DURABILITY, 1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(weaponType.name + " (Reloading...)");
		stack.setItemMeta(meta);
		stack.setDurability((short) durability);
		//player.getInventory().remove(weaponType.material);
		//player.getInventory().addItem(stack);
		//player.updateInventory();
		
		//ProtocolManager protocolManager = plugin.protocolManager;
		//PacketContainer slotPacket = protocolManager.createPacket(PacketType.Play.Server.SET_SLOT);
		
		//slotPacket.getBytes().write(0, (byte) 0); // Window ID 0 - edit only hotbar
		//slotPacket.getShorts().write(0, (short) 9); // Slot number 37
		
		//try {
		//	protocolManager.sendServerPacket(player, slotPacket);
		//} catch (InvocationTargetException e)  {
		//	plugin.getServer().broadcastMessage("Error");
		//}
		
		CraftPlayer cp = (CraftPlayer) player;
		PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(0, 36, CraftItemStack.asNMSCopy(stack));
		cp.getHandle().playerConnection.sendPacket(packet);
	}
	
	public void updateAmmo(int amount) {
		player.setLevel(amount);
	}
}
