package fr.vexia.proxy.commands;

import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.utils.TextBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

public abstract class VexiaCommand extends Command implements TabExecutor {

    private VexiaProxy proxy;

    public VexiaCommand(VexiaProxy proxy, String name) {
        super(name);
        this.proxy = proxy;
    }

    public VexiaCommand(VexiaProxy proxy, String name, String permission, String... aliases) {
        super(name, permission, aliases);
        this.proxy = proxy;
    }

    public void register() {
        proxy.getProxy().getPluginManager().registerCommand(proxy, this);
    }

    public abstract boolean onlyPlayer();

    public abstract Rank minimumGrade();

    public abstract void onCommand(CommandSender sender, VexiaPlayer account, String[] args);

    protected abstract void help(CommandSender sender);

    public abstract Iterable<String> onTabExecutor(CommandSender sender, VexiaPlayer account, String[] args);

    @Override
    public void execute(CommandSender sender, String[] args) {
        VexiaPlayer vexiaPlayer = null;
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            vexiaPlayer = PlayerManager.get(player.getUniqueId());
            if (vexiaPlayer.getRank().getId() < minimumGrade().getId()) {
                sender.sendMessage(new TextBuilder("Vous n'avez pas la permission.", ChatColor.RED).build());
                return;
            }
        } else if (onlyPlayer()) {
            sender.sendMessage(new TextBuilder("Vous devez être un joueur pour faire cette commande.", ChatColor.RED).build());
            return;
        }
        onCommand(sender, vexiaPlayer, args);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        VexiaPlayer vexiaPlayer = null;
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            vexiaPlayer = PlayerManager.get(player.getUniqueId());
            if (vexiaPlayer.getRank().getId() < minimumGrade().getId()) {
                return new ArrayList<>();
            }
        } else if (onlyPlayer()) {
            return new ArrayList<>();
        }
        Iterable<String> returnValue = onTabExecutor(sender, vexiaPlayer, args);
        return (returnValue != null) ? returnValue : new ArrayList<>();
    }
}
