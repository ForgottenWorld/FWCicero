package it.forgottenworld.fwcicero.listeners;

import it.forgottenworld.fwcicero.FWCicero;
import it.forgottenworld.fwcicero.object.managers.CiceroManager;
import it.forgottenworld.fwcicero.utility.ChatFormatter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;
import java.util.UUID;

public class NewPlayerListener implements Listener {

    public static HashMap<UUID,Long> invulnerability = new HashMap<UUID,Long>();

    @EventHandler(priority = org.bukkit.event.EventPriority.LOWEST)
    public void onNewPlayerJoin(PlayerJoinEvent e){

        //Acquisisco il player
        Player join = (Player) e.getPlayer();

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
        invulnerability.put(join.getUniqueId(), timeLeft);
        CiceroManager.playerInCicero.put(join.getUniqueId(), timeLeft);
        CiceroManager.save();
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.LOWEST)
    public void onNewPlayerHit(EntityDamageByEntityEvent e){
        //Controllo se sono due player
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
            if(checkAction((Player)e.getEntity(),(Player)e.getDamager()))
                e.setCancelled(true);

        } else if (e.getEntity() instanceof Player && e.getDamager() instanceof Projectile){
            // Se il danno proviene da un arco
            // Prendo colui che ha lanciato la freccia
            ProjectileSource attaccante = ((Projectile)e.getDamager()).getShooter();
            // Se l'ha lanciata un Player
            if (attaccante instanceof Player) {
                if(checkAction((Player)e.getEntity(),(Player)attaccante))
                    e.setCancelled(true);

            }
        }
    }

    public boolean checkAction(Player defender, Player striker){
        //Se il difensore è nuovo
        if(invulnerability.containsKey(defender.getUniqueId())){
            //Controllo se è già passato il tempo
            if(invulnerability.get(defender.getUniqueId()) <= System.currentTimeMillis()){
                invulnerability.remove(defender.getUniqueId());
                return false;
            }else{
                //Cancello l'evento
                //todo message
                striker.sendMessage(ChatFormatter.formatProtectErrorMessage("Non puoi colpire un altro player, non sono scaduti i suoi due giorni di protezione."));
                return true;
            }
        }

        //Se l'attaccante è nuovo
        if(invulnerability.containsKey(striker.getUniqueId())){
            //Controllo se è già passato il tempo
            if(invulnerability.get(striker.getUniqueId()) <= System.currentTimeMillis()){
                invulnerability.remove(striker.getUniqueId());
                return false;
            }else{
                //Cancello l'evento
                //todo message
                striker.sendMessage(ChatFormatter.formatProtectErrorMessage("Non puoi colpire un altro player, non sono scaduti i tuoi due giorni di protezione."));
                return true;
            }
        }

        return false;
    }

}
