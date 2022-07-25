package be.alexandre01.fk.teams.waiting.inventories.listener;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.fk.teams.Team;
import be.alexandre01.fk.teams.waiting.inventories.TeamSelector;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickTeam implements Listener {
    private TeamSelector teamSelector;
    private FKPlugin plugin;

    public ClickTeam(TeamSelector teamSelector) {
        this.teamSelector = teamSelector;
        this.plugin = FKPlugin.instance;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (!event.getInventory().equals(teamSelector.getInv())) {
                return;
            }

            event.setCancelled(true);

            Team team = teamSelector.getTeamFromItem(event.getSlot());

            if (team == null) {
                return;
            }


            FKPlayer fkPlayer = plugin.getCustomPlayer(player);
            Team oldTeam = fkPlayer.getTeam();

            fkPlayer.setTeam(team);

          /*  if (oldTeam != null) {
                teamSelector.generateLores(event.getInventory().getItem(teamSelector.getTeams().getTeams().indexOf(oldTeam)), oldTeam);
            }
            teamSelector.generateLores(event.getCurrentItem(), team);*/

            player.closeInventory();
        }

    }

}
