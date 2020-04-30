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
import fr.vexia.proxy.utils.TimeFormat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

public class SanctionCommand extends VexiaCommand {

    private final SanctionType sanctionType;
    private final StaffManager staffManager;

    public SanctionCommand(VexiaProxy proxy, SanctionType sanctionType) {
        super(proxy, sanctionType.getName().toLowerCase());
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
        if (args.length < (sanctionType == SanctionType.KICK ? 2 : 3)) {
            help(sender);
            return;
        }

        VexiaPlayer vexiaPlayer = PlayerManager.get(args[0]);
        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(args[0]);

        if (!exists(vexiaPlayer, sender)) return;
        if (!canApplySanction(vexiaPlayer, account, sender)) return;

        if (sanctionType == SanctionType.BAN && checkBan(vexiaPlayer, sender) ||
                sanctionType == SanctionType.MUTE && checkMute(vexiaPlayer, sender) ||
                sanctionType == SanctionType.KICK && checkKick(proxiedPlayer, sender))
            return;

        Date end = getEnd(args[1], sender);
        Date start = Calendar.getInstance().getTime();
        String reason = String.join(" ", Arrays.copyOfRange(args, sanctionType == SanctionType.KICK ? 1 : 2, args.length));
        TextBuilder sanctionComponent = buildSanctionMessage(sender, end, reason);

        VexiaSanction vexiaSanction = new VexiaSanction(vexiaPlayer.getUUID(), account.getUUID(), sanctionType, reason, start, end);
        SanctionManager.save(vexiaSanction);

        ProxyServer.getInstance().broadcast(String.format("§5§l[§fVexiaSanction§5§l] §d%s s'est fais %s par %s.",
                vexiaPlayer.getName(), sanctionType.getName().toLowerCase(), sender.getName()));

        if (proxiedPlayer != null && sanctionType != SanctionType.MUTE) {
            proxiedPlayer.disconnect(sanctionComponent.build());
        } else if (proxiedPlayer != null) {
            String sanctionText = String.format("§5§l[§fVexiaSanction§5§l] §dVous venez de vous faire mute ! \n§d" +
                            "Par: §7%s \n" +
                            "§dLe: §7%s \n" +
                            "§dFin le: §7%s \n" +
                            "§dRaison: §7%s",
                    sender.getName(), TimeFormat.formatDate(start.getTime()),
                    (end == null ? "Jamais" : TimeFormat.formatDate(end.getTime())), reason);

            proxiedPlayer.sendMessage(new TextBuilder(sanctionText).build());
        }

        staffManager.broadcast(sanctionComponent);
    }

    private boolean exists(VexiaPlayer vexiaPlayer, CommandSender sender) {
        if (vexiaPlayer == null) {
            sender.sendMessage(new TextBuilder("Erreur: Ce joueur n'existe pas !", ChatColor.RED).build());
            return false;
        }
        return true;
    }

    private boolean canApplySanction(VexiaPlayer vexiaPlayer, VexiaPlayer account, CommandSender sender) {
        if (vexiaPlayer.getRank().getId() > account.getRank().getId()) {
            sender.sendMessage(new TextBuilder("Erreur: Vous n'avez pas la permission d'appliquer une sanction sur ce joueur !", ChatColor.RED).build());
            return false;
        }
        return true;
    }

    private boolean checkBan(VexiaPlayer vexiaPlayer, CommandSender sender) {
        boolean ban = SanctionManager.getActiveSanction(vexiaPlayer.getUUID(), SanctionType.BAN) != null;
        if (ban) {
            sender.sendMessage(new TextBuilder("Erreur: Ce joueur est déjà banni !", ChatColor.RED).build());
            return true;
        }
        return false;
    }

    private boolean checkMute(VexiaPlayer vexiaPlayer, CommandSender sender) {
        boolean mute = SanctionManager.getActiveSanction(vexiaPlayer.getUUID(), SanctionType.MUTE) != null;
        if (mute) {
            sender.sendMessage(new TextBuilder("Erreur: Ce joueur est déjà mute !", ChatColor.RED).build());
            return true;
        }
        return false;
    }

    private boolean checkKick(ProxiedPlayer proxiedPlayer, CommandSender sender) {
        if (proxiedPlayer == null) {
            sender.sendMessage(new TextBuilder("§cErreur: Vous ne pouvez pas éjecté un joueur non connecté !").build());
            return true;
        }
        return false;
    }

    private Date getEnd(String time, CommandSender sender) {
        if (sanctionType != SanctionType.KICK && !time.equalsIgnoreCase("perm")) {
            long duration = TimeFormat.parsePeriod(time);
            if (duration == -1) {
                sender.sendMessage(new TextBuilder("Erreur: Durée invalide !", ChatColor.RED).build());
                return null;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MILLISECOND, (int) duration);
            return calendar.getTime();
        }
        return null;
    }

    private TextBuilder buildSanctionMessage(CommandSender sender, Date end, String reason) {
        return new TextBuilder("§5§l§m-----§f VexiaSanction §5§l§m-----\n\n" +
                "§dPar: §7" + sender.getName() + "\n" +
                "§dType: §7" + sanctionType.getName() + "\n" +
                (sanctionType != SanctionType.KICK ? "§dFin le: §7" + (end == null ? "Jamais" : TimeFormat.formatDate(end.getTime())) + "\n" : "") +
                "§dRaison: §7" + reason + "\n\n" +
                "§5§l§m---------------------------");
    }

    @Override
    protected void help(CommandSender sender) {
        sender.sendMessage(new TextBuilder("§cUtilisation: /" + getName() + " <joueur> " + (sanctionType == SanctionType.KICK ? "" : "<durée> ") + "<raison>").build());
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
