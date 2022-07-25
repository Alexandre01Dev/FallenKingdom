package be.alexandre01.fk.scoreboards;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.fk.teams.Team;
import be.alexandre01.universal.server.packets.ui.scoreboard.ScoreboardImpl;
import be.alexandre01.universal.server.player.TitleImpl;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameScoreboard extends ScoreboardImpl<FKPlayer> {

    // DNSpigotAPI dnSpigotAPI;
    FKPlugin fkPlugin;

    String team = "NA";
    String coeurHP = "NA";
    List<Team> otherTeam;
    String[] suppLine;

    public GameScoreboard() {
        this.fkPlugin = FKPlugin.instance;
    }

    @Override
    public void setPlayer(FKPlayer player) {
        super.setPlayer(player);
        otherTeam = FKPlugin.instance.getTeams().getTeams().stream().filter(t -> t != player.getTeam()).collect(Collectors.toList());
        suppLine = new String[otherTeam.size()];
    }

    @Override
    protected void reloadData() {

        Team t = getPlayer().getTeam();
        if(t != null){
            Base base = t.getBase();
            if(base != null){
                coeurHP = base.getCore().getHealth() + " §c❤";
            }
            team = t.getColorName();
        }


        for (int i = 0; i < otherTeam.size(); i++) {
            Team team = otherTeam.get(i);
            if(team != player.getTeam())
                suppLine[i] = team.getColorName()+ "§7 : " + team.getBase().getCore().getHealth() + "§c ❤";
        }
    }

    @Override
    protected void setLines(String ip) {
        objectiveSign.setDisplayName("§c§lFK Universe");
        try {
            objectiveSign.setLine(0, "§1");
            objectiveSign.setLine(1, "§2 " );
            objectiveSign.setLine(2, "§f✸ §fCompte: §b" + player.getName());
            objectiveSign.setLine(3, "§3  ");

            objectiveSign.setLine(4, "§f✸ §fTon équipe: "+ team);
            objectiveSign.setLine(5, "§e");

            objectiveSign.setLine(6, "§f✸ Ton coeur: "+  coeurHP);

            // objectiveSign.setLine(9, "§7● §8[§c----§7---§8] §e§l0.0%");
            objectiveSign.setLine(7, "§5 ");
            objectiveSign.setLine(8, "§6✸ Les autres là:");
            // objectiveSign.setLine(12, "§7● §fServeur: §c" + dnSpigotAPI.getProcessName());
            for (int i = 0; i < suppLine.length; i++) {
                objectiveSign.setLine(i+9, suppLine[i]);
            }
            objectiveSign.setLine(19, "§9 ");
            objectiveSign.setLine(20, ip);
            objectiveSign.updateLines();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
