package fr.vexia.core.staff.vanish;

import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.core.commands.VexiaCommand;
import fr.vexia.core.staff.StaffManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class VanishCommand extends VexiaCommand {

    private final StaffManager staffManager;

    public VanishCommand() {
        super("vanish");
        this.staffManager = StaffManager.get();
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
        Player player = (Player) sender;
        if (!staffManager.isMod(player)) {
            player.sendMessage("§7[§fModération§7] §cVous devez activer le mode modérateur");
            return;
        }
        if (staffManager.isVanish(player)) {
            staffManager.unVanish(player);
            return;
        }

        staffManager.vanish(player);
    }

    @Override
    protected void help(CommandSender sender, VexiaPlayer account) {

    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, VexiaPlayer account, String[] args) {
        return null;
    }
}
