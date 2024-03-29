package fr.vexia.proxy.commands.moderation;

import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.commands.VexiaCommand;
import fr.vexia.proxy.utils.TextBuilder;
import net.md_5.bungee.api.CommandSender;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class StaffCommand extends VexiaCommand {

    private VexiaProxy proxy;

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
        if (args.length == 0) {
            ArrayList<UUID> staff = proxy.getStaffManager().getStaff();
            String staffList = staff.stream().map(uuid -> {
                VexiaPlayer player = PlayerManager.get(uuid);
                return player.getRank().getColor() + player.getName();
            }).collect(Collectors.joining(", "));
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
