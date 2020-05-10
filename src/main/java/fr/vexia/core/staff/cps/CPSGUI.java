package fr.vexia.core.staff.cps;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.core.staff.StaffManager;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CPSGUI implements InventoryProvider {

    private static final ClickableItem ORANGE_GLASS = cancelItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setDyeColor(DyeColor.ORANGE).setName(" ").toItemStack());
    private static final ClickableItem RED_GLASS = cancelItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setDyeColor(DyeColor.RED).setName(" ").toItemStack());

    private final Player target;

    public CPSGUI(Player target) {
        this.target = target;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        VexiaPlayer vexiaPlayer = PlayerManager.get(target.getUniqueId());

        contents.set(0, 4, cancelItem(new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)
                .setSkullOwner(target.getName())
                .setName("§6" + target.getName())
                .addLoreLine("§6Grade: " + vexiaPlayer.getRank().getColoredPrefix())
                .toItemStack()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

        contents.set(0, 5, cancelItem(new ItemBuilder(Material.REDSTONE_BLOCK)
                .setName("§6Vie: §7" + target.getHealth() + "§8/§7" + target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
                .addLoreLine("§6Faim: §7" + target.getFoodLevel() + "§8/§720.0")
                .toItemStack()));

        CPSPlayer cpsPlayer = StaffManager.get().getCPSPlayer(target);
        System.out.println("CPSPlayer: "+cpsPlayer);
        System.out.println("LeftClicks: "+cpsPlayer.getLeftClicks());
        contents.set(0, 3, cancelItem(new ItemBuilder(Material.GOLD_BLOCK, convertToGui(cpsPlayer.getLeftClicks()))
                .setName("§6CPS Gauche: §e" + cpsPlayer.getLeftClicks())
                .addLoreLine("§6Max CPS G.: §e" + cpsPlayer.getMaxLeft())
                .addLoreLine("§6Alerte CPS G.: §e" + cpsPlayer.getAlertLeft())
                .toItemStack()));
        contents.set(0, 6, cancelItem(new ItemBuilder(Material.DIAMOND_BLOCK, convertToGui(cpsPlayer.getRightClicks()))
                .setName("§6CPS Droit: §e"+cpsPlayer.getRightClicks())
                .addLoreLine("§6Max CPS D.: §e"+cpsPlayer.getMaxRight())
                .addLoreLine("§6Alerte CPS D.: §e"+cpsPlayer.getAlertRight())
                .toItemStack()));

        int ping = ((CraftPlayer) target).getHandle().ping;
        contents.set(0, 4, cancelItem(new ItemBuilder(Material.EMERALD_BLOCK, convertToGui(ping))
                .setName("§6Ping: §e"+ping)
                .toItemStack()));
    }

    private Integer convertToGui(int value) {
        if (value < 1)
            return 1;
        return Math.min(value, 64);
    }


    private static ClickableItem cancelItem(ItemStack itemStack) {
        return ClickableItem.of(itemStack, event -> event.setCancelled(true));
    }

}
