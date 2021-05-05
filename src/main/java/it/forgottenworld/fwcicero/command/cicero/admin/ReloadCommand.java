package it.forgottenworld.fwcicero.command.cicero.admin;

import it.forgottenworld.fwcicero.FWCicero;
import it.forgottenworld.fwcicero.command.SubCommand;
import it.forgottenworld.fwcicero.utility.ChatFormatter;
import it.forgottenworld.fwcicero.utility.Messages;
import it.forgottenworld.fwcicero.utility.Permissions;
import it.forgottenworld.fwcicero.utility.commands.CommandDescriptions;
import it.forgottenworld.fwcicero.utility.commands.CommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends SubCommand {
    @Override
    public String getName() {
        return CommandNames.RELOAD_CMD;
    }

    @Override
    public String getDescription() {
        return CommandDescriptions.RELOAD_CMD;
    }

    @Override
    public String getArgumentsName() {
        return "";
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
        return 1;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        FWCicero.saveData();
        FWCicero.loadData();

        FWCicero.info(Messages.CONFIG_RELOAD);

        if(sender instanceof Player)
            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.CONFIG_RELOAD));
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
