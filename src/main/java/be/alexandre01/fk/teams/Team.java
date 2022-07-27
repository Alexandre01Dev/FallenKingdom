package be.alexandre01.fk.teams;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.fk.teams.waiting.inventories.TeamSelector;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class Team {
    private final static HashMap<UUID,Team> teamsRevive = new HashMap<>();
    public final ItemStack colorItem;
    public final String colorName;

    public final ChatColor color;

    public final String messageOfWin;
    public final String messageOfLose;

    @Setter public boolean isDead = false;


    @Getter private final ArrayList<FKPlayer> players = new ArrayList<FKPlayer>();

    @Getter private final ArrayList<FKPlayer> lastPlayers = new ArrayList<FKPlayer>();

    private final Base base;

    private final Teams teams;

    public Team(ItemStack colorItem, String colorName,ChatColor color, String messageOfWin, String messageOfLose,Base base,Teams teams) {
        this.colorItem = colorItem;
        this.colorName = colorName;
        this.messageOfWin = messageOfWin;
        this.messageOfLose = messageOfLose;

        this.base = base;
        base.setTeam(this);
        this.teams = teams;
        this.color = color;
    }

    public void eliminate(FKPlayer fkPlayer) {
        if(getBase().getCore().isDead()){
            getLastPlayers().remove(fkPlayer);

            if(getLastPlayers().isEmpty()){
                Bukkit.broadcastMessage(getMessageOfLose());
                setDead(true);
                int i = 0;
                Team t = null;
                for(Team team : FKPlugin.instance.getTeams().getTeams()){
                    if(!team.isDead()){
                        i++;
                        t = team;
                    }
                }
                if(i == 1){
                    if(t != null){
                        Bukkit.broadcastMessage(t.getMessageOfWin());
                    }else {
                        Bukkit.broadcastMessage("§c§lFK Universe §7: §c§lEgalité");
                    }
                }
            }
            return;
        }
    }

    public void addPlayer(FKPlayer player){
        players.add(player);
        lastPlayers.add(player);

        player.whenLeave(new FKPlayer.Leave() {
            @Override
            public void doWhen(FKPlayer player) {
                removePlayer(player);
            }
        });
    }

    public void removePlayer(FKPlayer player){
        players.remove(player);
        lastPlayers.remove(player);

        FKPlugin plugin = FKPlugin.instance;
        TeamSelector teamSelector = plugin.getWaitingSession().getTeamSelector();
        if(!teamSelector.isDestroyed){
            Inventory inventory = teamSelector.getInv();
            teamSelector.generateLores(inventory.getItem(teamSelector.getTeams().getTeams().indexOf(this)), this);
        }

        if(player.getTeam() != null && player.getTeam().getLastPlayers().size() == 0){
           eliminate(player);
        }
    }
}
