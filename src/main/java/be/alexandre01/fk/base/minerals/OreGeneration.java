package be.alexandre01.fk.base.minerals;

import be.alexandre01.universal.server.utils.locations.Region;
import org.bukkit.Location;
import org.bukkit.Material;

public class OreGeneration {
    private int current = 0;
    int[] coalChance = generateChance(70);
    int[] ironChance = generateChance(30);
    int[] goldChance = generateChance(10);
    int[] diamondChance = generateChance(5);
    int[] emeraldChance = generateChance(1);
    int[] lapisChance = generateChance(10);
    int[] redstoneChance = generateChance(10);

    int totalChance = 500;
    public void generate(Region region, Location spawn) {
        int y = spawn.getBlockY()-3;

        for (int i = y; i > 0 ; i--) {
            //for region x, z
            for (int x = region.getMinX(); x < region.getMaxX(); x++) {
                for (int z = region.getMinZ(); z < region.getMaxZ(); z++) {
                    Location location = new Location(spawn.getWorld(), x, i, z);
                    //To remove in prod
                    location.getBlock().setType(Material.STONE);
                    int random = (int) (Math.random() * totalChance);

                    Material m = getBlock(random);

                    if (m != null) {
                        location.getBlock().setType(m);
                    }
                }
            }
        }
    }


    public Material getBlock(int chance) {
        if(isChance(coalChance,chance)){
            return Material.COAL_ORE;
        }

        if(isChance(ironChance,chance)){
            return Material.IRON_ORE;
        }

        if(isChance(goldChance,chance)){
            return Material.GOLD_ORE;
        }

        if(isChance(diamondChance,chance)){
            return Material.DIAMOND_ORE;
        }

        if(isChance(emeraldChance,chance)){
            return Material.EMERALD_ORE;
        }

        if(isChance(lapisChance,chance)){
            return Material.LAPIS_ORE;
        }

        if(isChance(redstoneChance,chance)){
            return Material.REDSTONE_ORE;
        }

        return null;
    }

    public boolean isChance(int[] chance, int random){
        int min = chance[0];
        int max = chance[1];

        if(random >= min && random <= max){
            return true;
        }

        return false;
    }

    public int[] generateChance(int pourcent){
        int[] i = new int[]{current,pourcent+current};
        current += pourcent+1;
        return i;
    }


}
