package be.alexandre01.fk.teams.list;

import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.teams.Team;
import be.alexandre01.fk.teams.Teams;
import be.alexandre01.universal.server.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Green extends Team {
    public Green(Base base, Teams teams) {
        super(new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.GREEN).build(false), "§aVert", ChatColor.GREEN,"Y en a qui mangerons des brocolis ce soir, les verts ont gagnés", "On en connait certains qui sont vert de jalousie, ils sont désormais éliminés !",base,teams);
    }
}
