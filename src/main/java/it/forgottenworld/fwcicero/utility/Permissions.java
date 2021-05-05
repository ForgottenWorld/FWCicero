package it.forgottenworld.fwcicero.utility;

import org.bukkit.command.CommandSender;

public class Permissions {

    public static final String PERM_USER = "forgotten.cicero.user";
    public static final String PERM_MOD = "forgotten.cicero.mod";

    public static boolean playerHasPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission))
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));

        return sender.hasPermission(permission);
    }
}
