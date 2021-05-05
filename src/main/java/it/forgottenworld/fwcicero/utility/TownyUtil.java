package it.forgottenworld.fwcicero.utility;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;

public class TownyUtil {

    /**
     *
     * @param name
     * @return
     */
    public static Town getTownFromString(String name) {
        try {
            return TownyAPI.getInstance().getDataSource().getTown(name);
        } catch (NotRegisteredException e) {}
        return null;
    }

    /**
     *
     * @param name
     * @return
     */
    public static boolean isTown(String name){
        if (getTownFromString(name) == null)
            return false;
        return TownyUniverse.getInstance().getDataSource().getTowns().contains(getTownFromString(name));
    }

}
