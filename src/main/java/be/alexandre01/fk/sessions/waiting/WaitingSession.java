package be.alexandre01.fk.sessions.waiting;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.fk.scoreboards.WaitingScoreboard;
import be.alexandre01.fk.sessions.game.GameSession;
import be.alexandre01.fk.sessions.waiting.commands.StartCommand;
import be.alexandre01.fk.sessions.waiting.runnable.StartTimer;
import be.alexandre01.fk.sessions.waiting.runnable.WaitingRunnable;
import be.alexandre01.fk.teams.Team;
import be.alexandre01.fk.teams.Teams;
import be.alexandre01.fk.teams.waiting.commands.ForceTeam;
import be.alexandre01.fk.teams.waiting.inventories.TeamSelector;
import be.alexandre01.universal.server.SpigotPlugin;
import be.alexandre01.universal.server.events.factories.IEvent;
import be.alexandre01.universal.server.packets.ui.scoreboard.PersonalScoreboard;
import be.alexandre01.universal.server.packets.ui.scoreboard.ScoreboardImpl;
import be.alexandre01.universal.server.session.Session;
import be.alexandre01.universal.server.session.inventory.item.SessionItem;
import be.alexandre01.universal.server.session.listeners.examples.*;
import be.alexandre01.universal.server.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class WaitingSession extends Session<FKPlayer> {
    String[] customMessage = new String[]{"§e§nOn l'attendait tous !", "§7§nIl ne pouvait pas dépécher un peu ?", "§eLe voilà enfin !", "§cIl est §aprésent !","§cIl est là pour vous donner une raclée","§c§l§nIl va vous pulvériser !","§5§l§nUne aura menaçante dégage de lui.","§3§nIl est un peu timide, soutenez le.","§4§nNe le faites pas cabler","§4§nTape ou il te tape.", "§9Lui ça se voit il est abo a OtaquinTV sur §dtiktok"};

    @Getter TeamSelector teamSelector;


    SessionItem selectorItem;

    BukkitTask actionBar;

    @Getter BukkitTask startTimer;



    @Getter
    HashMap<String, Team> toOverride = new HashMap<>();

    public WaitingSession(String name) {
        super(name, true);
    }

    @Override
    protected void start(SpigotPlugin base) {
        teamSelector = new TeamSelector();
        for(FKPlayer fkPlayer : FKPlugin.instance.getPlayers()){
            addPlayer(fkPlayer);
        }

        actionBar = Bukkit.getScheduler().runTaskTimer(base, new WaitingRunnable(this), 0, 20);
        base.registerCommand("start", new StartCommand("start"));
        base.registerCommand("force", new ForceTeam("force",this));

        Bukkit.getPluginManager().registerEvents(new Listener(){
            @EventHandler
            public void onDamage(EntityDamageEvent e){
                if(isStarted()){
                    e.setCancelled(true);
                }
            }
        },getSpigotPlugin());
        Bukkit.getPluginManager().registerEvents(new Listener(){
            @EventHandler
            public void onDamage(PlayerInteractEvent e){
                if(isStarted()){
                    e.setCancelled(true);
                }
            }
        },getSpigotPlugin());
        getListenerManager().registerEvent(new IEvent<CreatureSpawnEvent>() {
            @Override
            public void onEvent(CreatureSpawnEvent event) {
                if(isStarted()){
                    event.setCancelled(true);
                }
            }
        });
        getListenerManager().registerEvent(new CancelBlockBreak(this));
        getListenerManager().registerEvent(new CancelBlockPlace(this));
        getListenerManager().registerEvent(new CancelFoodDecrease(this));
        getListenerManager().registerEvent(new CancelDropInventory(this));
        getListenerManager().registerEvent(new CancelClickInventory(this));



        getListenerManager().registerEvent(new IEvent<EntityTargetLivingEntityEvent>() {
            @Override
            public void onEvent(EntityTargetLivingEntityEvent event) {
                if(isStarted()){
                    event.setCancelled(true);
                }
            }
        });



        selectorItem = getItemFactory().addItem(new SessionItem("selector",new ItemBuilder(Material.COMPASS).setDisplayName("§e§nChoisissez votre équipe").build(false)),true);
        selectorItem.setItemInteractEvent(event -> {
            teamSelector.openInventory(event.getPlayer());
        });
        FKPlugin.instance.changeScoreboard(WaitingScoreboard.class);
    }

    @Override
    protected void stop(SpigotPlugin base) {
        FKPlugin instance = FKPlugin.instance;
        teamSelector.destroy();
        GameSession gameSession = instance.getGameSession();
        gameSession.processStart();
        Teams teams = instance.getTeams();
        autoAddAll();
        for(Team team : teams.getTeams()){
            team.getPlayers().forEach(gameSession::addPlayer);
        }

        if(getPlayers() != null && getPlayers().size() != 0){
            for (int i = 0; i < getPlayers().size(); i++) {
                removePlayer(getPlayers().get(i));
            }
            for(Player player : Bukkit.getOnlinePlayers()){

                player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
            }

        }

        if(actionBar != null){
            actionBar.cancel();
        }

    }

    public void doStartTimer(){
        FKPlugin fkPlugin = FKPlugin.instance;

        startTimer = Bukkit.getScheduler().runTaskTimerAsynchronously(SpigotPlugin.getInstance(),new StartTimer(fkPlugin,this),0,20);
    }

    @Override
    protected void onAddPlayer(FKPlayer player) {
        Bukkit.broadcastMessage("Le joueur " + player.getName() + " a rejoint la partie.");
        Bukkit.broadcastMessage(customMessage[(int)(Math.random()*customMessage.length)]);
        FKPlugin instance = FKPlugin.instance;
        autoAddPlayer(player,instance.getTeams(),instance);
        if(toOverride.containsKey(player.getName())){
            player.setTeam(toOverride.get(player.getName()));
            toOverride.remove(player.getName());
        }

            player.teleport(instance.getAutoConfig().getLocation("Spawn"));

        player.getInventory().clear();
        player.getInventory().setItem(4, selectorItem.getItemStack());
        player.getInventory().setHeldItemSlot(4);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);

        System.out.println("Le joueur " + player.getName() + " a rejoint la partie.");
    }

    @Override
    protected void onRemovePlayer(FKPlayer player) {
    }

    public void autoAddAll(){
        FKPlugin instance = FKPlugin.instance;
        Teams teams = instance.getTeams();
        ArrayList<Team> teamList = teams.getTeams();
        for(FKPlayer fkPlayer : instance.getPlayers()){
            autoAddPlayer(fkPlayer,teams,teamList,instance);
        }
    }

    public void autoAddPlayer(FKPlayer fkPlayer,Teams teams,FKPlugin instance){
        ArrayList<Team> teamList = new ArrayList<>(teams.getTeams());
        Collections.shuffle(teamList);
        autoAddPlayer(fkPlayer,teams,teamList,instance);
    }
    public void autoAddPlayer(FKPlayer fkPlayer,Teams teams,ArrayList<Team> teamList,FKPlugin instance){

        if(fkPlayer.getTeam() == null){
            Integer i = 0;
            while (teamList.get(i).getPlayers().size() >= teams.getMaxSlotByTeam()){
                i++;
                if(teamList.size() < i){
                    instance.getSpectatorSession().addPlayer(fkPlayer);
                    fkPlayer.sendMessage("§cVous avez été désigné comme spectateur, il n'y avait plus d'équipe disponible.");
                    i = null;
                    break;
                }
            }
            if(i != null){
                fkPlayer.setTeam(teamList.get(i));
            }
        }
    }
}
