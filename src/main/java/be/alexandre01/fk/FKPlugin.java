package be.alexandre01.fk;


import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.base.core.bossbar.CustomWither;
import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.fk.scoreboards.BlankScoreboard;
import be.alexandre01.fk.scoreboards.WaitingScoreboard;
import be.alexandre01.fk.sessions.game.GameSession;
import be.alexandre01.fk.sessions.spectator.SpectatorSession;
import be.alexandre01.fk.sessions.waiting.WaitingSession;
import be.alexandre01.fk.teams.Teams;
import be.alexandre01.universal.config.auto.AutoConfig;
import be.alexandre01.universal.server.SpigotPlugin;
import be.alexandre01.universal.server.events.factories.IEvent;
import be.alexandre01.universal.server.packets.ui.scoreboard.PersonalScoreboard;
import be.alexandre01.universal.server.packets.ui.scoreboard.ScoreboardImpl;
import be.alexandre01.universal.server.session.Session;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


@Getter
public class FKPlugin extends Session<FKPlayer> {
    public static FKPlugin instance;
    @Getter
    private WaitingSession waitingSession;
    @Getter
    private SpectatorSession spectatorSession;

    private BlankScoreboard blankScoreboard = new BlankScoreboard();

    @Getter
    GameSession gameSession;

    @Getter @Setter
    Class<?> scoreboard;

    @Getter @Setter private Session<FKPlayer> currentSession;
    private AutoConfig autoConfig;

    @Getter
    Teams teams;
    @Getter
    ArrayList<Base> bases = new ArrayList<>();


    public FKPlugin() {
        super("FKPlugin", false);
        autoConfig = new AutoConfig("FallenKingdom.yml",getSpigotPlugin());
        autoConfig.setDefaultLocs("Spawn","BlueCore","BlueCuboid1","BlueCuboid2","BlueSpawn","RedCore","RedCuboid1","RedCuboid2","RedSpawn","OrangeCore","OrangeCuboid1","OrangeCuboid2","OrangeSpawn","BlueCore","BlueCuboid1","BlueCuboid2","GreenCore","GreenCuboid1","GreenCuboid2","GreenSpawn");


    }

    @Override
    protected void start(SpigotPlugin base) {
        super.start(base);
        instance = this;
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

        getSpigotPlugin().getBasePlayerManager().changeBasePlayer(FKPlayer.class);
        getListenerManager().registerEvent(new IEvent<PlayerLoginEvent>() {
            @Override
            public void onEvent(PlayerLoginEvent event) {
                System.out.println(event.getHostname());
            }
        });
        autoConfig.initAutoConfigCommmands("fkloc");
        try {
            teams = new Teams();
        }catch (Exception e){
            e.printStackTrace();
        }

        getSpigotPlugin().getScoreboardManager().setupSchedulers(16,1);
        getSpigotPlugin().getScoreboardManager().startGlowingTask(80,80, TimeUnit.MILLISECONDS);
        getSpigotPlugin().getScoreboardManager().startReloadingTask(1,1, TimeUnit.SECONDS);
        CustomWither.registerEntity();
        waitingSession = new WaitingSession("WaitingSession");
        waitingSession.processStart();
        currentSession = waitingSession;
        gameSession = new GameSession("GameSession");
        spectatorSession = new SpectatorSession("SpectatorSession");
        spectatorSession.processStart();



    }

    public void changeScoreboard(Class<?> scoreboard){
        this.scoreboard = scoreboard;
        for(FKPlayer player : getPlayers()){
            showScoreboard(player);
        }
    }

    public void showScoreboard(FKPlayer fkPlayer){
        try {
            if(scoreboard != null){
                fkPlayer.getPersonalScoreboard().setScoreboardImpl(blankScoreboard);
            }

            fkPlayer.getPersonalScoreboard().setScoreboardImpl((ScoreboardImpl) scoreboard.newInstance());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
         }
    }
    @Override
    protected void onAddPlayer(FKPlayer player) {
        if(!player.getJoins().isEmpty())
            player.getJoins().forEach(join -> join.doWhen(player));

        getCurrentSession().addPlayer(player);

        player.setPersonalScoreboard(new PersonalScoreboard<FKPlayer>(player));
        showScoreboard(player);

        getSpigotPlugin().getScoreboardManager().onLogin(player);
    }

    @Override
    protected void onRemovePlayer(FKPlayer player) {
        getSpigotPlugin().getScoreboardManager().onLogout(player);

        for (int i = 0; i < player.getLeaves().size() ; i++) {
            player.getLeaves().get(i).doWhen(player);
        }
    }
}
