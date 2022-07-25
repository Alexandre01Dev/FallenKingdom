package be.alexandre01.fk.sessions.waiting.runnable;

import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.fk.sessions.waiting.WaitingSession;
import be.alexandre01.fk.teams.Team;

public class WaitingRunnable implements Runnable {

    WaitingSession session;

    Team team;

    public WaitingRunnable(WaitingSession session) {
        this.session = session;
    }
    @Override
    public void run() {
        for (FKPlayer player : session.getPlayers()) {
            player.sendActionBar("§7§lEn attentes de tout le monde... §cC'est long §e:D");
        }
    }
}

