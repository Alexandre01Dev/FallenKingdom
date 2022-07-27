package be.alexandre01.fk.teams.list;

import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.teams.Team;
import be.alexandre01.fk.teams.Teams;
import be.alexandre01.universal.server.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Orange extends Team {
    public Orange(Base base, Teams teams) {
        super(new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.ORANGE).build(false), "§6Orange", ChatColor.GOLD,"§aOn attendeait rien §6d'eux§e, mais ils §6§lont gagnés", "La récolte ne s'est pas bien passé, les oranges sont éliminés !" ,base,teams);
    }
}
