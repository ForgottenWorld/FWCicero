package it.forgottenworld.fwcicero.object;

import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Location;

import java.util.Objects;

public class CiceroLocation {

    private Town town;
    private Location location;

    public CiceroLocation(Town town, Location location){
        this.town = town;
        this.location = location;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public Town getTown() {
        return town;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "CiceroLocation{" +
                "town=" + town +
                ", location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CiceroLocation)) return false;
        CiceroLocation that = (CiceroLocation) o;
        return Objects.equals(getTown(), that.getTown()) &&
                Objects.equals(getLocation(), that.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTown(), getLocation());
    }
}
