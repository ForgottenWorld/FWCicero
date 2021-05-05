package it.forgottenworld.fwcicero.command.protect.user;

import it.forgottenworld.fwcicero.command.SubCommand;
import it.forgottenworld.fwcicero.listeners.NewPlayerListener;
import it.forgottenworld.fwcicero.object.managers.CiceroManager;
import it.forgottenworld.fwcicero.utility.ChatFormatter;
import it.forgottenworld.fwcicero.utility.Permissions;
import it.forgottenworld.fwcicero.utility.commands.CommandDescriptions;
import it.forgottenworld.fwcicero.utility.commands.CommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TimeleftCommand extends SubCommand {
    @Override
    public String getName() {
        return CommandNames.TIMELEFT_CMD;
    }

    @Override
    public String getDescription() {
        return CommandDescriptions.TIMELEFT_PROTECT_CMD;
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
        return false;
    }

    @Override
    public int getArgsRequired() {
        return 1;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(NewPlayerListener.invulnerability.containsKey(((Player) sender).getUniqueId())){
            sender.sendMessage(ChatFormatter.formatProtectSuccessMessage(CiceroManager.convertMillisecond((Player)sender,NewPlayerListener.invulnerability.get(((Player) sender).getUniqueId()),System.currentTimeMillis())));
        }else{
            //todo message
            sender.sendMessage(ChatFormatter.formatProtectErrorMessage("Non sei sotto alcuna protezione."));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
