package it.forgottenworld.fwcicero.listeners;

import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import it.forgottenworld.fwcicero.object.managers.CiceroManager;
import it.forgottenworld.fwcicero.utility.ChatFormatter;
import it.forgottenworld.fwcicero.utility.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BecomeResidentListener implements Listener {

    @EventHandler
    public void onPlayerBecomeResident(TownAddResidentEvent e){
        if (CiceroManager.isPlayerInCicero(e.getResident().getUUID())) {
            CiceroManager.playerInCicero.remove(e.getResident().getUUID());
            Player player = Bukkit.getPlayer(e.getResident().getUUID());
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.JOIN_TOWN));
        }

        if (NewPlayerListener.invulnerability.containsKey(e.getResident().getUUID())) {
            NewPlayerListener.invulnerability.remove(e.getResident().getUUID());
            Player player = Bukkit.getPlayer(e.getResident().getUUID());
            player.sendMessage(ChatFormatter.formatProtectErrorMessage(Messages.JOIN_TOWN));
        }
    }

}
