package be.alexandre01.fk.base.core.bossbar;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.base.core.Core;
import be.alexandre01.fk.players.FKPlayer;
import be.alexandre01.fk.sessions.game.GameSession;
import be.alexandre01.universal.server.SpigotPlugin;
import be.alexandre01.universal.server.packets.Reflections;
import be.alexandre01.universal.server.packets.npc.NPC;
import be.alexandre01.universal.server.packets.npc.type.NPCUniversalEntity;
import gnu.trove.map.TIntObjectMap;
import lombok.SneakyThrows;
import lombok.With;
import net.minecraft.server.v1_8_R3.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftCreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftWither;
import org.bukkit.entity.*;

import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BossBar {
    private Location loc;
    private Block block;
    CraftCreatureSpawner spawner;

    Wither wither;

    NBTTagCompound nbt;

    MobSpawnerAbstract mobSpawner;

    NPC npc;
    @SneakyThrows
    public void spawn(Location loc, Core core) {

        this.loc = loc;
        block = loc.getBlock();
        npc = new NPC("Wither"+core.getBase().getTeam().getColorName(),block.getLocation().clone().add(0,-1.5,0),EntityType.WITHER);


        block.setType(Material.MOB_SPAWNER);
       /* wither = (Wither) block.getWorld().spawnEntity(block.getLocation().clone().add(0,-1.5,0), EntityType.WITHER);
        CraftWither craftWither = (CraftWither) wither;

        Reflections reflections = new Reflections();
        wither.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1),false);
        wither.setCustomNameVisible(false);
        craftWither.getHandle().b(true);
        craftWither.getHandle().setInvisible(true);
        noAI(wither);*/



        for(Player player : Bukkit.getOnlinePlayers()) {
            refreshPacket(player,core);
        }
        block.getRelative(BlockFace.DOWN).setType(Material.BEDROCK);
        block.getRelative(BlockFace.UP).setType(Material.BEDROCK);
        block.getRelative(BlockFace.NORTH).setType(Material.BEDROCK);
        block.getRelative(BlockFace.SOUTH).setType(Material.BEDROCK);
        block.getRelative(BlockFace.EAST).setType(Material.BEDROCK);
        block.getRelative(BlockFace.WEST).setType(Material.BEDROCK);

         spawner = (CraftCreatureSpawner) block.getState();


        spawner.setDelay(0);
        spawner.setSpawnedType(EntityType.WITHER);
        spawner.update();
        TileEntityMobSpawner t = spawner.getTileEntity();

        MobSpawnerAbstract m =  t.getSpawner();
        Reflections reflections = new Reflections();
        reflections.setValue(m,"requiredPlayerRange",0);
        System.out.println(m);
        Class<?> c;
        try {
          c = Class.forName("net.minecraft.server.v1_8_R3.MobSpawnerAbstract$a");
        }catch (ClassNotFoundException e){
            System.out.println("Class not found ... searching for imanityspigot class");
            c = Class.forName("net.minecraft.server.v1_8_R3.TileEntityMobSpawnerData");
        }


        System.out.println(Arrays.toString(c.getTypeParameters()));
        System.out.println(Arrays.toString(c.getDeclaredConstructors()));
        Constructor<?> constructor = c.getDeclaredConstructor(MobSpawnerAbstract.class,NBTTagCompound.class,String.class);
        constructor.setAccessible(true);

        Method method = Class.forName("net.minecraft.server.v1_8_R3.MobSpawnerAbstract").getDeclaredMethod("a", c);
        method.setAccessible(true);

        method.invoke(m,constructor.newInstance(m,nbt = new NBTTagCompound(),"WitherBoss"));

      //  MobSpawnerAbstract.a spawnData = (MobSpawnerAbstract.a) reflections.getValue(m, "spawnData");

        /*List<MobSpawnerAbstract.a> as = new ArrayList<>();
        as.add(spawnData);*/

  //      reflections.setValue(m,"mobs",as);
        refresh(core.getHealth(),core.getBase().getTeam().getColor());
       // System.out.println(spawnData);
    }

    public void refresh(double health, ChatColor colorTeam){

        /*CraftCreatureSpawner spawner = (CraftCreatureSpawner) block.getState();


        spawner.setDelay(0);
        spawner.setSpawnedType(EntityType.WITHER);
        TileEntityMobSpawner t = (TileEntityMobSpawner) reflections.getValue(spawner, "spawner");
        MobSpawnerAbstract m = (MobSpawnerAbstract) reflections.getValue(t, "a");
        System.out.println(m);



        MobSpawnerAbstract.a spawnData = (MobSpawnerAbstract.a) reflections.getValue(m, "spawnData");*/
        nbt.setString("CustomName", colorTeam+"VIE: §d" + health + "/500");




       // wither.setCustomName(colorTeam+"VIE: §d" + health + "/500");
        float pourcent = (int) (health / 500 * 100);

        nbt.setFloat("HealF", pourcent*3f);
        //wither.setHealth(pourcent*3f);
        for(Player player : Bukkit.getOnlinePlayers()){
            NPCUniversalEntity npc = (NPCUniversalEntity) this.npc.get(player);
            if(npc == null){
                continue;
            }
            npc.setCustomName(colorTeam+"VIE: §d" + health + "/500");
            npc.setMetadata(6, pourcent*3f);
            npc.setInvisible();
        }

        Reflections reflections = new Reflections();
     /*   List<MobSpawnerAbstract.a> as = new ArrayList<>();
        as.add(spawnData);

        reflections.setValue(m,"mobs",as);*/


        spawner.update(true);
    }

    public void refreshPacket(Player player,Core core){
        if(npc.get(player.getPlayer()) != null){
            NPCUniversalEntity u = (NPCUniversalEntity) npc.get(player.getPlayer());
            u.hide();
            if(!core.getBase().isIn(player.getPlayer())) return;
            u.show();
            u.setCustomName("VIE: §d" + core.getHealth() + "/500");
            float pourcent = (int) (core.getHealth() / 500 * 100);
            u.setMetadata(6, pourcent*3f);
            u.setInvisible();
            return;
        }

        if(!core.getBase().isIn(player.getPlayer())) return;
        npc.initAndShow(player.getPlayer());
        refreshPacket(player,core);
    }
    void noAI(Entity bukkitEntity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) bukkitEntity).getHandle();
        NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        nmsEntity.f(tag);
    }

}

