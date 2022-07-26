package be.alexandre01.fk.players;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.sessions.waiting.WaitingSession;
import be.alexandre01.fk.teams.Team;
import be.alexandre01.fk.teams.waiting.inventories.TeamSelector;
import be.alexandre01.universal.server.packets.ui.bossbar.BossBar;
import be.alexandre01.universal.server.player.BasePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class FKPlayer extends BasePlayer {


    @Getter
    private Team team;
    @Getter @Setter
    private boolean isInGame;

    @Getter private boolean isDead;

    private BossBar bossBar;
    @Getter
    private final ArrayList<Join> joins = new ArrayList<Join>();
    @Getter
    private final ArrayList<Leave> leaves = new ArrayList<Leave>();
    public final String[] bg = {"§b§l OUI !","§e§lPEUT-ETRE","§cGIGA CHAD","§7§lAH QUE NON !"};
    @Getter final String[] customKill = new String[]{
            "§7Aie ! §c%attacker% §7as tordu le coup de §4%victim%",
            "§4%victim% §7a fait une grise d'angoisse, faute à §c%attacker%",
            "§4%victim% §7a été tué par §c%attacker%",
            "§4%victim% §7s'est fait croqué par §c%attacker%",
            "§4%victim% §7est tombé dans les griffes de §c%attacker%",
            "§7Malheureusement pour notre cher §4%victim%, §c%attacker% l'a tué",
            "§4%victim% §7s'est fait dans son froc par §c%attacker%",
            "§4%victim% §7a la rage, il a été tué par §c%attacker%",
            "§4%victim% §7a été tué par le tristement célèbre §c%attacker%",
            "§4%victim% §7a été passé à la friteuse par §c%attacker%",
            "§c%attacker% §7a guillotiné §4%victim%",
            "§c%attacker% §7a démonté §4%victim%",
            "§c%attacker% §7a donné un cadeau explosif a §4%victim%"
    };

    @Getter public String bgFinal;

    public FKPlayer(Player player) {
        super(player);

        int i = (int) (Math.random() * 100);

        if(i < 70){
            bgFinal = bg[0];
        }else if(i < 90) {
            bgFinal = bg[1];
        }else if(i < 97) {
            bgFinal = bg[2];
        }else{
            bgFinal = bg[3];
        }
        whenLeave(new Leave() {
            @Override
            public void doWhen(FKPlayer player) {
                if(player.getTeam() != null){
                    player.getTeam().removePlayer(player);
                }
                joins.clear();
                leaves.clear();
            }
        });
    }

    public void whenJoin(Join join){
        this.joins.add(join);
    }

    public void whenLeave(Leave leave){
        this.leaves.add(leave);
    }

    public void setTeam(Team team) {

        FKPlugin plugin = FKPlugin.instance;
        TeamSelector teamSelector = plugin.getWaitingSession().getTeamSelector();
        Team oldTeam = null;
        if(this.team != null){
            oldTeam = this.team;
            if(this.team == team){
                return;
            }
            this.team.removePlayer(this);
        }




        this.team = team;
        if(team.getTeams().getMaxSlotByTeam() > team.getPlayers().size()){
            team.addPlayer(this);
            sendTitle(10,10,10,"§9YOUPI","Vous faites désormais partie de l'équipe " + team.getColorName());
            playSound(getLocation(), Sound.ORB_PICKUP, 1, 1);
            if(!teamSelector.isDestroyed){
                Inventory inventory = teamSelector.getInv();
                teamSelector.generateLores(inventory.getItem(teamSelector.getTeams().getTeams().indexOf(team)), team);
            }
            if(oldTeam != null){
                if(!teamSelector.isDestroyed){
                    Inventory inventory = teamSelector.getInv();
                    teamSelector.generateLores(inventory.getItem(teamSelector.getTeams().getTeams().indexOf(this.team)), this.team);
                }
            }

            setNameTag(getPlayer(), team.getClass().getSimpleName(), team.getColor().toString(),"");

        }else {
            sendMessage("§cVous ne pouvez pas rejoindre l'équipe " + team.getColorName() + " car elle est pleine");
        }
    }
    public interface Leave {
        void doWhen(FKPlayer player);
    }

    public interface Join {
        void doWhen(FKPlayer player);
    }
}
