package it.forgottenworld.fwcicero.command.cicero.user;

import it.forgottenworld.fwcicero.command.CiceroCommandExecutor;
import it.forgottenworld.fwcicero.command.SubCommand;
import it.forgottenworld.fwcicero.utility.ChatFormatter;
import it.forgottenworld.fwcicero.utility.Permissions;
import it.forgottenworld.fwcicero.utility.commands.CommandDescriptions;
import it.forgottenworld.fwcicero.utility.commands.CommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpCommand extends SubCommand {
    @Override
    public String getName() {
        return CommandNames.HELP_CMD;
    }

    @Override
    public String getDescription() {
        return CommandDescriptions.HELP_CMD;
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
        sender.sendMessage(ChatFormatter.chatHeader());

        sender.sendMessage(ChatFormatter.formatListMessage("cicero", "", "", CommandDescriptions.CICERO_CMD));

        for (int i = 0; i < CiceroCommandExecutor.getSubcommands().size(); i++) {

            SubCommand subCommand = CiceroCommandExecutor.getSubcommands().get(i);

            if (!sender.hasPermission(subCommand.getPermission()))
                continue;

            sender.sendMessage(ChatFormatter.formatListMessage("cicero", subCommand.getName(), subCommand.getArgumentsName(), subCommand.getDescription()));
        }

        sender.sendMessage(ChatFormatter.formatListMessage("protect", "", "", CommandDescriptions.PROTECT_CMD));

        sender.sendMessage(ChatFormatter.chatFooter());
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
