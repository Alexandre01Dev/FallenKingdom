package be.alexandre01.fk.teams.list;

import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.teams.Team;
import be.alexandre01.fk.teams.Teams;
import be.alexandre01.universal.server.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Red extends Team {
    public Red(Base base, Teams teams) {
        super(new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.RED).build(false), "§cRouge", ChatColor.RED,"§4Rouge de colère§e, §4§lils détruisirent leurs énemies !", "§eCarton pour §4les rouges §e! §4§lIls sont désormais éliminés !",base,teams);
    }
}
