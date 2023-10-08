package be.alexandre01.fk.scoreboards;

import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.universal.server.packets.ui.scoreboard.ScoreboardImpl;

public class WaitingScoreboard extends ScoreboardImpl<FKPlayer> {

    // DNSpigotAPI dnSpigotAPI;

    String team = "NA";

    public WaitingScoreboard() {
        // this.dnSpigotAPI = dnSpigotAPI;

    }

    @Override
    public void setPlayer(FKPlayer player) {
        super.setPlayer(player);
        this.team = player.getTeam().getColorName();
    }

    @Override
    protected void reloadData() {
        if(getPlayer().getTeam() != null){
            team = getPlayer().getTeam().getColorName();
        }
    }

    @Override
    protected void setLines(String ip) {
        objectiveSign.setDisplayName("§c§lFK Universe");

        objectiveSign.setLine(0, "§1");
        objectiveSign.setLine(1, "§2 " );
        objectiveSign.setLine(2, "§f✸ §fCompte: §b" + player.getName());
        objectiveSign.setLine(3, "§3  ");
        objectiveSign.setLine(4, "§f✸ §fTon équipe: "+ team);
        objectiveSign.setLine(5, "§e");
        objectiveSign.setLine(6, "§f✸ §fEs-tu §lbg ?: "+ player.getBgFinal());
        // objectiveSign.setLine(9, "§7● §8[§c----§7---§8] §e§l0.0%");
        objectiveSign.setLine(7, "§0 ");
        objectiveSign.setLine(8, "§6 ");
        // objectiveSign.setLine(12, "§7● §fServeur: §c" + dnSpigotAPI.getProcessName());
        objectiveSign.setLine(9, "§9 ");
        objectiveSign.setLine(10, ip);

        objectiveSign.updateLines();
    }
}
