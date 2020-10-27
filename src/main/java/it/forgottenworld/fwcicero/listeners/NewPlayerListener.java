package it.forgottenworld.fwcicero.listeners;

import it.forgottenworld.fwcicero.FWCicero;
import it.forgottenworld.fwcicero.command.CiceroCommandExecutor;
import it.forgottenworld.fwcicero.utility.ChatFormatter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NewPlayerListener implements Listener {

    @EventHandler(priority = org.bukkit.event.EventPriority.LOWEST)
    public void onNewPlayerJoin(PlayerJoinEvent e){

        //Acquisisco il player
        Player join = (Player)e.getPlayer();

        int hours = (int) ((FWCicero.getDefaultConfig().getInt("time") / (1000*60*60)));

        //Controllo se non è la prima volta che entra
        if(join.hasPlayedBefore()) return;

        //Se è nuovo
        TextComponent motd = new TextComponent("/cicero help");
        motd.setColor(ChatColor.GREEN);
        motd.setItalic(true);
        motd.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cicero help"));
        motd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Premi qui per visualizzare la lista delle funzionalita' del Cicero").color(ChatColor.GRAY).italic(true).create()));
        e.getPlayer().sendMessage(ChatFormatter.pluginPrefix() + ChatColor.GREEN + "Ciao, sembra essere la prima volta che entri nel server! Mi presento io sono Cicero, come nuovo utente hai diritto alla possibilita' di viaggiare nelle citta' di tutto il piano e di visitarle, con il mio aiuto, per " + hours + " ore. Buon divertimento! Quando sei pronto digita in chat oppure premi:");
        e.getPlayer().spigot().sendMessage(motd);

        long timeLeft = System.currentTimeMillis() + FWCicero.getDefaultConfig().getInt("time");
        CiceroCommandExecutor.whitelistP.put(join.toString(), timeLeft);
    }

}
