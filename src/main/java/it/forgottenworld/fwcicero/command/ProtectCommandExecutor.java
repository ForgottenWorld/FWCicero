package it.forgottenworld.fwcicero.command;

import it.forgottenworld.fwcicero.command.protect.admin.AllowCommand;
import it.forgottenworld.fwcicero.command.protect.user.LeaveCommand;
import it.forgottenworld.fwcicero.command.protect.user.TimeleftCommand;
import it.forgottenworld.fwcicero.command.protect.admin.RemoveCommand;
import it.forgottenworld.fwcicero.utility.ChatFormatter;
import it.forgottenworld.fwcicero.utility.Messages;
import it.forgottenworld.fwcicero.utility.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProtectCommandExecutor implements TabExecutor {

    private static final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public ProtectCommandExecutor(){
        subcommands.add(new AllowCommand());
        subcommands.add(new LeaveCommand());
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
            sender.sendMessage(ChatFormatter.chatHeader());

            for (int i = 0; i < getSubcommands().size(); i++) {

                SubCommand subCommand = getSubcommands().get(i);

                if (!sender.hasPermission(subCommand.getPermission()))
                    continue;

                sender.sendMessage(ChatFormatter.formatListMessage(label, subCommand.getName(), subCommand.getArgumentsName(), subCommand.getDescription()));
            }

            sender.sendMessage(ChatFormatter.chatFooter());
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
