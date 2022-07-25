package be.alexandre01.fk.base;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.base.core.Core;
import be.alexandre01.fk.teams.Team;
import be.alexandre01.universal.server.utils.locations.Cuboid;
import be.alexandre01.universal.server.utils.locations.Region;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Base {

    @Getter Region region;


    @Getter
    Location spawn;

    @Getter Location coreSpawn;

    @Getter
    Core core;
    @Getter String name;
    @Getter @Setter
    Team team;

    public Base(Region region, Location location,Location coreSpawn, String name) {
        this.region = region;
        this.name = name;
        this.spawn = location;
        this.coreSpawn = coreSpawn;

        FKPlugin.instance.getBases().add(this);

        this.core = new Core(this);
    }




    public boolean isIn(Location location) {
        System.out.println(region.getMinX() + " " + region.getMaxX() + " " + region.getMinZ() + " " + region.getMaxZ());
        return region.contains(location);
    }


    public boolean isIn(Player player) {
        return region.contains(player.getLocation());
    }


    public static boolean isInBase(Location location) {
        for(Base base : FKPlugin.instance.getBases()){
            if(base.isIn(location)){
                return true;
            }
        }
        return false;
    }
}
