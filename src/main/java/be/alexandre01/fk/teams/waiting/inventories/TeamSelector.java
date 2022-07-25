package be.alexandre01.fk.teams.waiting.inventories;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.teams.Team;
import be.alexandre01.fk.teams.Teams;
import be.alexandre01.fk.teams.waiting.inventories.listener.ClickTeam;
import be.alexandre01.universal.server.SpigotPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TeamSelector {
    @Getter private Inventory inv;

    public boolean isDestroyed = false;


    ClickTeam clickTeam = new ClickTeam(this);

    boolean hasBeenRegistered = false;
    @Getter Teams teams;

    public TeamSelector() {
        update();
        Bukkit.getPluginManager().registerEvents(clickTeam, SpigotPlugin.getInstance());
    }

    public void update() {
        FKPlugin plugin = FKPlugin.instance;
        inv = Bukkit.createInventory(null, 9, "§7§lChoisissez votre équipe");
        teams = plugin.getTeams();
        int i = 0;
        for (Team team : teams.getTeams()) {
            ItemStack item = team.getColorItem();
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§7§l" + team.getColorName());
            item.setItemMeta(meta);
            inv.setItem(i, item);
            i++;
        }
    }

    public Team getTeamFromItem(int slot){
        return teams.getTeams().get(slot);
    }
    public ItemStack generateLores(ItemStack itemStack, Team team){
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.getLore();
        if(lore == null){
            lore = new ArrayList<String>();
        }
        lore.clear();
        lore.add("§7§lEquipe : " + team.getColorName());
        lore.add("§7§lSlot : §a" + team.getPlayers().size() + "/" + teams.getMaxSlotByTeam());

        if(teams.getMaxSlotByTeam() <= team.getPlayers().size()){
            lore.add("§c§lAucun place disponible");
        }else {
            lore.add("§a§lTu peux rejoindre");
        }

        itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void openInventory(Player player) {
        if(!hasBeenRegistered){
            int i = 0;
            for (ItemStack item : inv.getContents()) {
                if(item != null){
                    generateLores(item, getTeamFromItem(i));
                }
                i++;
            }
            hasBeenRegistered = true;
        }

        player.openInventory(inv);
    }

    public void destroy(){
        HandlerList.unregisterAll(clickTeam);
        inv.clear();
        isDestroyed = true;
    }
}
