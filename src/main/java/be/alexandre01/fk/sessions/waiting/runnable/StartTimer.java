package be.alexandre01.fk.sessions.waiting.runnable;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.sessions.waiting.WaitingSession;
import be.alexandre01.universal.server.SpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class StartTimer implements Runnable {
    int i = 30;
    FKPlugin fkPlugin;
    WaitingSession session;

    public StartTimer(FKPlugin fkPlugin, WaitingSession session) {
        this.fkPlugin = fkPlugin;
        this.session = session;
    }

    @Override
    public void run() {
        try {
            if (i == 25) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(7, 3, 10, ChatColor.AQUA + "§l" + 5, "");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.DIG_GRASS, 1, 1);
                });
                return;
            }
            if (i == 24) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(7, 3, 10, ChatColor.DARK_AQUA + "§l" + 4,"");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.BURP, 1, 1);
                });
                return;
            }
            if (i == 23) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(7, 3, 10, ChatColor.GREEN + "§l" + 3,"");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.ANVIL_LAND, 1, 1);
                });
                return;
            }
            if (i == 22) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(7, 3, 10, ChatColor.GOLD + "§l" + 2,"");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);
                });
                return;
            }
            if (i == 21) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(7, 3, 10, ChatColor.RED + "§l" + 1,"");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.CAT_MEOW, 1, 1);
                });
                return;
            }

            if (i == 15) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(7, 3, 10, ChatColor.RED + "§l.","");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.WOOD_CLICK, 1, 1);
                });

                return;
            }

            if (i == 14) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(7, 3, 10, ChatColor.RED + "§l..","");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.WOOD_CLICK, 1, 0.8f);
                });
                return;
            }
            if (i == 13) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(7, 3, 30, ChatColor.RED + "§l...","");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.WOOD_CLICK, 1, 0.6f);
                });
                return;
            }

            if (i == 11) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(5, 15, 10, ChatColor.RED + "¯\\\\_(ツ)_/¯", "Il parait que le dev a mal fait son bouleau ");
                    fkPlayer.sendMessage("§c§l" + "Quel saligot je vous jure.");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.VILLAGER_NO, 1, 1f);
                });
                return;
            }
            if (i == 5) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(5, 15, 10, ChatColor.RED + "¯\\\\_(ツ)_/¯", "BON...");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.VILLAGER_NO, 1, 1f);
                });
                return;
            }
            if (i == 3) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(5, 15, 10, ChatColor.RED + " 3 ","");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.EXPLODE, 1, 1.9f);
                });
                return;
            }

            if (i == 2) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(5, 15, 10, ChatColor.GOLD + " 2 ","");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.EXPLODE, 1, 1.5f);
                });
                return;
            }
            if (i == 1) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.sendTitle(5, 15, 10, ChatColor.GREEN + " 1 ", "§a :) ");
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.EXPLODE, 1, 1f);
                });
                return;
            }

            if (i == 0) {
                fkPlugin.getPlayers().forEach(fkPlayer -> {
                    fkPlayer.playSound(fkPlayer.getLocation(), Sound.LEVEL_UP, 1, 1f);
                });
                Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPlugin.getInstance(),() -> {
                    session.finish();
                });
                if (session.getStartTimer() != null) {
                    session.getStartTimer().cancel();
                }
            }
        }finally {
            i--;
        }
    }



}
