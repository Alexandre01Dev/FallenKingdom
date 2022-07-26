package be.alexandre01.fk.sessions.game.damages;

import be.alexandre01.universal.server.events.factories.IEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageByCause implements Listener {
    UniversalDamage universalDamage;

    public DamageByCause(UniversalDamage damage) {
        this.universalDamage = damage;
    }
    @EventHandler
    public void onEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Bukkit.broadcastMessage(""+event.getDamage());
            if((player.getHealth()-event.getFinalDamage()) <= 0){
                System.out.println("Damage By Cause " + this);
                universalDamage.onKill(player);
                player.setHealth(20);
                event.setCancelled(true);
            }
        }

    }
}
