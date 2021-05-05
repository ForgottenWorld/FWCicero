package it.forgottenworld.fwcicero.object.managers;

import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwcicero.FWCicero;
import it.forgottenworld.fwcicero.config.ConfigManager;
import it.forgottenworld.fwcicero.listeners.NewPlayerListener;
import it.forgottenworld.fwcicero.object.CiceroLocation;
import it.forgottenworld.fwcicero.utility.ChatFormatter;
import it.forgottenworld.fwcicero.utility.Messages;
import it.forgottenworld.fwcicero.utility.TownyUtil;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;

public class CiceroManager {

    //White-list citta' per controlli su /cicero
    public static ArrayList<String> listaCitta = new ArrayList<String>();
    // Map of player allowed to use Cicero
    public static HashMap<UUID, Long> playerInCicero = new HashMap<UUID, Long>();
    // Map of Towns viewed by a Player
    public static HashMap<UUID, CiceroLocation> lastTownViewedPlayer = new HashMap<>();
    // Server
    static Server server;

    public static boolean isPlayerInCicero(UUID uuid) {
        return playerInCicero.containsKey(uuid);
    }

    public static long getTimeleft(UUID uuid){
        if (isPlayerInCicero(uuid))
            return playerInCicero.get(uuid);
        else
            return 0;
    }

    public static void teleportAction(Town town, Player player){
        // Teleport verso quella città
        player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TELEPORT_TO + " " + ChatColor.GOLD + town.getName()));
        player.teleport(CiceroLocationManager.getCiceroLocationFromTown(town).getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        server = player.getServer();
        //TODO message
        server.broadcastMessage(ChatFormatter.formatWarningMessage(player.getName()) + ChatFormatter.formatSuccessMessageNoPrefix(" sta visitando ") + ChatFormatter.formatWarningMessageNoPrefix(town.getName()));

        save();
    }

    public static String convertMillisecond(Player player, long from, long to){
        long milliseconds = from - to;
        if(milliseconds<=0){
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.END_TIME));
            playerInCicero.remove(player.getUniqueId());
            //TODO message
            return "Tempo scaduto.";
        }
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)));
        //TODO message
        return "Hai a disposizione ancora " +
                ChatColor.WHITE + hours + ChatColor.GREEN + " ore " +
                ChatColor.WHITE + minutes + ChatColor.GREEN + " minuti " +
                ChatColor.WHITE + seconds + ChatColor.GREEN + " secondi.";
    }

    public static void save() {
        ConfigManager database = FWCicero.getInstance().getDatabase();
        database.getFile().set("players", null);

        for (Map.Entry<UUID, Long> pl : playerInCicero.entrySet()){
            String path = "players." + pl.getKey();
            database.getFile().set(path + ".time", pl.getValue());
        }

        for (Map.Entry<UUID, Long> pl : NewPlayerListener.invulnerability.entrySet()){
            String path = "players." + pl.getKey();
            database.getFile().set(path + ".invulnerability", NewPlayerListener.invulnerability.get(pl.getKey()));
        }

        for (Map.Entry<UUID, CiceroLocation> plcl : lastTownViewedPlayer.entrySet()){
            String path = "players." + plcl.getKey();
            database.getFile().set(path + ".town", plcl.getValue().getTown().getName());
        }

        database.saveFile();
    }

    public static void load() {
        ConfigManager database = FWCicero.getInstance().getDatabase();
        playerInCicero.clear();
        lastTownViewedPlayer.clear();

        if (database.getFile().getConfigurationSection("players") != null){
            for (String uuid : database.getFile().getConfigurationSection("players").getKeys(false)){
                playerInCicero.put(UUID.fromString(uuid), database.getFile().getLong("players." + uuid + ".time"));
                NewPlayerListener.invulnerability.put(UUID.fromString(uuid), database.getFile().getLong("players." + uuid + ".invulnerability"));
                if (database.getFile().getConfigurationSection("players." + uuid + ".town") != null) {
                    lastTownViewedPlayer.put(UUID.fromString(uuid), CiceroLocationManager.getCiceroLocationFromTown(TownyUtil.getTownFromString(database.getFile().getString("players." + uuid + ".town"))));
                }
            }
        }
    }
}
