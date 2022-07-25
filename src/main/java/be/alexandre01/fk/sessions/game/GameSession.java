package be.alexandre01.fk.sessions.game;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.base.core.Core;
import be.alexandre01.fk.base.core.listener.CoreListener;
import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.fk.scoreboards.GameScoreboard;
import be.alexandre01.fk.scoreboards.WaitingScoreboard;
import be.alexandre01.fk.sessions.game.damages.DamageByBlock;
import be.alexandre01.fk.sessions.game.damages.DamageByCause;
import be.alexandre01.fk.sessions.game.damages.DamageByEntity;
import be.alexandre01.fk.sessions.game.damages.UniversalDamage;
import be.alexandre01.fk.sessions.game.targetting.TargetRunnable;
import be.alexandre01.fk.teams.Team;
import be.alexandre01.universal.server.SpigotPlugin;
import be.alexandre01.universal.server.events.factories.IEvent;
import be.alexandre01.universal.server.events.players.IPlayerEvent;
import be.alexandre01.universal.server.packets.ui.bossbar.BossBar;
import be.alexandre01.universal.server.packets.ui.bossbar.BossBarManagerTask;
import be.alexandre01.universal.server.session.Session;
import be.alexandre01.universal.server.session.listeners.examples.PlayerDamageListener;
import lombok.Getter;
import lombok.With;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class GameSession extends Session<FKPlayer> {
    int i = 0;

    @Getter HashMap<EnderCrystal, Core> cores = new HashMap<>();

    @Getter HashMap<String,BossBar> bossBars = new HashMap<>();

    @Getter
    BukkitTask targetTask;

    TargetRunnable targetRunnable;

    UniversalDamage universalDamage = new UniversalDamage();
    public GameSession(String name) {
        super(name, true);
    }

    @Override
    protected void start(SpigotPlugin base) {
        FKPlugin instance = FKPlugin.instance;

        for (World world : Bukkit.getWorlds()){
            for(Entity entity : world.getEntities()){
                if(entity instanceof EnderCrystal){
                    entity.remove();
                }
                if(entity instanceof LivingEntity){
                    if(!(entity instanceof Player)){
                        entity.remove();
                    }

                }
            }
        }
        instance.setCurrentSession(this);

        getListenerManager().registerEvent(new CoreListener());


        targetRunnable = new TargetRunnable();

        targetTask = Bukkit.getScheduler().runTaskTimer(base, targetRunnable, 10, 12);

        getListenerManager().registerEvent(new IEvent<BlockBreakEvent>() {
            @Override
            public void onEvent(BlockBreakEvent event) {
                FKPlayer fkPlayer = getCustomPlayer(event.getPlayer());
                if(fkPlayer.getTeam() == null) return;

                if(!fkPlayer.getTeam().getBase().isIn(event.getBlock().getLocation())){
                    event.setCancelled(true);
                }
            }
        });
        getListenerManager().registerEvent(new IEvent<BlockPlaceEvent>() {
            @Override
            public void onEvent(BlockPlaceEvent event) {
                FKPlayer fkPlayer = getCustomPlayer(event.getPlayer());
                if(fkPlayer.getTeam() == null) return;

                if(!fkPlayer.getTeam().getBase().isIn(event.getBlock().getLocation())){
                    event.setCancelled(true);
                }
            }
        });

        getListenerManager().registerEvent(new IEvent<EntityDamageByEntityEvent>() {
            @Override
            public void onEvent(EntityDamageByEntityEvent event) {
                if(event.getEntity() instanceof Wither){
                    event.setCancelled(true);
                }
            }
        });
        getListenerManager().registerEvent(new IEvent<EntityDamageEvent>() {
            @Override
            public void onEvent(EntityDamageEvent event) {
                if(event.getEntity() instanceof Wither){
                    event.setCancelled(true);
                }
            }
        });
        getListenerManager().registerEvent(new IEvent<CreatureSpawnEvent>() {
            @Override
            public void onEvent(CreatureSpawnEvent event) {
                if(event.getEntity().getType() == EntityType.WITHER){
                   return;
                }

                if(Base.isInBase(event.getLocation())){
                    event.setCancelled(true);
                }
                if (event.getEntity().getType() != EntityType.CREEPER) {
                    if (i < 4) {
                        i++;
                        return;
                    } else {
                        event.setCancelled(true);
                        event.getEntity().getWorld().spawn(event.getEntity().getLocation(), Creeper.class);
                        i = 0;
                    }
                    return;
                }
                Creeper creeper = (Creeper) event.getEntity();
                creeper.setPowered(true);
            }
        });
        getListenerManager().registerEvent(new IEvent<EntityTargetLivingEntityEvent>() {
            @Override
            public void onEvent(EntityTargetLivingEntityEvent event) {
                if(event.getTarget() == null){
                    return;
                }
                if(Base.isInBase(event.getTarget().getLocation())){
                    event.setCancelled(true);
                    return;
                }

                if(event.getEntity() instanceof Creature){
                    Creature creature = (Creature) event.getEntity();
                    targetRunnable.getTargetters().add(creature);
                }
            }
        });

        getListenerManager().registerEvent(new IEvent<EntityDamageByEntityEvent>() {
            @Override
            public void onEvent(EntityDamageByEntityEvent event) {
                if(event.getEntity() instanceof Wither){
                    event.setCancelled(true);
                }
            }
        });

      //  getListenerManager().registerEvent(new DamageByBlock(universalDamage));
       // getListenerManager().registerEvent(new DamageByCause(universalDamage));
        getListenerManager().registerEvent(new DamageByEntity(universalDamage));



        for(Team team : instance.getTeams().getTeams()){
            Core core = team.getBase().getCore();
            core.spawn();
            BossBar bossBar = new BossBar("§cVie "+ core.health,100);
            BossBarManagerTask task = getSpigotPlugin().getBossBarManagerTask();
            task.addBossBar(team.colorName,bossBar);
            bossBars.put(team.colorName,bossBar);
            //send bossbar

        }
        instance.changeScoreboard(GameScoreboard.class);
    }

    @Override
    public void onAddPlayer(FKPlayer player) {

        if(player.getTeam() != null){
            if(player.getTeam().getBase() != null){
                Base base = player.getTeam().getBase();
                player.teleport(safeLoc(base.getSpawn()));

            }
        }else {
            removePlayer(player);
            FKPlugin.instance.getSpectatorSession().addPlayer(player);
            player.teleport(FKPlugin.instance.getAutoConfig().getLocation("Spawn"));
        }
    }

    public Location safeLoc(Location location){

        while (location.getBlock().getType() != Material.AIR){
            location.add(0,1,0);
        }
        return location;
    }
}
