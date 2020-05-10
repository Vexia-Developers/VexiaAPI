package fr.vexia.core.staff.cps;

import fr.minuskube.inv.SmartInventory;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.core.VexiaCore;
import fr.vexia.core.commands.VexiaCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class VerifCommand extends VexiaCommand {

    public VerifCommand() {
        super("verif");
    }

    @Override
    public boolean onlyPlayer() {
        return false;
    }

    @Override
    public Rank minimumRank() {
        return Rank.MODERATEUR;
    }

    @Override
    protected void onCommand(CommandSender sender, VexiaPlayer account, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("§cUtilisation: /verif <joueur>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cErreur: Ce joueur n'existe pas");
            return;
        }

        SmartInventory.builder()
                .id("cps_" + player.getName() + "_" + target.getName())
                .provider(new CPSGUI(target))
                .manager(VexiaCore.get().getInventoryManager())
                .size(6, 9)
                .title(ChatColor.GOLD + "Verif » " + target.getName())
                .build()
                .open(player);
    }

    @Override
    protected void help(CommandSender sender, VexiaPlayer account) {

    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, VexiaPlayer account, String[] args) {
        return null;
    }
}
