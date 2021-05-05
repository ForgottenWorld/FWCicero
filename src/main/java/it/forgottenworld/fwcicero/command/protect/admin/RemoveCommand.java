package it.forgottenworld.fwcicero.command.protect.admin;

import it.forgottenworld.fwcicero.command.SubCommand;
import it.forgottenworld.fwcicero.listeners.NewPlayerListener;
import it.forgottenworld.fwcicero.utility.ChatFormatter;
import it.forgottenworld.fwcicero.utility.Permissions;
import it.forgottenworld.fwcicero.utility.commands.CommandDescriptions;
import it.forgottenworld.fwcicero.utility.commands.CommandNames;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RemoveCommand extends SubCommand {
    @Override
    public String getName() {
        return CommandNames.REMOVE_CMD;
    }

    @Override
    public String getDescription() {
        return CommandDescriptions.REMOVE_PROTECT_CMD;
    }

    @Override
    public String getArgumentsName() {
        return "<player>";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_MOD;
    }

    @Override
    public boolean getCanConsoleRunCmd() {
        return true;
    }

    @Override
    public int getArgsRequired() {
        return 2;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender.getServer().getPlayer(args[1]) != null) {
            Player removePlayer = Bukkit.getServer().getPlayer(args[1]);
            if (NewPlayerListener.invulnerability.containsKey(removePlayer.getUniqueId())) {
                NewPlayerListener.invulnerability.remove(removePlayer.getUniqueId());
                //todo message
                sender.sendMessage(ChatFormatter.formatProtectSuccessMessage("Il player " + args[1] + " ha perso la protezione."));
            } else {
                //todo message
                sender.sendMessage(ChatFormatter.formatProtectSuccessMessage("Il player non è presente nella lista."));
            }
        }else{
            //todo message
            sender.sendMessage(ChatFormatter.formatErrorMessage("Il player " + args[1] + " e' Offline o inesistente."));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        ArrayList<String> result = new ArrayList<>();

        if (args.length == 2) {
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (int i = 0; i < players.length; i++) {
                if (players[i].getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    result.add(players[i].getName());
                }
            }
        }

        return result;
    }
}
