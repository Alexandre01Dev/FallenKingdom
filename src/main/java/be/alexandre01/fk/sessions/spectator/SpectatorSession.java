package be.alexandre01.fk.sessions.spectator;

import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.universal.server.SpigotPlugin;
import be.alexandre01.universal.server.events.factories.IEvent;
import be.alexandre01.universal.server.events.players.IPlayerEvent;
import be.alexandre01.universal.server.session.Session;
import be.alexandre01.universal.server.session.listeners.examples.CancelAllDamage;
import be.alexandre01.universal.server.session.listeners.examples.CancelPVP;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SpectatorSession extends Session<FKPlayer> {

    public SpectatorSession(String name) {
        super(name, false);
    }

    @Override
    protected void start(SpigotPlugin base) {
        getListenerManager().registerEvent(new CancelAllDamage(this));
        getListenerManager().registerEvent(new IEvent<EntityDamageByEntityEvent>() {
            @Override
            public void onEvent(EntityDamageByEntityEvent event) {
                if (event.getDamager() instanceof Player) {
                    if(containsPlayer((Player) event.getDamager()))
                        event.setCancelled(true);
                }
            }
        });
    }

    @Override
    protected void onAddPlayer(FKPlayer player) {
        player.setGameMode(GameMode.SPECTATOR);
    }
}