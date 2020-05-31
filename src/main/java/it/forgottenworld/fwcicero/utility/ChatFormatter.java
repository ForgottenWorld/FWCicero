package it.forgottenworld.fwcicero.utility;

import org.bukkit.ChatColor;

public class ChatFormatter {

    public static String pluginPrefix() {
        return  ChatColor.DARK_GRAY + "[" +
                ChatColor.DARK_GREEN + "Cicero" +
                ChatColor.DARK_GRAY + "] " +
                ChatColor.RESET;
    }

    public static String chatHeader() {
        return  ChatColor.GREEN + "-------------------[ " +
                ChatColor.DARK_GREEN + "Cicero" +
                ChatColor.GREEN + " ]-------------------";
    }

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

    public static String helpMessage() {
        String message = chatHeader();
        message = message.concat(
                "\n" + ChatColor.GRAY + "Lista comandi:" +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_GREEN + "/cicero " + ChatColor.GRAY + ": Attiva la visita delle citta'." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_GREEN + "/cicero " + ChatColor.GREEN + "add " + "<Town> " + ChatColor.GRAY + ": Aggiunge una Town al Cicero." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_GREEN + "/cicero " + ChatColor.GREEN + "add " + "<Town> " + "[-f] " + ChatColor.GRAY + ": Aggiunge una Town al Cicero sulla tua posizione. " +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_GREEN + "/cicero " + ChatColor.GREEN + "allow " + "<Player> " + ChatColor.GRAY + ": Abilita un Player al Cicero." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_GREEN + "/cicero " + ChatColor.GREEN + "help " + ChatColor.GRAY + ": Mostra questo elenco." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_GREEN + "/cicero " + ChatColor.GREEN + "list " + ChatColor.GRAY + ": Mostra la lista delle Town nel Cicero." + "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_GREEN + "/cicero " + ChatColor.GREEN + "reload " + ChatColor.GRAY + ": Ricarica il config.yml." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_GREEN + "/cicero " + ChatColor.GREEN + "remove " + "<Town> " + ChatColor.GRAY + ": Rimuove una Town dal Cicero." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_GREEN + "/cicero " + ChatColor.GREEN + "timeleft " + ChatColor.GRAY + ": Mostra il tempo residuo per utilizzare il Cicero."
        );
        return message;
    }

}
