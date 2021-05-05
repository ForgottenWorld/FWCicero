package it.forgottenworld.fwcicero.object.managers;

import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwcicero.FWCicero;
import it.forgottenworld.fwcicero.config.ConfigManager;
import it.forgottenworld.fwcicero.object.CiceroLocation;
import it.forgottenworld.fwcicero.utility.FWLocation;
import it.forgottenworld.fwcicero.utility.TownyUtil;
import org.bukkit.Location;

import java.util.ArrayList;

public class CiceroLocationManager {

    static ArrayList<CiceroLocation> ciceroTowns = new ArrayList<>();

    public static void addCiceroLocation(Town town, Location location){
        if (contains(town))
            return;

        ciceroTowns.add(new CiceroLocation(town, location));

        save();
    }

    public static void removeCiceroLocation(Town town){
        ciceroTowns.remove(getCiceroLocationFromTown(town));

        save();
    }

    public static CiceroLocation getCiceroLocationFromTown(Town town) {
        for (CiceroLocation cl : ciceroTowns) {
            if (town.getUuid().equals(cl.getTown().getUuid()))
                return cl;
        }
        return null;
    }

    public static boolean contains(Town town) {
        return getCiceroLocations().contains(getCiceroLocationFromTown(town));
    }

    public static ArrayList<CiceroLocation> getCiceroLocations() {
        return ciceroTowns;
    }

    public static void save() {
        ConfigManager database = FWCicero.getInstance().getDatabase();
        database.getFile().set("locations", null);

        for (CiceroLocation cl : getCiceroLocations()){
            String path = "locations." + cl.getTown().getName();
            database.getFile().set(path + ".loc", FWLocation.getStringFromLocation(cl.getLocation()));
        }

        database.saveFile();
    }

    public static void load() {
        ConfigManager database = FWCicero.getInstance().getDatabase();
        ciceroTowns.clear();
        CiceroManager.listaCitta.clear();

        if (database.getFile().getConfigurationSection("locations") != null){
            for (String town : database.getFile().getConfigurationSection("locations").getKeys(false)){
                CiceroLocation cl = new CiceroLocation(TownyUtil.getTownFromString(town), FWLocation.getLocationFromString(database.getFile().getString("locations." + town + ".loc")));
                ciceroTowns.add(cl);
                CiceroManager.listaCitta.add(cl.getTown().getName());
            }
        }
    }
}
