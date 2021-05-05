package it.forgottenworld.fwcicero.command;

import it.forgottenworld.fwcicero.FWCicero;
import it.forgottenworld.fwcicero.command.cicero.admin.AddCommand;
import it.forgottenworld.fwcicero.command.cicero.admin.AllowCommand;
import it.forgottenworld.fwcicero.command.cicero.admin.ReloadCommand;
import it.forgottenworld.fwcicero.command.cicero.admin.RemoveCommand;
import it.forgottenworld.fwcicero.command.cicero.user.HelpCommand;
import it.forgottenworld.fwcicero.command.cicero.user.ListCommand;
import it.forgottenworld.fwcicero.command.cicero.user.TimeleftCommand;
import it.forgottenworld.fwcicero.object.managers.CiceroLocationManager;
import it.forgottenworld.fwcicero.object.managers.CiceroManager;
import it.forgottenworld.fwcicero.utility.ChatFormatter;
import it.forgottenworld.fwcicero.utility.Messages;
import it.forgottenworld.fwcicero.utility.Permissions;
import it.forgottenworld.fwcicero.utility.TownyUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CiceroCommandExecutor implements TabExecutor {

    private static final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CiceroCommandExecutor(){
        subcommands.add(new AddCommand());
        subcommands.add(new AllowCommand());
        subcommands.add(new HelpCommand());
        subcommands.add(new ListCommand());
        subcommands.add(new ReloadCommand());
        subcommands.add(new RemoveCommand());
        subcommands.add(new TimeleftCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < getSubcommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {

                    SubCommand subCommand = getSubcommands().get(i);

                    if (!subCommand.getCanConsoleRunCmd()) {
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(Messages.NO_CONSOLE);
                            return true;
                        }
                    }

                    if (!Permissions.playerHasPermission(sender, subCommand.getPermission()))
                        return true;

                    if (args.length < subCommand.getArgsRequired()) {
                        sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_SYNTAX));
                        return true;
                    }

                    subCommand.perform(sender, args);

                }
            }
        }else{
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.NO_CONSOLE);
                return true;
            }

            Player player = (Player) sender;

            if(CiceroManager.lastTownViewedPlayer.containsKey(player.getUniqueId())){
                // Se sta già visitando le città
                // Se il player non sta usando il comando nelle 48 ore di tempo concesso
                if(System.currentTimeMillis() >= CiceroManager.playerInCicero.get(player.getUniqueId()) && CiceroManager.isPlayerInCicero(player.getUniqueId())){
                    player.sendMessage(ChatFormatter.formatErrorMessage(Messages.END_TIME));
                    CiceroManager.playerInCicero.remove(player.getUniqueId());
                    CiceroManager.lastTownViewedPlayer.remove(player.getUniqueId());
                    return true;
                }

                int index = CiceroManager.listaCitta.indexOf(CiceroManager.lastTownViewedPlayer.get(player.getUniqueId()).getTown().getName());

                if(CiceroManager.listaCitta.size()>index+1){
                    String town = CiceroManager.listaCitta.get(index+1);
                    CiceroManager.teleportAction(TownyUtil.getTownFromString(town), player);
                    //todo message
                    player.sendMessage(ChatFormatter.formatSuccessMessage("Stai visitando " + ChatColor.GOLD + town));
                    CiceroManager.lastTownViewedPlayer.put(player.getUniqueId(), CiceroLocationManager.getCiceroLocationFromTown(TownyUtil.getTownFromString(town)));
                }else{
                    player.sendMessage(ChatFormatter.formatWarningMessage(Messages.END_TOWN));
                    CiceroManager.playerInCicero.remove(player.getUniqueId());
                    CiceroManager.lastTownViewedPlayer.remove(player.getUniqueId());
                    //Location Spes
                    World w = sender.getServer().getWorld(FWCicero.getInstance().getConfig().getString("respawn.w"));
                    //todo message
                    Location SPES = new Location(w, FWCicero.getInstance().getConfig().getInt("respawn.x"), FWCicero.getInstance().getConfig().getInt("respawn.y"), FWCicero.getInstance().getConfig().getInt("respawn.z"));
                    Player p = (Player)sender;
                    p.teleport(SPES, PlayerTeleportEvent.TeleportCause.PLUGIN);
                }

            }else{
                // Se è la prima volta o non sta visitando
                // Se il player non è abilitato ad utilizzare il comando
                if(!CiceroManager.isPlayerInCicero(player.getUniqueId())){
                    player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NOT_ALLOW));
                    return true;
                }

                // Se non ci sono città in whitelist
                if(CiceroManager.listaCitta.isEmpty()){
                    player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN));
                    return true;
                }

                CiceroManager.lastTownViewedPlayer.put(player.getUniqueId(), CiceroLocationManager.getCiceroLocationFromTown(TownyUtil.getTownFromString(CiceroManager.listaCitta.get(0))));
                CiceroManager.teleportAction(TownyUtil.getTownFromString(CiceroManager.listaCitta.get(0)), player);
                //todo message
                player.sendMessage(ChatFormatter.formatSuccessMessage("Stai visitando " + ChatColor.GOLD + CiceroManager.listaCitta.get(0)));
            }
        }

        return true;
    }

    public static ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            for (int i = 0; i < getSubcommands().size(); i++) {
                SubCommand subCommand = getSubcommands().get(i);

                if (!sender.hasPermission(subCommand.getPermission()))
                    continue;

                subcommandsArguments.add(subCommand.getName());
            }

            return subcommandsArguments;

        }else if (args.length >= 2) {
            for (int i = 0; i < getSubcommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                    return getSubcommands().get(i).getSubcommandArguments((Player) sender, args);
                }
            }
        }

        return null;
    }
}