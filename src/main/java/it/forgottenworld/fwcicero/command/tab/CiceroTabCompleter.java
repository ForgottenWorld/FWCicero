package it.forgottenworld.fwcicero.command.tab;

import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.TownyUniverse;
import it.forgottenworld.fwcicero.command.CiceroCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CiceroTabCompleter implements TabCompleter {

    List<String> arguments = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(arguments.isEmpty()){
            if(sender.hasPermission("forgotten.cicero.mod"))
                arguments.add("add");

            if(sender.hasPermission("forgotten.cicero.mod"))
                arguments.add("allow");

            arguments.add("help");
            arguments.add("list");

            if(sender.hasPermission("forgotten.cicero.mod"))
                arguments.add("reload");

            if(sender.hasPermission("forgotten.cicero.mod"))
                arguments.add("remove");

            arguments.add("timeleft");
        }

        if(args.length == 1){
            List<String> result = new ArrayList<String>();
            for (String a : arguments){
                if(a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            return result;
        }

        if(args.length == 2){
            List<String> result = new ArrayList<String>();
            switch (args[0]) {
                case "add":
                    Town[] towns = new Town[TownyUniverse.getInstance().getDataSource().getTowns().size()];
                    TownyUniverse.getInstance().getDataSource().getTowns().toArray(towns);
                    for (int i = 0; i < towns.length; i++) {
                        if (towns[i].getName().toLowerCase().startsWith(args[1].toLowerCase()))
                            result.add(towns[i].getName());
                    }
                    break;

                case "allow":
                    Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                    Bukkit.getServer().getOnlinePlayers().toArray(players);
                    for (int i = 0; i < players.length; i++) {
                        if (players[i].getName().toLowerCase().startsWith(args[1].toLowerCase()))
                            result.add(players[i].getName());
                    }
                    break;

                case "remove":
                    for (String c : CiceroCommandExecutor.listaCitta) {
                        if (c.toLowerCase().startsWith(args[1].toLowerCase()))
                            result.add(c);
                    }
                    break;
            }

            return result;
        }

        if(args.length == 3){
            List<String> result = new ArrayList<String>();
            switch (args[0]) {
                case "add":
                    result.add("-f");
                    break;
            }

            return result;
        }

        return null;
    }

}
