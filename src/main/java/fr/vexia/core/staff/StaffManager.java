package fr.vexia.core.staff;

import fr.vexia.core.VexiaCore;
import fr.vexia.core.items.ItemBuilder;
import fr.vexia.core.staff.cps.CPSPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.awt.event.WindowStateListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StaffManager {
    /*		MOD FIELDS		*/
    public static final ItemStack BOOSTER_ITEM = new ItemBuilder(Material.COMPASS)
            .setGlow(true)
            .setName("§6Booster")
            .toItemStack();
    public static final ItemStack FREEZE_ITEM = new ItemBuilder(Material.PACKED_ICE)
            .setGlow(true)
            .setName("§6Freeze")
            .toItemStack();
    public static final ItemStack KNOCKBACK_ITEM = new ItemBuilder(Material.STICK)
            .addEnchant(Enchantment.KNOCKBACK, 3)
            .setName("§aBâton knockback")
            .toItemStack();
    public static final ItemStack VERIF_ITEM = new ItemBuilder(Material.BOOK)
            .setGlow(true)
            .setName("§6Verif")
            .toItemStack();
    public static final ItemStack RANDOM_TP_ITEM = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)
            .setGlow(true)
            .setName("§6Téléportation aléatoire")
            .toItemStack();
    public static final ItemStack VANISH_ITEM = new ItemBuilder(Material.NETHER_STAR, 1)
            .setGlow(true)
            .setName("§6Vanish")
            .toItemStack();

    private final HashMap<UUID, ItemStack[]> mod;

    /*		FREEZE FIELDS		*/
    private final Set<UUID> frozens;
    private final Set<UUID> vanishs;
    private final HashMap<UUID, CPSPlayer> cpsPlayers;

    private static StaffManager INSTANCE;

    public static StaffManager get() {
        return INSTANCE == null ? new StaffManager() : INSTANCE;
    }

    public StaffManager() {
        INSTANCE = this;
        this.mod = new HashMap<>();
        this.frozens = new HashSet<>();
        this.cpsPlayers = new HashMap<>();
        this.vanishs = new HashSet<>();
    }


    /*		MOD METHODS		*/
    public boolean isMod(Player player) {
        return mod.containsKey(player.getUniqueId());
    }

    public void activateMod(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();
        mod.put(player.getUniqueId(), contents);

        player.getInventory().clear();
        player.setGameMode(GameMode.CREATIVE);
        player.setHealth(20);
        inventory.addItem(BOOSTER_ITEM, FREEZE_ITEM, KNOCKBACK_ITEM, VERIF_ITEM, RANDOM_TP_ITEM, VANISH_ITEM);
        player.sendMessage("§e[§9StaffMode§e] §dMode modération activé");
    }

    public void disableMod(Player player) {
        ItemStack[] contents = mod.remove(player.getUniqueId());
        player.getInventory().setContents(contents);
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.sendMessage("§e[§9StaffMode§e] §dMode modération désactivé");

        player.setAllowFlight(Bukkit.getMotd().contains("hub"));
    }


    /*		FREEZE METHODS		*/
    public boolean isFreeze(Player player) {
        return this.frozens.contains(player.getUniqueId());
    }

    public void freeze(Player moderator, Player target) {
        this.frozens.add(target.getUniqueId());

        Bukkit.broadcastMessage("§7[§fFreeze§7] §6" + moderator.getName() + " vient de freeze " + target.getName());
        target.sendTitle("§4Tu viens de te faire freeze", "§cRegarde ton chat !", 20, 200, 20);
        target.sendMessage("§c§lTu viens de te faire freeze par un modérateur !");
        target.sendMessage("§c§lRends-toi sur le discord : https://discord.vexia.fr/");
        target.sendMessage("§c§lSi tu te déconnectes, tu seras bannis !");
    }

    public void unFreeze(Player moderator, Player player) {
        Bukkit.broadcastMessage("§7[§fFreeze§7] §F§6" + moderator.getName() + " §Ea libéré §6" + player.getName());
        this.frozens.remove(player.getUniqueId());
    }

    public void freezeLogout(Player player) {
        if (!this.frozens.remove(player.getUniqueId())) {
            return;
        }

        Bukkit.broadcastMessage("§7[§fFreeze§7] §6" + player.getName() + " §es'est déconnecté en étant freeze");
    }

    /*		VANISH METHODS		*/
    public boolean isVanish(Player player) {
        return this.vanishs.contains(player.getUniqueId());
    }

    public void vanish(Player player) {
        this.vanishs.add(player.getUniqueId());
        player.sendMessage("§7[§fModération§7] §6Vanish : §a✔");
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (!isVanish(players)) {
                players.hidePlayer(VexiaCore.get(), player);
            } else {
                player.showPlayer(VexiaCore.get(), players);
            }
        }
    }

    public void unVanish(Player player) {
        this.vanishs.remove(player.getUniqueId());
        player.sendMessage("§7[§fModération§7] §6Vanish : §a✘");
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (!isVanish(players)) {
                players.showPlayer(VexiaCore.get(), player);
            } else {
                player.hidePlayer(VexiaCore.get(), players);
            }
        }
    }

    /*    CPS METHODS       */
    public CPSPlayer getCPSPlayer(Player player) {
        return this.cpsPlayers.containsKey(player.getUniqueId()) ?
                this.cpsPlayers.get(player.getUniqueId())
                :
                this.cpsPlayers.put(player.getUniqueId(), new CPSPlayer());
    }
    public HashMap<UUID, CPSPlayer> getCPSPlayers() {
        return this.cpsPlayers;
    }

    public void removeCPSPlayer(Player player) {
        this.cpsPlayers.remove(player.getUniqueId());
    }

}
