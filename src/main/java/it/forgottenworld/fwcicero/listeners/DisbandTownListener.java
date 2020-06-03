package it.forgottenworld.fwcicero.listeners;

import com.palmergames.bukkit.towny.event.DeleteTownEvent;
import it.forgottenworld.fwcicero.FWCicero;
import it.forgottenworld.fwcicero.command.CiceroCommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DisbandTownListener implements Listener {

    @EventHandler(priority = org.bukkit.event.EventPriority.LOWEST)
    public void onDisbandTownEvent(DeleteTownEvent e){
        if(CiceroCommandExecutor.listaCitta.contains(e.getTownName())){
            CiceroCommandExecutor.listaCitta.remove(e.getTownName());
            CiceroCommandExecutor.locationC.remove(e.getTownName());
            FWCicero.info("Citta' " + e.getTownName() + " rimossa automaticamente.");
        }
    }

}
