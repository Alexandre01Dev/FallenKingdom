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
        if (targetters.isEmpty()) {
            return;
        }

        for (int i = 0; i < targetters.size(); i++) {
            Creature creature = targetters.get(i);
            if (creature.getTarget() == null || creature.isDead()) {
                targetters.remove(creature);
                continue;
            }


            if (Base.isInBase(creature.getTarget().getLocation())) {
                creature.setTarget(null);
            }
        }
    }


}

