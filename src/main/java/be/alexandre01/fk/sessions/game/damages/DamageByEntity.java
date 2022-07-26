package be.alexandre01.fk.sessions.game.damages;

import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.universal.server.events.factories.IEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageByEntity implements Listener {

    UniversalDamage universalDamage;

    public DamageByEntity(UniversalDamage damage) {
        this.universalDamage = damage;
    }

    @EventHandler
    public void onEvent(EntityDamageByEntityEvent event) {
        if(event.getDamager() == null) return;

        if (event.getEntity() instanceof Player) {
            FKPlayer player = universalDamage.getPlugin().getCustomPlayer((Player) event.getEntity());

            if(player.getTeam() == null){
                event.setCancelled(true);
                return;
            }
            if (event.getDamager() instanceof Player) {

                FKPlayer damager = universalDamage.getPlugin().getCustomPlayer((Player) event.getDamager());
                if(universalDamage.getPlugin().getGameSession().getDayTime().getDay() > 1){
                    damager.getPlayer().sendMessage("§cTu ne peux pas encore attaquer, calmos s'il te plait.");
                    event.setCancelled(true);
                    return;
                }
                if(player.getTeam() == damager.getTeam()){
                    event.setCancelled(true);
                    return; 
                }
                universalDamage.onDamage(player, damager);
            }

            if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
                if(event.getDamager() instanceof EnderCrystal){
                    event.setCancelled(true);
                }
            }


            System.out.println(event.getFinalDamage());
            if ((player.getHealth() - event.getFinalDamage()) <= 0) {
                universalDamage.onKill(player);
                player.setHealth(20);
                event.setCancelled(true);
            }
        }
    }
}
