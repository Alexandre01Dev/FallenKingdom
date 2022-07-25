package be.alexandre01.fk.base.core.bossbar;

import gnu.trove.map.TIntObjectMap;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.entity.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomWither extends EntityWither {

	private PathfinderGoalArrowAttack a;
	public CustomWither(World world) {
		super(world);
		DataWatcher w = new DataWatcher((net.minecraft.server.v1_8_R3.Entity)null);
		TIntObjectMap dataValues = (TIntObjectMap) getPrivateField("dataValues",DataWatcher.class,w);
		dataValues.forEachEntry((i, o) -> {
			if(i != 0)
				w.a(i,o);
			return true;
		});
		w.a(0, (byte)32);
		setPrivateField("datawatcher",Entity.class,this,w);




	}

	public static CustomWither spawn(Location loc) {
		World w = ((CraftWorld) loc.getWorld()).getHandle();
		CustomWither f = new CustomWither(w);
		f.setPosition(loc.getX(), loc.getY(), loc.getZ());
		w.addEntity(f, CreatureSpawnEvent.SpawnReason.CUSTOM);
		f.setEquipment(0,new ItemStack(Items.BOW));
		//f.setPrivateField("a",EntitySkeleton.class, f,);
		return f;
	}

	public static void registerEntity() {
		try {
			List<Map<?, ?>> dataMap = new ArrayList<Map<?, ?>>();
			for (Field f : EntityTypes.class.getDeclaredFields()){
				if (f.getType().getSimpleName().equals(Map.class.getSimpleName())){
					f.setAccessible(true);
					dataMap.add((Map<?, ?>) f.get(null));
				}
			}

			if (dataMap.get(2).containsKey(64)){
				dataMap.get(0).remove("CustomWither");
				dataMap.get(2).remove(64);
			}
			Method a = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
			a.setAccessible(true);
			a.invoke(null, CustomWither.class, "CustomWither", 64);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object getPrivateField(String fieldName, Class clazz, Object a) {
		Object o = null;
		Field field;
		try {
			field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			o = field.get(a);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return o;
	}

	private void setPrivateField(String fieldName, Class clazz, Object a, Object e) {
		Field field;
		try {
			System.out.println(Arrays.toString(clazz.getDeclaredFields()));
			System.out.println(Arrays.toString(clazz.getFields()));
			field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(a, e);
		} catch (NoSuchFieldException | IllegalAccessException ex) {
			ex.printStackTrace();
		}
	}

}

