package fr.vexia.proxy.commands.player;

import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.commands.VexiaCommand;
import fr.vexia.proxy.utils.TextBuilder;
import net.md_5.bungee.api.CommandSender;

public class HelpCommand extends VexiaCommand {

    public HelpCommand(VexiaProxy proxy) {
        super(proxy, "help", null, "aide", "?");
    }

    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public Rank minimumGrade() {
        return Rank.JOUEUR;
    }

    @Override
    public void onCommand(CommandSender sender, VexiaPlayer account, String[] args) {
        sender.sendMessage(new TextBuilder("§6§m                        [§e§l AIDE §6§m]                        ").build());
        sender.sendMessage(new TextBuilder(" ").build());
        sender.sendMessage(new TextBuilder("    §e➥ /help : §6Afficher la liste des commandes.").build());
        sender.sendMessage(new TextBuilder("    §e➥ /lobby : §6Se connecter au lobby.").build());
        sender.sendMessage(new TextBuilder("    §e➥ /ping : §6Afficher son serveur ainsi que son ping.").build());
        sender.sendMessage(new TextBuilder("    §e➥ /friends : §6Affiche l'aide des amis.").build());
        sender.sendMessage(new TextBuilder(" ").build());
    }

    @Override
    protected void help(CommandSender sender) {

    }

    @Override
    public Iterable<String> onTabExecutor(CommandSender sender, VexiaPlayer account, String[] args) {
        return null;
    }

}
