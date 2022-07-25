package be.alexandre01.fk.sessions.game.finish;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.universal.server.SpigotPlugin;
import be.alexandre01.universal.server.session.Session;

public class FinishSession extends Session<FKPlayer> {
    public FinishSession(String name) {
        super(name, true);
    }

    @Override
    protected void start(SpigotPlugin base) {
        FKPlugin instance = FKPlugin.instance;

        instance.setCurrentSession(this);
    }
}
