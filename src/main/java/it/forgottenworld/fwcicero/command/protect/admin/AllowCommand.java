package it.forgottenworld.fwcicero.command.protect.admin;

import it.forgottenworld.fwcicero.FWCicero;
import it.forgottenworld.fwcicero.command.SubCommand;
import it.forgottenworld.fwcicero.listeners.NewPlayerListener;
import it.forgottenworld.fwcicero.object.managers.CiceroManager;
import it.forgottenworld.fwcicero.utility.ChatFormatter;
import it.forgottenworld.fwcicero.utility.Permissions;
import it.forgottenworld.fwcicero.utility.commands.CommandDescriptions;
import it.forgottenworld.fwcicero.utility.commands.CommandNames;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AllowCommand extends SubCommand {
    @Override
    public String getName() {
        return CommandNames.ALLOW_CMD;
    }

    @Override
    public String getDescription() {
        return CommandDescriptions.ALLOW_PROTECT_CMD;
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
        if(sender.getServer().getPlayer(args[1]) != null){
            if (NewPlayerListener.invulnerability.containsKey(sender.getServer().getPlayerUniqueId(args[1]))) {
                //todo message
                sender.sendMessage(ChatFormatter.formatProtectErrorMessage("Il player " + args[1] + " e' gia' stato abilitato."));
                return;
            }
            //Se l'utente è nuovo
            //Aggiungo il player alla whitelist, 48 ore per lanciare il comando
            long timeLeft = System.currentTimeMillis() + FWCicero.getDefaultConfig().getInt("time");
            NewPlayerListener.invulnerability.put(sender.getServer().getPlayerUniqueId(args[1]), timeLeft);

            CiceroManager.save();
            //todo message
            sender.sendMessage(ChatFormatter.formatProtectSuccessMessage("Il player " + args[1] + " e' stato aggiungo alla protezione."));
        }else{
            //todo message
            sender.sendMessage(ChatFormatter.formatProtectErrorMessage("Il player " + args[1] + " e' Offline o inesistente."));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        ArrayList<String> result = new ArrayList<>();

        if (args.length == 2) {
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (int i = 0; i < players.length; i++) {
                if (players[i].getName().toLowerCase().startsWith(args[1].toLowerCase()))
                    result.add(players[i].getName());
            }
        }

        return result;
    }
}
