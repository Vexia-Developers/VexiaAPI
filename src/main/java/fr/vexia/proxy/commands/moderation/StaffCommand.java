package fr.vexia.proxy.commands.moderation;

import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.commands.VexiaCommand;
import fr.vexia.proxy.utils.TextBuilder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StaffCommand extends VexiaCommand {

    public StaffCommand(VexiaProxy proxy) {
        super(proxy, "staff");
    }

    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public Rank minimumGrade() {
        return Rank.MODERATEUR;
    }

    @Override
    public void onCommand(CommandSender sender, VexiaPlayer account, String[] args) {

        List<VexiaPlayer> staff = new ArrayList<>();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            VexiaPlayer vexiaPlayer = PlayerManager.get(player.getUniqueId());
            if (vexiaPlayer == null || vexiaPlayer.getRank().getId() < Rank.STAFF.getId()) continue;
            staff.add(vexiaPlayer);
        }

        if (args.length == 0) {
            String staffList = staff.stream().map(player -> player.getRank().getColor() + player.getName()).collect(Collectors.joining(", "));
            sender.sendMessage(new TextBuilder("§9Staff (" + staff.size() + ") : " + staffList).build());
            return;
        }

        String message = String.join(" ", args);
        sender.sendMessage(new TextBuilder("§9[Staff] " + account.getRank().getColor() + account.getName() + " §f> " + message).build());
    }

    @Override
    protected void help(CommandSender sender) {

    }

    @Override
    public Iterable<String> onTabExecutor(CommandSender sender, VexiaPlayer account, String[] args) {
        return null;
    }
}
