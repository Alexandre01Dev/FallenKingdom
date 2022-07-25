package be.alexandre01.fk.sessions.game.targetting;


import be.alexandre01.fk.base.Base;
import lombok.Getter;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class TargetRunnable extends BukkitRunnable {
    @Getter
    private final ArrayList<Creature> targetters = new ArrayList<>();

    public TargetRunnable() {

    }
    @Override
    public void run() {
        if(targetters.isEmpty()) {
            return;
        }

        for(Creature creature : targetters) {
            if(creature.getTarget() == null) {
                targetters.remove(creature);
                System.out.println("Removed targetter");
                continue;
            }

            System.out.println("Targetting " + creature.getTarget().getName());
            if(Base.isInBase(creature.getTarget().getLocation())) {

                creature.setTarget(null);
            }
        }

    }
}
