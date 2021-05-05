package it.forgottenworld.fwcicero.listeners;

import com.palmergames.bukkit.towny.event.PreDeleteTownEvent;
import it.forgottenworld.fwcicero.FWCicero;
import it.forgottenworld.fwcicero.object.managers.CiceroLocationManager;
import it.forgottenworld.fwcicero.object.managers.CiceroManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DisbandTownListener implements Listener {

    @EventHandler(priority = org.bukkit.event.EventPriority.LOWEST)
    public void onDisbandTownEvent(PreDeleteTownEvent e){
        if(CiceroLocationManager.contains(e.getTown())){
            CiceroLocationManager.removeCiceroLocation(e.getTown());
            CiceroManager.listaCitta.remove(e.getTown().getName());
            //todo message
            FWCicero.info("Citta' " + e.getTownName() + " rimossa automaticamente.");
        }
    }

}
