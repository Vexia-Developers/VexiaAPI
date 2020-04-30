package fr.vexia.proxy.commands.admin;

import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.commands.VexiaCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class RestartCommand extends VexiaCommand {

    public RestartCommand(VexiaProxy proxy) {
        super(proxy, "restart");
    }

    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public Rank minimumGrade() {
        return Rank.ADMINISTRATEUR;
    }

    @Override
    public void onCommand(CommandSender sender, VexiaPlayer account, String[] args) {
        ProxyServer.getInstance().stop();
    }

    @Override
    protected void help(CommandSender sender) {

    }

    @Override
    public Iterable<String> onTabExecutor(CommandSender sender, VexiaPlayer account, String[] args) {
        return null;
    }
}
