package be.alexandre01.fk.sessions.game;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.base.core.Core;
import be.alexandre01.fk.base.core.listener.CoreListener;
import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.fk.scoreboards.GameScoreboard;
import be.alexandre01.fk.sessions.game.damages.DamageByCause;
import be.alexandre01.fk.sessions.game.damages.DamageByEntity;
import be.alexandre01.fk.sessions.game.damages.UniversalDamage;
import be.alexandre01.fk.sessions.game.runnables.InBaseTimer;
import be.alexandre01.fk.sessions.game.runnables.TitleTimer;
import be.alexandre01.fk.sessions.game.targetting.TargetRunnable;
import be.alexandre01.fk.sessions.game.time.DayTime;
import be.alexandre01.fk.teams.Team;
import be.alexandre01.universal.server.SpigotPlugin;
import be.alexandre01.universal.server.events.factories.IEvent;
import be.alexandre01.universal.server.packets.ui.bossbar.BossBar;
import be.alexandre01.universal.server.packets.ui.bossbar.BossBarManagerTask;
import be.alexandre01.universal.server.session.Session;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameSession extends Session<FKPlayer> {
    int i = 0;

    @Getter
    HashMap<EnderCrystal, Core> cores = new HashMap<>();

    @Getter
    HashMap<String, BossBar> bossBars = new HashMap<>();

    @Getter
    BukkitTask targetTask;

    TargetRunnable targetRunnable;

    UniversalDamage universalDamage = new UniversalDamage();

    @Getter
    private DayTime dayTime = new DayTime();

    @Getter
    private BukkitTask titleTimer ;
    private BukkitTask inBase;


    public GameSession(String name) {
        super(name, true);
    }

    @Override
    protected void start(SpigotPlugin base) {
        FKPlugin instance = FKPlugin.instance;

        titleTimer =  Bukkit.getScheduler().runTaskTimer(getSpigotPlugin(),new TitleTimer(FKPlugin.instance,this),0,20);
        inBase =  Bukkit.getScheduler().runTaskTimer(getSpigotPlugin(),new InBaseTimer(FKPlugin.instance,this),0,20);

        for (World world : Bukkit.getWorlds()) {
            world.setFullTime(0);
            for (Entity entity : world.getEntities()) {
                if (entity instanceof EnderCrystal) {
                    entity.remove();
                }
                if (entity instanceof LivingEntity) {
                    if (!(entity instanceof Player)) {
                        entity.remove();
                    }

                }
            }
        }

        for(Player player : Bukkit.getOnlinePlayers()){

            player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
        }

        instance.setCurrentSession(this);

        getListenerManager().registerEvent(new CoreListener());


        targetRunnable = new TargetRunnable();

        targetTask = Bukkit.getScheduler().runTaskTimer(base, targetRunnable, 10, 12);

        Material[] m = new Material[]{
                Material.WOOD,
                Material.COAL_ORE,
                Material.IRON_ORE,
                Material.GOLD_ORE,
                Material.DIAMOND_ORE,
                Material.LAPIS_ORE,
                Material.EMERALD_ORE,
                Material.REDSTONE_ORE,
                Material.LOG,
                Material.LOG_2,
                Material.CARROT,
                Material.POTATO,
                Material.MELON_BLOCK,
                Material.PUMPKIN,
                Material.SUGAR_CANE,
                Material.HAY_BLOCK,
                Material.NETHER_WARTS,
                Material.IRON_BLOCK
        };
        List<Material> materials = Arrays.asList(m);
        Bukkit.getPluginManager().registerEvents(new Listener(){
            @EventHandler
            public void onEvent(BlockBreakEvent event) {
                FKPlayer fkPlayer = getCustomPlayer(event.getPlayer());
                if (fkPlayer.getTeam() == null) return;


                if(materials.contains(event.getBlock().getType())){
                   return;
                }

                if (!fkPlayer.getTeam().getBase().isIn(event.getBlock().getLocation())) {
                    event.setCancelled(true);
                }
            }
        },getSpigotPlugin());
       Bukkit.getPluginManager().registerEvents(new Listener() {
           @EventHandler
           public void onEvent(BlockPlaceEvent event) {
               FKPlayer fkPlayer = getCustomPlayer(event.getPlayer());
               if (fkPlayer.getTeam() == null) return;

               if (!fkPlayer.getTeam().getBase().isIn(event.getBlock().getLocation())) {
                   event.setCancelled(true);
               }

               if (dayTime.getDay() >= 3) {
                   if (event.getBlock().getType() == Material.TNT) {
                       TNTPrimed tnt = event.getBlock().getWorld().spawn(event.getBlock().getLocation().add(0.5, 0, 0.5), TNTPrimed.class);
                       tnt.setCustomName("§cATTENTION ÇA VA PÉTER !");
                       tnt.setCustomNameVisible(true);
                   }
               }
           }
       },getSpigotPlugin());



        getListenerManager().registerEvent(new IEvent<EntityExplodeEvent>() {
            @Override
            public void onEvent(EntityExplodeEvent event) {
                if (dayTime.getDay() < 3) {
                    for (Block block : event.blockList()) {
                        if (Base.isInBase(block.getLocation())) {
                            event.blockList().remove(block);
                        }
                    }
                }

            }
        });

       Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onEvent(PlayerInteractEvent event) {
                FKPlayer fkPlayer = getCustomPlayer(event.getPlayer());
                if (fkPlayer.getTeam() == null) return;

                Location loc;

                if (event.getClickedBlock() != null) {
                    loc = event.getClickedBlock().getLocation();
                } else {
                    loc = event.getPlayer().getLocation();
                }

                if (Base.isInBase(loc) && !fkPlayer.getTeam().getBase().isIn(loc)) {
                    if(getDayTime().getDay() >= 3){
                        if(event.getItem() != null){
                            if(event.getItem().getType() == Material.TNT){
                                return;
                            }
                        }

                    }
                    event.setCancelled(true);
                }
            }
        },getSpigotPlugin());

        getListenerManager().registerEvent(new IEvent<EntityDamageByEntityEvent>() {
            @Override
            public void onEvent(EntityDamageByEntityEvent event) {
                if (event.getEntity() instanceof Wither) {
                    event.setCancelled(true);
                }
            }
        });
        getListenerManager().registerEvent(new IEvent<EntityDamageEvent>() {
            @Override
            public void onEvent(EntityDamageEvent event) {
                if (event.getEntity() instanceof Wither) {
                    event.setCancelled(true);
                }
            }
        });
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onEvent(CreatureSpawnEvent event) {
                if (event.getEntity().getType() == EntityType.WITHER) {
                    return;
                }

                if (Base.isInBase(event.getLocation())) {
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
        },getSpigotPlugin());


        getListenerManager().registerEvent(new IEvent<EntityTargetLivingEntityEvent>() {
            @Override
            public void onEvent(EntityTargetLivingEntityEvent event) {
                if (event.getTarget() == null) {
                    return;
                }
                if (Base.isInBase(event.getTarget().getLocation())) {
                    event.setCancelled(true);
                    return;
                }

                if (event.getEntity() instanceof Creature) {
                    Creature creature = (Creature) event.getEntity();
                    targetRunnable.getTargetters().add(creature);
                }
            }
        });


        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onEvent(EntityDeathEvent event) {
                if (event.getEntity() instanceof Creeper) {
                    event.getDrops().add(new ItemStack(Material.TNT));
                }
            }
        },getSpigotPlugin());

        dayTime.getListeners().put(2, new DayTime.DayTimeListener() {
            @Override
            public void onDayChange(int day, DayTime dayTime) {
                dayTime.setStatus("PvP");
                for (FKPlayer fkPlayer : getPlayers()) {
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.ANVIL_USE, 1, 1);
                }
            }
        });

        dayTime.getListeners().put(3, new DayTime.DayTimeListener() {
            @Override
            public void onDayChange(int day, DayTime dayTime) {
                dayTime.setStatus("Assaut");
                Bukkit.broadcastMessage("§c§l L'assaut commence !§l BATTEZ VOUS");
                for (FKPlayer fkPlayer : getPlayers()) {
                    fkPlayer.playSound(fkPlayer.getLocation(),Sound.ENDERDRAGON_GROWL, 1, 1);
                }
            }
        });


        getSpigotPlugin().getServer().getPluginManager().registerEvents(new DamageByCause(universalDamage), getSpigotPlugin());
        getSpigotPlugin().getServer().getPluginManager().registerEvents(new DamageByEntity(universalDamage), getSpigotPlugin());

        // getListenerManager().registerEvent(new DamageByBlock(universalDamage));

        // getListenerManager().registerEvent(new DamageByEntity(universalDamage));


        for (Team team : instance.getTeams().getTeams()) {
            Core core = team.getBase().getCore();
            core.spawn();
            BossBar bossBar = new BossBar("§cVie " + core.health, 100);
            BossBarManagerTask task = getSpigotPlugin().getBossBarManagerTask();
            task.addBossBar(team.colorName, bossBar);
            bossBars.put(team.colorName, bossBar);
            //send bossbar

        }
        instance.changeScoreboard(GameScoreboard.class);
    }

    @Override
    public void onAddPlayer(FKPlayer player) {
        if (player.getTeam() != null) {
            if (player.getTeam().getBase() != null) {
                Base base = player.getTeam().getBase();
                player.teleport(safeLoc(base.getSpawn()));
                player.setHealth(20);
                player.setFoodLevel(20);
                player.getInventory().clear();
                player.getInventory().setHelmet(null);
                player.getInventory().setChestplate(null);
                player.getInventory().setLeggings(null);
                player.getInventory().setBoots(null);
            }
        } else {
            removePlayer(player);
            FKPlugin.instance.getSpectatorSession().addPlayer(player);
            player.teleport(FKPlugin.instance.getAutoConfig().getLocation("Spawn"));
        }
    }

    public Location safeLoc(Location location) {

        while (location.getBlock().getType() != Material.AIR) {
            location.add(0, 1, 0);
        }
        return location;
    }
}
