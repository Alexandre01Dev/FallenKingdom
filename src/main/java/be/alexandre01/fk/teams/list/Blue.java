package be.alexandre01.fk.teams.list;

import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.teams.Team;
import be.alexandre01.fk.teams.Teams;
import be.alexandre01.universal.server.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Blue extends Team {

    public Blue(Base base, Teams teams) {
        super(new ItemBuilder(Material.WOOL).setWoolColor(DyeColor.BLUE).build(false), "§3Bleu", ChatColor.BLUE, "§eIls ont eu une peur §bbleu §emais ils ont §b§lgagné !", "§eLes §bbleu §eon été cuisiné en cordon §bbleu §e! Ils sont §b§ldésormais éliminés !",base,teams);
    }
}
