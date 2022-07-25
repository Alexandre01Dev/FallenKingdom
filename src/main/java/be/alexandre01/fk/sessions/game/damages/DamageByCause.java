package be.alexandre01.fk.sessions.game.damages;

import be.alexandre01.universal.server.events.factories.IEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageByCause extends IEvent<EntityDamageEvent> {
    UniversalDamage universalDamage;

    public DamageByCause(UniversalDamage damage) {
        this.universalDamage = damage;
    }
    @Override
    public void onEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if(player.getHealth()-event.getDamage() <= 0){
                universalDamage.onKill(player);
                event.setCancelled(true);
            }
        }
    }
}
