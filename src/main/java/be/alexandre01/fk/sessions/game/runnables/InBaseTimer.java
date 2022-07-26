package be.alexandre01.fk.sessions.game.runnables;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.fk.sessions.game.GameSession;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class InBaseTimer implements Runnable {
    int i = 16;
    FKPlugin fkPlugin;
    GameSession session;

    HashMap<Player, Base> inBase = new HashMap<>();

    public InBaseTimer(FKPlugin fkPlugin, GameSession session) {
        this.fkPlugin = fkPlugin;
        this.session = session;
    }

    @Override
    public void run() {
        for (FKPlayer player : fkPlugin.getPlayers()) {
            Location location = player.getLocation();
            Base c = null;
            for(Base base : FKPlugin.instance.getBases()){
                if(base.isIn(location)){
                    c = base;
                   break;
                }
            }

            Base d = inBase.get(player.getPlayer());

            if(d != c){
                if(d == null){
                   player.sendMessage("-------------------");
                     player.sendMessage(ChatColor.GREEN + "Vous êtes dans la base " + c.getTeam().getColorName());
                   player.sendMessage("-------------------");

                   if(player.getTeam() == c.getTeam()){
                       player.sendMessage(ChatColor.GREEN + "Ca fait du bien d'être à nouveau chez soi");
                       player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.3f, 1.9f);
                   }

                   c.getCore().getBossBar().refreshPacket(player.getPlayer(),c.getCore());
                }
                else{
                    player.sendMessage("-------------------");
                    player.sendMessage(ChatColor.RED + "Vous avez quitté la base " + d.getTeam().getColorName());
                    player.sendMessage("-------------------");
                    d.getCore().getBossBar().refreshPacket(player.getPlayer(),d.getCore());
                }
                inBase.put(player.getPlayer(),c);
            }
        }
    }



}
