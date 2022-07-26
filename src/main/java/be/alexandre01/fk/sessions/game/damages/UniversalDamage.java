package be.alexandre01.fk.sessions.game.damages;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.fk.sessions.game.GameSession;
import be.alexandre01.fk.sessions.waiting.WaitingSession;
import be.alexandre01.fk.teams.Team;
import be.alexandre01.universal.server.SpigotPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Random;

public class UniversalDamage {
    private HashMap<Player,Player> lastDamager = new HashMap<>();
    @Getter private FKPlugin plugin;
    public UniversalDamage(){
        plugin = FKPlugin.instance;
    }
    public void onKill(Player player){
        FKPlayer fkPlayer = FKPlugin.instance.getCustomPlayer(player);
        FKPlugin instance = FKPlugin.instance;

        if(getLastDamager(player) != null){
            FKPlayer lastDamager = instance.getCustomPlayer(getLastDamager(player));
            String i = fkPlayer.getCustomKill()[new Random().nextInt(fkPlayer.getCustomKill().length)].replaceAll("%victim%", player.getName()).replaceAll("%attacker%", lastDamager.getName());
            Bukkit.broadcastMessage(i);
            lastDamager.playSound(lastDamager.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
        }else {
            Bukkit.broadcastMessage("§c" + player.getName() + "§7 est mort par quelque chose d'invraisemblable");
        }
        lastDamager.remove(player);


        player.setGameMode(GameMode.SPECTATOR);


        for (ItemStack itemStack : player.getInventory().getContents()) {
            if(itemStack != null){
                player.getWorld().dropItem(player.getLocation(), itemStack);
            }
        }

        player.getInventory().clear();

        if(fkPlayer.getTeam().getBase().getCore().isDead()){
            fkPlayer.getTeam().getLastPlayers().remove(fkPlayer);

            if(fkPlayer.getTeam().getLastPlayers().isEmpty()){
                Bukkit.broadcastMessage(fkPlayer.getTeam().getMessageOfLose());
                fkPlayer.getTeam().setDead(true);
                int i = 0;
                Team t = null;
                for(Team team : instance.getTeams().getTeams()){
                    if(!team.isDead()){
                        i++;
                        t = team;
                    }
                }
                if(i == 1){
                    if(t != null){
                        Bukkit.broadcastMessage(t.getMessageOfWin());
                    }else {
                        Bukkit.broadcastMessage("§c§lFK Universe §7: §c§lEgalité");
                    }
                }
            }
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(instance.getGameSession().safeLoc(fkPlayer.getTeam().getBase().getSpawn()));

                player.setGameMode(GameMode.SURVIVAL);
            }
        }.runTaskLater(SpigotPlugin.getInstance(), 20*5);
    }

    public void onDamage(Player player, Player damager){
        lastDamager.put(player, damager);



    }

    public Player getLastDamager(Player player){
        return lastDamager.get(player);
    }
}
