package be.alexandre01.fk.sessions.game.damages;

import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.universal.server.events.factories.IEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageByEntity extends IEvent<EntityDamageByEntityEvent> {

    UniversalDamage universalDamage;

    public DamageByEntity(UniversalDamage damage) {
        this.universalDamage = damage;
    }

    @Override
    public void onEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            FKPlayer player = universalDamage.getPlugin().getCustomPlayer((Player) event.getEntity());
            if (event.getDamager() instanceof Player) {

                FKPlayer damager = universalDamage.getPlugin().getCustomPlayer((Player) event.getDamager());
                if(player.getTeam() == damager.getTeam()){
                    event.setCancelled(true);
                    return; 
                }
                universalDamage.onDamage(player, damager);
            }
            if (player.getHealth() - event.getDamage() <= 0) {
                universalDamage.onKill(player);
                event.setCancelled(true);
            }
        }
    }
}
