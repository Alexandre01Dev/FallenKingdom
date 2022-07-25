package be.alexandre01.fk.base.core.bossbar;

import be.alexandre01.fk.base.Base;
import be.alexandre01.fk.base.core.Core;
import be.alexandre01.universal.server.packets.Reflections;
import gnu.trove.map.TIntObjectMap;
import lombok.SneakyThrows;
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

    @SneakyThrows
    public void spawn(Location loc, Core core) {
        this.loc = loc;
        block = loc.getBlock();

        block.setType(Material.MOB_SPAWNER);
        wither = (Wither) block.getWorld().spawnEntity(block.getLocation().clone().add(0,-1.5,0), EntityType.WITHER);
        CraftWither craftWither = (CraftWither) wither;

        Reflections reflections = new Reflections();
        wither.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1),false);
        wither.setCustomNameVisible(false);
        craftWither.getHandle().b(true);
        craftWither.getHandle().setInvisible(true);
        noAI(wither);


        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(wither.getEntityId(), craftWither.getHandle().getDataWatcher(), true);
        DataWatcher w = new DataWatcher((net.minecraft.server.v1_8_R3.Entity)null);
        w.a(0, (byte)32);
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(metadata);
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

        reflections.setValue(m,"requiredPlayerRange",0);
        System.out.println(m);
        Class<?> c;
        try {
          c = Class.forName("net.minecraft.server.v1_8_R3.MobSpawnerAbstract$a");
        }catch (ClassNotFoundException e){
            c = Class.forName("net.minecraft.server.v1_8_R3.TileEntityMobSpawnerData");
        }


        System.out.println(Arrays.toString(c.getTypeParameters()));
        System.out.println(Arrays.toString(c.getDeclaredConstructors()));
        Constructor<?> constructor = c.getDeclaredConstructor(MobSpawnerAbstract.class,NBTTagCompound.class,String.class);
        constructor.setAccessible(true);

        Method method = m.getClass().getDeclaredMethod("a", c);
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
        wither.setCustomName(colorTeam+"VIE: §d" + health + "/500");
        float pourcent = (int) (health / 500 * 100);

        nbt.setFloat("HealF", pourcent*3f);
        wither.setHealth(pourcent*3f);

        System.out.println("Refresh");

        Reflections reflections = new Reflections();
     /*   List<MobSpawnerAbstract.a> as = new ArrayList<>();
        as.add(spawnData);

        reflections.setValue(m,"mobs",as);*/


        spawner.update(true);
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

