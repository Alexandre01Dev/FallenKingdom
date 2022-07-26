package be.alexandre01.fk.sessions.game.runnables;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.sessions.game.GameSession;
import be.alexandre01.fk.sessions.waiting.WaitingSession;
import be.alexandre01.universal.server.SpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class TitleTimer implements Runnable {
    int i = 16;
    FKPlugin fkPlugin;
    GameSession session;

    public TitleTimer(FKPlugin fkPlugin, GameSession session) {
        this.fkPlugin = fkPlugin;
        this.session = session;
    }

    @Override
    public void run() {
        try {
            if (i == 15) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(7, 40, 10, ChatColor.RED + "FK","Made by §bDreamNetwork");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.WOOD_CLICK, 1, 1);
                });

                return;
            }



            if (i == 11) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(5, 35, 10, ChatColor.RED + "", "Développé en en moins d'une semaine");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.VILLAGER_NO, 1, 1f);
                });
                return;
            }


            if (i == 7) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(5, 15, 10, ChatColor.RED + "", "Donc soyez un minimum tolérant.");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.VILLAGER_YES, 1, 1f);
                });
                return;
            }
            if (i == 3) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(5, 15, 10, ChatColor.GREEN + "Sur ce","Bon jeu");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.LEVEL_UP, 1, 1.9f);
                });
                return;
            }


            if (i == 0) {
                if (session.getTitleTimer() != null) {
                    session.getTitleTimer().cancel();
                }
            }
        }finally {
            i--;
        }
    }



}
