package be.alexandre01.fk.scoreboards;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.fk.teams.Team;
import be.alexandre01.universal.server.packets.ui.scoreboard.ScoreboardImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlankScoreboard extends ScoreboardImpl<FKPlayer> {
    

    public BlankScoreboard() {
    }
    @Override
    protected void reloadData() {
    }

    @Override
    protected void setLines(String ip) {
        objectiveSign.setDisplayName("§c§lFK Universe");

        for (int i = 0; i < 100; i++) {
            objectiveSign.setLine(i, " ");
        }
        objectiveSign.updateLines();
    }
}
