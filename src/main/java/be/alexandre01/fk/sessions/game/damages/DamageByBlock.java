package be.alexandre01.fk.sessions.game.damages;

import be.alexandre01.universal.server.events.factories.IEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;

public class DamageByBlock extends IEvent<EntityDamageByBlockEvent> {
    UniversalDamage universalDamage;

    public DamageByBlock(UniversalDamage damage) {
        this.universalDamage = damage;
    }

    @Override
    public void onEvent(EntityDamageByBlockEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if(player.getHealth()-event.getDamage() <= 0){
                universalDamage.onKill(player);
                player.setHealth(10);
                event.setCancelled(true);
            }
        }
    }

}
