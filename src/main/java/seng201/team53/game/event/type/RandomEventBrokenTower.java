package seng201.team53.game.event.type;

import seng201.team53.game.items.towers.Tower;
import seng201.team53.game.items.towers.TowerType;
import seng201.team53.game.map.GameMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

/**
 * Represents a random event where a tower becomes broken
 */
public class RandomEventBrokenTower implements RandomEvent {
    private final Map<TowerType, String> messages = new HashMap<>() {
        {
            put(Tower.Type.LUMBER_MILL, "Pests have infested your Lumber Mill. Maintenance is required!");
            put(Tower.Type.MINE, "A cave-in has occurred in your Mine. It’s time to rebuild!");
            put(Tower.Type.QUARRY, "A landslide has damaged your Quarry. We need to start repairs!");
            put(Tower.Type.WINDMILL, "A strong gust has damaged your Windmill. Time for repairs!");
        }
    };

    /**
     * Returns a description of the random event based on what tower type it has selected
     * @return The description
     */
    @Override
    public String getDescription(TowerType towerType) {
        return messages.get(towerType);
    }

    /**
     * Checks if there are any unbroken towers available for the random event to apply
     * @return true if there is at least one unbroken tower, false otherwise
     */
    @Override
    public boolean isAvailable() {
        GameMap map = getGameEnvironment().getMap();
        for (Tower tower : map.getTowers())
            if (!tower.isBroken())
                return true;
        return false;
    }

    /**
     * Applies the random event to a randomly selected unbroken tower
     */
    @Override
    public TowerType apply() {
        GameMap map = getGameEnvironment().getMap();
        List<Tower> unbrokenTowers = map.getTowers().stream().filter(tower -> !tower.isBroken()).toList();
        if (unbrokenTowers.isEmpty())
            return null;

        int randomInt = ThreadLocalRandom.current().nextInt(0, unbrokenTowers.size());
        Tower tower = unbrokenTowers.get(randomInt);
        tower.setBroken(true);
        return tower.getPurchasableType();
    }
}
