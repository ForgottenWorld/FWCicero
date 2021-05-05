package it.forgottenworld.fwcicero.utility;

import org.bukkit.ChatColor;

import java.util.Collections;

public class ChatFormatter {

    /**
     * PREFIX
     */
    public static String pluginPrefix() {
        return  ChatColor.DARK_GRAY + "[" +
                ChatColor.DARK_GREEN + "Cicero" +
                ChatColor.DARK_GRAY + "] " +
                ChatColor.RESET;
    }

    public static String pluginPrefixProtect() {
        return  pluginPrefix() +
                ChatColor.DARK_GRAY + "[" +
                ChatColor.DARK_RED + "Protect" +
                ChatColor.DARK_GRAY + "] " +
                ChatColor.RESET;
    }

    /**
     * HEADER & FOOTER
     */
    public static String chatHeader() {
        return  ChatColor.GREEN + "----------------------[ " +
                ChatColor.DARK_GREEN + "Cicero" +
                ChatColor.GREEN + " ]-----------------------";
    }

    public static String chatFooter() {
        return  ChatColor.GREEN + String.join("", Collections.nCopies(53, "-"));
    }

    /**
     * MESSAGE
     * CICERO
     */
    public static String formatSuccessMessage(String message) {
        message = pluginPrefix() + ChatColor.GREEN + message;
        return message;
    }

    public static String formatWarningMessage(String message) {
        message = pluginPrefix() + ChatColor.GOLD + message;
        return message;
    }

    public static String formatErrorMessage(String message) {
        message = pluginPrefix() + ChatColor.RED + message;
        return message;
    }

    /**
     * PROTECT
     */
    public static String formatProtectSuccessMessage(String message) {
        message = pluginPrefixProtect() + ChatColor.GREEN + message;
        return message;
    }

    public static String formatProtectWarningMessage(String message) {
        message = pluginPrefixProtect() + ChatColor.GOLD + message;
        return message;
    }

    public static String formatProtectErrorMessage(String message) {
        message = pluginPrefixProtect() + ChatColor.RED + message;
        return message;
    }

    /**
     * NO-PREFIX
     */
    public static String formatSuccessMessageNoPrefix(String message) {
        message = ChatColor.GREEN + message;
        return message;
    }

    public static String formatWarningMessageNoPrefix(String message) {
        message = ChatColor.GOLD + message;
        return message;
    }

    public static String formatErrorMessageNoPrefix(String message) {
        message = ChatColor.RED + message;
        return message;
    }

    public static String formatListMessage(String command, String subcommand, String args, String description) {
        String message = ChatColor.GOLD + "> " +
                ChatColor.DARK_GREEN + "/" + command + " " +
                ChatColor.GREEN + subcommand + " " + args +
                ChatColor.GRAY + ": " + description;
        return message;
    }
}
