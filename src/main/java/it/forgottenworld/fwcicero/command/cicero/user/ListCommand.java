package it.forgottenworld.fwcicero.command.cicero.user;

import it.forgottenworld.fwcicero.command.SubCommand;
import it.forgottenworld.fwcicero.object.CiceroLocation;
import it.forgottenworld.fwcicero.object.managers.CiceroLocationManager;
import it.forgottenworld.fwcicero.utility.ChatFormatter;
import it.forgottenworld.fwcicero.utility.Messages;
import it.forgottenworld.fwcicero.utility.Permissions;
import it.forgottenworld.fwcicero.utility.commands.CommandDescriptions;
import it.forgottenworld.fwcicero.utility.commands.CommandNames;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ListCommand extends SubCommand {
    @Override
    public String getName() {
        return CommandNames.LIST_CMD;
    }

    @Override
    public String getDescription() {
        return CommandDescriptions.LIST_CMD;
    }

    @Override
    public String getArgumentsName() {
        return "";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_USER;
    }

    @Override
    public boolean getCanConsoleRunCmd() {
        return true;
    }

    @Override
    public int getArgsRequired() {
        return 1;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!CiceroLocationManager.getCiceroLocations().isEmpty()){
            sender.sendMessage(ChatFormatter.chatHeader());
            sender.sendMessage(ChatColor.GRAY + "Lista delle citta':");
            for(CiceroLocation cl : CiceroLocationManager.getCiceroLocations()){
                sender.sendMessage(ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + cl.getTown().getName());
            }
            sender.sendMessage(ChatFormatter.chatFooter());
        }else{
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
