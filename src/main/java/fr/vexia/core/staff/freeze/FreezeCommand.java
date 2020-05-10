package fr.vexia.core.staff.freeze;

import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.core.commands.VexiaCommand;
import fr.vexia.core.staff.StaffManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FreezeCommand extends VexiaCommand {

    public FreezeCommand() {
        super("freeze");
    }

    @Override
    public boolean onlyPlayer() {
        return true;
    }

    @Override
    public Rank minimumRank() {
        return Rank.MODERATEUR;
    }

    @Override
    protected void onCommand(CommandSender sender, VexiaPlayer account, String[] args) {
        if (args.length == 0) {
            help(sender, account);
            return;
        }

        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cCe joueur n'est pas connecté");
            return;
        }

        if(StaffManager.get().isFreeze(target)) {
            StaffManager.get().unFreeze(player, target);
        } else {
            StaffManager.get().freeze(player, target);
        }
    }

    @Override
    protected void help(CommandSender sender, VexiaPlayer account) {
        // TODO Auto-generated method stub

    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, VexiaPlayer account, String[] args) {
        return null;
    }

}
