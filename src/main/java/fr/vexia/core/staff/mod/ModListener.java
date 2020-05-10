package fr.vexia.core.staff.mod;

import fr.vexia.core.staff.StaffManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ModListener implements Listener {


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (StaffManager.get().isMod(player)) {
            StaffManager.get().disableMod(player);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (StaffManager.get().isMod(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (StaffManager.get().isMod(player)) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (StaffManager.get().isMod(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (StaffManager.get().isMod(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (StaffManager.get().isMod(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (StaffManager.get().isMod(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHitPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (StaffManager.get().isMod(player)) {
                event.setDamage(0.0D);
            }
        }
    }

	@EventHandler
	public void onPlayerInteractAnotherPlayer(PlayerInteractEntityEvent event) {
		if (!(event.getRightClicked() instanceof Player)) {
			return;
		}

		Player rightclick = (Player) event.getRightClicked();
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();

		if (!event.getHand().equals(EquipmentSlot.HAND) || !StaffManager.get().isMod(player)) {
			return;
		}

		if (item.equals(StaffManager.VERIF_ITEM)) {
			player.performCommand("verif " + rightclick.getName());
		} else if (item.equals(StaffManager.FREEZE_ITEM)) {
			player.performCommand("freeze " + rightclick.getName());
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!event.hasItem() || event.getItem() == null || !event.getItem().hasItemMeta() || event.getItem().getType() == Material.AIR) {
			return;
		}

		Player player = event.getPlayer();
		if (!StaffManager.get().isMod(player)) {
			return;
		}

		event.setCancelled(true);
		if (event.getItem().equals(StaffManager.VANISH_ITEM)) {
			player.performCommand("vanish");
		}
	}

}
