package fr.vexia.core.commands;

import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class VexiaCommand extends Command {

    public VexiaCommand(String name) {
        super(name);
    }

    public abstract boolean onlyPlayer();

    public abstract Rank minimumRank();

    protected abstract void onCommand(CommandSender sender, VexiaPlayer account, String[] args);

    protected abstract void help(CommandSender sender, VexiaPlayer account);

    protected abstract List<String> onTabComplete(CommandSender sender, VexiaPlayer account, String[] args);

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        VexiaPlayer account = null;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            account = PlayerManager.get(player.getUniqueId());
            if (account.getRank().getId() < minimumRank().getId()) {
                sender.sendMessage("§cVous n'avez pas la permission.");
                return false;
            }
        } else if (onlyPlayer()) {
            sender.sendMessage("§cVous devez être un joueur pour faire cette commande.");
            return false;
        }
        onCommand(sender, account, args);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        VexiaPlayer vexiaPlayer = null;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            vexiaPlayer = PlayerManager.get(player.getUniqueId());
            if (vexiaPlayer.getRank().getId() < minimumRank().getId()) {
                return new ArrayList<>();
            }
        } else if (onlyPlayer()) {
            return new ArrayList<>();
        }
        return onTabComplete(sender, vexiaPlayer, args);
    }

    public void register() throws NoSuchFieldException, IllegalAccessException {
        final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        bukkitCommandMap.setAccessible(true);
        CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        commandMap.register("vexia", this);
    }
}