package fr.vexia.proxy.listeners;

import fr.vexia.api.data.manager.PlayerManager;
import fr.vexia.api.data.manager.SanctionManager;
import fr.vexia.api.players.VexiaPlayer;
import fr.vexia.api.players.rank.Rank;
import fr.vexia.api.sanctions.VexiaSanction;
import fr.vexia.api.sanctions.SanctionType;
import fr.vexia.proxy.utils.PrefixUtils;
import fr.vexia.proxy.utils.TextBuilder;
import fr.vexia.proxy.utils.TimeFormat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyChatListener implements Listener {

    private static final Pattern URL_PATTERN = Pattern.compile("((http://|https://)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(/([a-zA-Z-_/.0-9#:?=&;,]*)?)?)");
    private static final List<String> COMMANDS = Arrays.asList("/plugins", "/ver", "/pl", "/version", "/about", "/bungee");

    @EventHandler
    public void onChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        VexiaPlayer vexiaPlayer = PlayerManager.get(player.getUniqueId());
        if (vexiaPlayer == null || vexiaPlayer.getRank().getId() >= Rank.ADMINISTRATEUR.getId()) return;

        VexiaSanction mute = SanctionManager.getActiveSanction(vexiaPlayer.getUUID(), SanctionType.MUTE);
        if (mute != null) {
            VexiaPlayer moderator = PlayerManager.get(mute.getModerator());

            String sanctionText = String.format("§5§l[§fVexiaSanction§5§l] §dVous venez de vous faire mute ! \n" +
                            "§dPar: §7%s \n" +
                            "§dLe: §7%s \n" +
                            "§dFin le: §7%s \n" +
                            "§dRaison: §7%s",
                    moderator.getName(), TimeFormat.formatDate(mute.getStart().getTime()),
                    (mute.getEnd() == null ? "Jamais" : TimeFormat.formatDate(mute.getEnd().getTime())), mute.getReason());

            player.sendMessage(sanctionText);
            event.setCancelled(true);
            return;
        }

        String message = event.getMessage();
        Matcher matcher = URL_PATTERN.matcher(message);
        if (matcher.find() && !matcher.group().contains("vexia.fr")) {
            player.sendMessage(new TextBuilder(PrefixUtils.CHAT + "§cLes liens sont interdits dans le chat !").build());
            event.setCancelled(true);
            return;
        }

        //NO BUKKIT COMMAND
        String[] strCut = event.getMessage().split(" ");
        if (event.isCommand() && (strCut[0].contains(":"))) {
            event.setCancelled(true);
            player.sendMessage(new TextBuilder("Commande inconnue. Faire /help pour afficher l'aide", ChatColor.RED).build());
        } else if (event.isCommand()) {
            for (String list : COMMANDS) {
                if (event.getMessage().toLowerCase().startsWith(list)) {
                    event.setCancelled(true);
                    player.sendMessage(new TextBuilder("Commande inconnue. Faire /help pour afficher l'aide", ChatColor.RED).build());
                    break;
                }
            }
        }
    }
}
