package fr.vexia.core.player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.potion.PotionEffect;

public class PlayerUtils {

    public static void reset(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.setLevel(0);
        player.setExp(0);
        player.setFoodLevel(20);
        player.setHealth(20.0);
        player.getInventory().clear();
        player.getEnderChest().clear();
        for (PotionEffect potionsEffects : player.getActivePotionEffects()) {
            player.removePotionEffect(potionsEffects.getType());
        }
    }

    public static void teleportServer(Plugin plugin, Player player, String server) {
        Messenger messenger = Bukkit.getMessenger();
        if(!messenger.isOutgoingChannelRegistered(plugin, "BungeeCord")) {
            messenger.registerOutgoingPluginChannel(plugin, "BungeeCord");
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

}
