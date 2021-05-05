package it.forgottenworld.fwcicero.command.cicero.admin;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwcicero.command.SubCommand;
import it.forgottenworld.fwcicero.object.managers.CiceroLocationManager;
import it.forgottenworld.fwcicero.object.managers.CiceroManager;
import it.forgottenworld.fwcicero.utility.ChatFormatter;
import it.forgottenworld.fwcicero.utility.Permissions;
import it.forgottenworld.fwcicero.utility.TownyUtil;
import it.forgottenworld.fwcicero.utility.commands.CommandDescriptions;
import it.forgottenworld.fwcicero.utility.commands.CommandNames;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AddCommand extends SubCommand {
    @Override
    public String getName() {
        return CommandNames.ADD_CMD;
    }

    @Override
    public String getDescription() {
        return CommandDescriptions.ADD_CMD;
    }

    @Override
    public String getArgumentsName() {
        return "<town> [-f]";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_MOD;
    }

    @Override
    public boolean getCanConsoleRunCmd() {
        return false;
    }

    @Override
    public int getArgsRequired() {
        return 2;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (TownyUtil.isTown(args[1])){
            if (CiceroLocationManager.getCiceroLocationFromTown(TownyUtil.getTownFromString(args[1])) == null){
                Town town = TownyUtil.getTownFromString(args[1]);

                Location loc = null;

                if (args.length > 2 && args[2].equalsIgnoreCase("-f")) {
                    loc = player.getLocation();
                } else {
                    if (!town.hasSpawn()) {
                        //todo message
                        player.sendMessage(ChatFormatter.formatErrorMessage("La citta' non ha settato uno spawn-point."));
                        return;
                    } else {
                        try {
                            loc = town.getSpawn();
                        } catch (TownyException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //Aggiunge la città alla whitelist
                CiceroLocationManager.addCiceroLocation(town, loc);
                CiceroManager.listaCitta.add(town.getName());

                //todo message
                player.sendMessage(ChatFormatter.formatSuccessMessage("La citta' " + args[1] + " e' stata aggiunta al cicero!"));
            } else {
                //todo message
                player.sendMessage(ChatFormatter.formatErrorMessage("La citta' " + args[1] + " e' gia' presente in whitelist."));
            }

        } else {
            //todo message
            player.sendMessage(ChatFormatter.formatErrorMessage("La citta' " + args[1] + " e' inesistente!"));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        ArrayList<String> result = new ArrayList<>();

        if (args.length == 2) {
            Town[] towns = new Town[TownyUniverse.getInstance().getDataSource().getTowns().size()];
            TownyUniverse.getInstance().getDataSource().getTowns().toArray(towns);
            for (int i = 0; i < towns.length; i++) {
                if (towns[i].getName().toLowerCase().startsWith(args[1].toLowerCase()))
                    if (!CiceroManager.listaCitta.contains(towns[i].getName()))
                        result.add(towns[i].getName());
            }
        }

        return result;
    }
}
