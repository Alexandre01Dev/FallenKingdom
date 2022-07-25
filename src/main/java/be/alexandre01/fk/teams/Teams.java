package be.alexandre01.fk.teams;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.teams.list.Blue;
import be.alexandre01.fk.teams.list.Green;
import be.alexandre01.fk.teams.list.Orange;
import be.alexandre01.fk.teams.list.Red;
import be.alexandre01.universal.server.utils.locations.Cuboid;
import be.alexandre01.universal.server.utils.locations.Region;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.HashMap;

public class Teams {

    @Getter @Setter
    int maxSlotByTeam = 25;
    @Getter ArrayList<Team> teams = new ArrayList<Team>();
    @Getter HashMap<String, Team> teamsByName = new HashMap<String, Team>();
    public Teams() {
        addTeam(new Blue(generateBaseFromColor("Blue"), this));
        addTeam(new Green(generateBaseFromColor("Green"),this));
        addTeam(new Orange(generateBaseFromColor("Orange"),this));
        addTeam(new Red(generateBaseFromColor("Red"),this));
    }


    private void addTeam(Team team){
        teams.add(team);
        teamsByName.put(team.getColorName(), team);
    }
    private Base generateBaseFromColor(String color) {
        FKPlugin plugin = FKPlugin.instance;
        return new Base(new Region(plugin.getAutoConfig().getLocation(color + "Cuboid1"),
                plugin.getAutoConfig().getLocation("" + color + "Cuboid2")),
                plugin.getAutoConfig().getLocation(color + "Spawn"),
                plugin.getAutoConfig().getLocation(color + "Core"), color);
    }
}
