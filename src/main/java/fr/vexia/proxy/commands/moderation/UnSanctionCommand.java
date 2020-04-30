package fr.vexia.proxy.commands.moderation;

import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.data.manager.SanctionManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.api.sanctions.VexiaSanction;
import fr.vexia.api.sanctions.SanctionType;
import fr.vexia.proxy.VexiaProxy;
import fr.vexia.proxy.commands.VexiaCommand;
import fr.vexia.proxy.staff.StaffManager;
import fr.vexia.proxy.utils.TabComplete;
import fr.vexia.proxy.utils.TextBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UnSanctionCommand extends VexiaCommand {

    private final SanctionType sanctionType;
    private final StaffManager staffManager;

    public UnSanctionCommand(VexiaProxy proxy, SanctionType sanctionType) {
        super(proxy, "un" + sanctionType.getName().toLowerCase());
        this.sanctionType = sanctionType;
        this.staffManager = proxy.getStaffManager();
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
            help(sender);
            return;
        }

        VexiaPlayer vexiaPlayer = PlayerManager.get(args[0]);
        if (!exists(vexiaPlayer, sender)) return;

        VexiaSanction sanction = sanctionType ==
                SanctionType.BAN ? checkBan(vexiaPlayer, sender) : checkMute(vexiaPlayer, sender);
        if (sanction == null) return;

        sanction.setEnd(Calendar.getInstance().getTime());

        SanctionManager.save(sanction);
        staffManager.broadcast(buildSanctionMessage(sender, vexiaPlayer));
    }

    private boolean exists(VexiaPlayer vexiaPlayer, CommandSender sender) {
        if (vexiaPlayer == null) {
            sender.sendMessage(new TextBuilder("Erreur: Ce joueur n'existe pas !", ChatColor.RED).build());
            return false;
        }
        return true;
    }

    private VexiaSanction checkBan(VexiaPlayer vexiaPlayer, CommandSender sender) {
        VexiaSanction ban = SanctionManager.getActiveSanction(vexiaPlayer.getUUID(), SanctionType.BAN);
        if (ban == null) {
            sender.sendMessage(new TextBuilder("Ce joueur n'est pas banni !", ChatColor.RED).build());
            return null;
        }
        return ban;
    }

    private VexiaSanction checkMute(VexiaPlayer vexiaPlayer, CommandSender sender) {
        VexiaSanction mute = SanctionManager.getActiveSanction(vexiaPlayer.getUUID(), SanctionType.BAN);
        if (mute == null) {
            sender.sendMessage(new TextBuilder("Ce joueur n'est pas muet !", ChatColor.RED).build());
            return null;
        }
        return mute;
    }

    private TextBuilder buildSanctionMessage(CommandSender sender, VexiaPlayer vexiaPlayer) {
        return new TextBuilder("§5§l§m-----§f VexiaSanction §5§l§m-----\n\n" +
                "§dJoueur: §7" + vexiaPlayer.getName() + "\n" +
                "§dPar: §7" + sender.getName() + "\n" +
                "§dType: §7Un" + sanctionType.getName().toLowerCase() + "\n\n" +
                "§5§l§m---------------------------");
    }

    @Override
    protected void help(CommandSender sender) {
        sender.sendMessage(new TextBuilder("§cUtilisation: /" + getName() + " <joueur> ").build());
    }

    @Override
    public Iterable<String> onTabExecutor(CommandSender sender, VexiaPlayer account, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            for (ProxiedPlayer players : ((ProxiedPlayer) sender).getServer().getInfo().getPlayers()) {
                list.add(players.getName());
            }
            int i = args.length - 1;
            return new TabComplete(args[i], list).getReponse();
        }
        return new ArrayList<>();
    }
}