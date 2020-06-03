package it.forgottenworld.fwcicero.command;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.TownyUniverse;
import it.forgottenworld.fwcicero.FWCicero;
import it.forgottenworld.fwcicero.utility.ChatFormatter;
import it.forgottenworld.fwcicero.utility.FWLocation;
import it.forgottenworld.fwcicero.utility.Messages;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CiceroCommandExecutor implements CommandExecutor, Serializable {

    //White-list citta' per controlli su /cicero
    public static ArrayList<String> listaCitta = new ArrayList<String>();
    //White-List player
    public static HashMap<String,Long> whitelistP = new HashMap<String,Long>();
    //HashMap di controllo
    public static HashMap<String,String> visita = new HashMap<String,String>();
    //Citta - Location
    public static HashMap<String,String> locationC = new HashMap<String,String>();
    // Server
    Server server;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args){

        Player player = (Player)sender;

        switch (args.length){
            case 0:    // Comando /cicero
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Messages.NO_CONSOLE);
                    return true;
                }

                // Se sta già visitando le città
                if(visita.containsKey(player.toString())){
                    // Se il player non sta usando il comando nelle 48 ore di tempo concesso
                    if(System.currentTimeMillis() >= whitelistP.get(player.toString())){
                        player.sendMessage(ChatFormatter.formatErrorMessage(Messages.END_TIME));
                        whitelistP.remove(player.toString());
                        visita.remove(player.toString());
                        return true;
                    }
                    int index = listaCitta.indexOf(visita.get(player.toString()));
                    if(listaCitta.size()>index+1){
                        String city = listaCitta.get(index+1);
                        teleportAction(city, player);
                        player.sendMessage(ChatFormatter.formatSuccessMessage("Stai visitando " + ChatColor.GOLD + city));
                        visita.put(player.toString(), listaCitta.get(index+1));
                    }else{
                        player.sendMessage(ChatFormatter.formatWarningMessage(Messages.END_TOWN));
                        whitelistP.remove(player.toString());
                        visita.remove(player.toString());
                        //Location Spes
                        World w = sender.getServer().getWorld(FWCicero.instance.getConfig().getString("respawn.w"));
                        Location SPES = new Location(w, FWCicero.instance.getConfig().getInt("respawn.x"), FWCicero.instance.getConfig().getInt("respawn.y"), FWCicero.instance.getConfig().getInt("respawn.z"));
                        Player p = (Player)sender;
                        p.teleport(SPES, PlayerTeleportEvent.TeleportCause.PLUGIN);
                    }
                    // Se è la prima volta o non sta visitando
                }else{
                    // Se il player non è abilitato ad utilizzare il comando
                    if(!whitelistP.containsKey(player.toString())){
                        player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NOT_ALLOW));
                        return true;
                    }
                    // Se non ci sono città in whitelist
                    if(listaCitta.isEmpty()){
                        player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN));
                        return true;
                    }
                    visita.put(player.toString(), listaCitta.get(0));
                    teleportAction(listaCitta.get(0), player);
                    player.sendMessage(ChatFormatter.formatSuccessMessage("Stai visitando " + ChatColor.GOLD + listaCitta.get(0)));
                }
                return true;

            case 1:
                switch (args[0].toLowerCase()){
                    case "help":    // Comando /cicero help
                        player.sendMessage(ChatFormatter.helpMessage());
                        return true;

                    case "list":    // Comando /cicero list
                        if(!sender.hasPermission("forgotten.cicero.mod")){
                            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));
                            return true;
                        }

                        if(!listaCitta.isEmpty()){
                            player.sendMessage(ChatFormatter.chatHeader());
                            player.sendMessage(ChatColor.GRAY + "Lista delle citta':");
                            for(String s : listaCitta){
                                player.sendMessage(ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + s);
                            }
                        }else{
                            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN));
                            return true;
                        }
                        return true;

                    case "timeleft":
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(Messages.NO_CONSOLE);
                            return true;
                        }

                        if(whitelistP.containsKey(sender.toString())){
                            convertMillisecond(player,whitelistP.get(player.toString()),System.currentTimeMillis());
                        }else{
                            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NOT_ALLOW));
                        }
                        return true;

                    case "reload":

                        FWCicero.info("Saving infos...");
                        saveHash();
                        FWCicero.info("Data Saved");

                        FWCicero.info("Loading infos...");
                        loadHash();
                        FWCicero.info("Data loaded");

                        FWCicero.instance.reloadConfig();
                        FWCicero.info(Messages.CONFIG_RELOAD);
                        if(sender instanceof Player)
                            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.CONFIG_RELOAD));
                        return true;
                }

                break;

            case 2:
                switch (args[0].toLowerCase()){
                    case "add":   // Comando /cicero add <Town>
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(Messages.NO_CONSOLE);
                            return true;
                        }

                        if(!sender.hasPermission("forgotten.cicero.mod")){
                            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));
                            return true;
                        }

                        //Controllo se la città esiste
                        ArrayList<String> listaC = new ArrayList<String>();
                        for (Iterator<Town> i = TownyUniverse.getInstance().getDataSource().getTowns().iterator(); i.hasNext(); ) {
                            listaC.add(i.next().getName());
                        }
                        if (!listaC.contains(args[1])) {
                            sender.sendMessage(ChatFormatter.formatErrorMessage("La citta' " + args[1] + " e' inesistente!"));
                            return true;
                        }
                        if (listaCitta.contains(args[1])) {
                            sender.sendMessage(ChatFormatter.formatErrorMessage("La citta' " + args[1] + " e' gia' presente in whitelist."));
                            return true;
                        }

                        Town t = null;
                        try {
                            t = TownyUniverse.getInstance().getDataSource().getTown(args[1]);
                        } catch (NotRegisteredException e) {
                            e.printStackTrace();
                        }
                        if (!t.hasSpawn()){
                            sender.sendMessage(ChatFormatter.formatErrorMessage("La citta' non ha settato uno spawn-point."));
                            return true;
                        }
                        //Location loc = player.getLocation();

                        //Aggiunge la città alla whitelist
                        listaCitta.add(args[1]);
                        //locationC.put(args[1], new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()));
                        try {
                            locationC.put(args[1], FWLocation.getStringFromLocation(new Location(t.getSpawn().getWorld(), t.getSpawn().getX(), t.getSpawn().getY(), t.getSpawn().getZ())));
                        } catch (TownyException e) {
                            e.printStackTrace();
                        }
                        sender.sendMessage(ChatFormatter.formatSuccessMessage("La citta' " + args[1] + " e' stata aggiunta al cicero!"));

                        return true;

                    case "remove":   // Comando /cicero remove <Town>
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(Messages.NO_CONSOLE);
                            return true;
                        }

                        if(!sender.hasPermission("forgotten.cicero.mod")){
                            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));
                            return true;
                        }

                        if (!listaCitta.contains(args[1])) {
                            player.sendMessage(ChatFormatter.formatErrorMessage("La citta' " + args[1] + " non e' presente in whitelist."));
                            return true;
                        }
                        //Rimuove la citta' dalla whitelist
                        listaCitta.remove(args[1]);
                        locationC.remove(args[1]);
                        player.sendMessage(ChatFormatter.formatSuccessMessage("La citta' " + args[1] + " e' stata rimossa dal cicero."));
                        return true;

                    case "allow":   // Comando /cicero allow <Player>
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(Messages.NO_CONSOLE);
                            return true;
                        }

                        if(!sender.hasPermission("forgotten.cicero.mod")){
                            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));
                            return true;
                        }

                        if(player.getServer().getPlayer(args[1]) != null){
                            if(whitelistP.containsKey(player.getServer().getPlayer(args[1]).toString())){
                                player.sendMessage(ChatFormatter.formatErrorMessage("Il player " + args[1] + " e' gia' stato abilitato."));
                                return true;
                            }
                            //Se l'utente è nuovo
                            //Aggiungo il player alla whitelist, 48 ore per lanciare il comando
                            long timeLeft = System.currentTimeMillis() + FWCicero.getDefaultConfig().getInt("time");
                            whitelistP.put(player.getServer().getPlayer(args[1]).toString(), timeLeft);
                            player.sendMessage(ChatFormatter.formatSuccessMessage("Il player " + args[1] + " e' stato aggiungo alla whitelist del cicero."));
                        }else{
                            player.sendMessage(ChatFormatter.formatErrorMessage("Il player " + args[1] + " e' Offline o inesistente."));
                            return true;
                        }
                        return true;
                }
                break;

            case 3:
                switch (args[0].toLowerCase()){
                    case "add":   // Comando /cicero add <Town> [-f]
                        if(args[2].equalsIgnoreCase("-f")) {
                            if (!(sender instanceof Player)) {
                                sender.sendMessage(Messages.NO_CONSOLE);
                                return true;
                            }

                            if (!sender.hasPermission("forgotten.cicero.mod")) {
                                sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));
                                return true;
                            }

                            //Controllo se la città esiste
                            ArrayList<String> listaC = new ArrayList<String>();
                            for (Iterator<Town> i = TownyUniverse.getInstance().getDataSource().getTowns().iterator(); i.hasNext(); ) {
                                listaC.add(i.next().getName());
                            }
                            if (!listaC.contains(args[1])) {
                                sender.sendMessage(ChatFormatter.formatErrorMessage("La citta' " + args[1] + " e' inesistente!"));
                                return true;
                            }
                            if (listaCitta.contains(args[1])) {
                                sender.sendMessage(ChatFormatter.formatErrorMessage("La citta' " + args[1] + " e' gia' presente in whitelist."));
                                return true;
                            }

                            Location loc = player.getLocation();

                            //Aggiunge la città alla whitelist
                            listaCitta.add(args[1]);
                            locationC.put(args[1], FWLocation.getStringFromLocation(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ())));
                            sender.sendMessage(ChatFormatter.formatSuccessMessage("La citta' " + args[1] + " e' stata aggiunta al cicero!"));
                            return true;
                        }
                        return false;
                }
                break;
        }

        return false;
    }

    public void teleportAction(String city, Player player){
        // Teleport verso quella città
        player.sendMessage(ChatFormatter.formatSuccessMessage("Teleport verso " + ChatColor.GOLD + city + ChatColor.GREEN + " in corso"));
        player.teleport(FWLocation.getLocationFromString(locationC.get(city)), PlayerTeleportEvent.TeleportCause.PLUGIN);
        server = player.getServer();
        server.broadcastMessage(ChatFormatter.formatSuccessMessage(player.getName() + " sta visitando " + city));
    }

    public static void convertMillisecond(Player sender, long from, long to){
        long milliseconds = from - to;
        if(milliseconds<=0){
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.END_TIME));
            whitelistP.remove(sender.toString());
            return;
        }
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)));
        sender.sendMessage(ChatFormatter.formatSuccessMessage("Cicero disattivo tra " +
                        ChatColor.WHITE + hours + ChatColor.GREEN + " ore " +
                        ChatColor.WHITE + minutes + ChatColor.GREEN + " minuti " +
                        ChatColor.WHITE + seconds + ChatColor.GREEN + " secondi "));
    }

//------------------------------------------------------------------------------------------------------------------------

    //Salva le HashMap su file
    public static void saveHash(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("plugins/FWCicero/whiteCitta.markus"));
            FWCicero.info(listaCitta.toString());
            oos.writeObject(listaCitta);
            oos.close();
            ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream("plugins/FWCicero/whitePlayer.markus"));
            FWCicero.info(whitelistP.toString());
            oos1.writeObject(whitelistP);
            oos1.close();
            ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream("plugins/FWCicero/visitaPlayer.markus"));
            FWCicero.info(visita.toString());
            oos2.writeObject(visita);
            oos2.close();
            ObjectOutputStream oos3 = new ObjectOutputStream(new FileOutputStream("plugins/FWCicero/locations.markus"));
            FWCicero.info(locationC.toString());
            oos3.writeObject(locationC);
            oos3.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //Carica le HashMap da file
    public static void loadHash(){
        try {
            FileInputStream fileIn = new FileInputStream("plugins/FWCicero/whiteCitta.markus");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            listaCitta = (ArrayList<String>) in.readObject();
            FWCicero.info(listaCitta.toString());
            in.close();
            fileIn.close();
            FileInputStream fileIn1 = new FileInputStream("plugins/FWCicero/whitePlayer.markus");
            ObjectInputStream in1 = new ObjectInputStream(fileIn1);
            whitelistP = (HashMap<String,Long>) in1.readObject();
            FWCicero.info(whitelistP.toString());
            in1.close();
            fileIn1.close();
            FileInputStream fileIn2 = new FileInputStream("plugins/FWCicero/visitaPlayer.markus");
            ObjectInputStream in2 = new ObjectInputStream(fileIn2);
            visita = (HashMap<String,String>) in2.readObject();
            FWCicero.info(visita.toString());
            in2.close();
            fileIn2.close();
            FileInputStream fileIn3 = new FileInputStream("plugins/FWCicero/locations.markus");
            ObjectInputStream in3 = new ObjectInputStream(fileIn3);
            locationC = (HashMap<String,String>) in3.readObject();
            FWCicero.info(locationC.toString());
            in3.close();
            fileIn3.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
