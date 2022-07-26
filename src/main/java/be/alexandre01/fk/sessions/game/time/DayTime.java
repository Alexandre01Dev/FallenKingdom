package be.alexandre01.fk.sessions.game.time;

import be.alexandre01.fk.FKPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;

import java.util.HashMap;

public class DayTime {
    @Getter int day = 1;

    @Getter private HashMap<Integer,DayTimeListener> listeners = new HashMap<>();

    @Getter  @Setter
    private String status = "LE REVEIL";
    FKPlugin plugin;
    public DayTime() {
         plugin = FKPlugin.instance;
    }

    public String getTime(){
        try {

            World world = plugin.getAutoConfig().getLocation("Spawn").getWorld();
            final int currentDay = day;
            day = (int) ((world.getFullTime()+6000) / 24000)+1;
            if(currentDay != day){
                if(listeners.containsKey(day)){
                    listeners.get(day).onDayChange(day,this);
                }
            }
            long gameTime = world.getTime();
            long hours = gameTime / 1000 + 6;
            long minutes = (gameTime % 1000) * 60 / 1000;
            if (hours >= 24) {
                hours -= 24;
            }

            //if (hours == 0) hours = 24;
            String mm = "0" + minutes;
            mm = mm.substring(mm.length() - 2, mm.length());
            String hh = "" + hours;
            if(hours < 10) hh = "0" + hours;
            return hh + ":" + mm + " ";
        }catch (Exception e) {
            e.printStackTrace();
        }

        return "00:00";
    }

    public interface DayTimeListener {
        void onDayChange(int day,DayTime dayTime);
    }

}
