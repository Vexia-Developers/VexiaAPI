package fr.vexia.core.staff.mod;

import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.core.commands.VexiaCommand;
import fr.vexia.core.staff.StaffManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ModCommand extends VexiaCommand {

    private final StaffManager staffManager;
    public ModCommand() {
        super("mod");
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
        Player player = (Player)sender;
        if(staffManager.isMod(player)) {
            staffManager.disableMod(player);
        } else {
            staffManager.activateMod(player);
        }
    }

    @Override
    protected void help(CommandSender sender, VexiaPlayer account) {

    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, VexiaPlayer account, String[] args) {
        return null;
    }
}
