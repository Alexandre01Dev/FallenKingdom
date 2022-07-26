package be.alexandre01.fk.base.core.listener;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.base.core.Core;
import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.fk.sessions.game.GameSession;
import be.alexandre01.universal.server.SpigotPlugin;
import be.alexandre01.universal.server.events.factories.IEvent;
import be.alexandre01.universal.server.packets.ui.bossbar.BossBar;
import be.alexandre01.universal.server.packets.ui.bossbar.BossBarManagerTask;
import be.alexandre01.universal.server.player.TitleImpl;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class CoreListener extends IEvent<EntityDamageByEntityEvent> {
    BossBarManagerTask task;
    public CoreListener() {
        task = SpigotPlugin.getInstance().getBossBarManagerTask();
    }
    @Override
    public void onEvent(EntityDamageByEntityEvent event) {

        if(event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            event.setCancelled(true);
        }


            if(event.getEntity() instanceof EnderCrystal){
                Core core = FKPlugin.instance.getGameSession().getCores().get(event.getEntity());
                if(core != null){
                    FKPlayer player = FKPlugin.instance.getGameSession().getCustomPlayer((Player) event.getDamager());
                    if(player.getTeam() == null){
                        event.setCancelled(true);
                        return;
                    }
                    if(player.getTeam() == core.getBase().getTeam()){
                        event.setCancelled(true);
                        return;
                    }
                    core.setDamage(event.getFinalDamage());

                    if(core.health > 0){
                        event.setCancelled(true);
                        core.getEnderCrystal().playEffect(EntityEffect.HURT);
                        core.loc.getWorld().playSound(core.loc, Sound.EXPLODE, 3, 1);
                        GameSession gameSession = FKPlugin.instance.getGameSession();


                        for(FKPlayer fkPlayer : core.getBase().getTeam().getPlayers()) {
                            fkPlayer.sendTitle(0, 20, 0, "§4§LALERTE", "§cVotre coeur se fait attaquer !");
                            fkPlayer.playSound(fkPlayer.getLocation(),Sound.NOTE_PLING, 1, 1);
                            fkPlayer.sendActionBar("§cVie "+ core.health);
                        }
                        return;
                    }

                    if(core.health <= 0) {
                        event.setCancelled(false);

                        core.getEnderCrystal().playEffect(EntityEffect.FIREWORK_EXPLODE);

                        /*
                        for(FKPlayer all  :  FKPlugin.instance.getPlayers()) {
                            all.sendTitle(20, 40, 20 , "§c§lCOEUR DETRUIT", "Le coeur");

                        }

                         */
                        return;
                    }


                    // DEFEAT

                    // DETECTION WIN
                }
            }

    }
}
