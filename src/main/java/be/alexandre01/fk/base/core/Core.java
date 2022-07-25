package be.alexandre01.fk.base.core;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.base.core.bossbar.BossBar;
import be.alexandre01.fk.sessions.game.GameSession;
import be.alexandre01.universal.server.packets.Reflections;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.MobSpawnerAbstract;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.TileEntityMobSpawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.*;

@Getter
public class Core {
    public Base base;
    public Location loc;
    private EnderCrystal enderCrystal;

    private BossBar bossBar;

    private ArmorStand armorStand;

    private boolean isDead = false;

    public double health = 500;
    public Core(Base base) {
        this.base = base;
        this.loc = base.getCoreSpawn();
    }

    public void spawn(){
        enderCrystal = loc.getWorld().spawn(loc, EnderCrystal.class);
        enderCrystal.setCustomName("§fVIE: §d" + health + "/500");
        enderCrystal.setCustomNameVisible(true);
        GameSession gameSession = FKPlugin.instance.getGameSession();
        gameSession.getCores().put(enderCrystal, this);

        Reflections reflections = new Reflections();
        //SPAWN BOSS BAR

        bossBar = new BossBar();
        bossBar.spawn(loc.clone().add(0,-3,0),this);

        bossBar.refresh(health,base.getTeam().getColor());

        //armorStand = loc.getWorld().spawn(loc.clone().add(0,1,0), ArmorStand.class);
       // armorStand.setGravity(false);
        //armorStand.setVisible(false);
       // armorStand.setCustomNameVisible(true);
    }



    public void setDamage(double damage){
        this.health -= damage;
        enderCrystal.setCustomName("§F§LVIE: §d" + health + "/500");
      //  armorStand.setCustomName("§F§LVIE: §d" + health + "/500");
        bossBar.refresh(health,base.getTeam().getColor());

        if(health <= 0){
            isDead = true;
        }
    }

}
