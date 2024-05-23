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
 * Represents a random event where a tower has its stats decreased
 */
public class RandomEventTowerStatsDecrease implements RandomEvent {
    private final Map<TowerType, String> messages = new HashMap<>() {
        {
            put(Tower.Type.LUMBER_MILL, "The trees seem sparse. Your Lumber Mill’s stats have decreased.");
            put(Tower.Type.MINE, "The mine has hit a dry spell. Your Mine’s stats have decreased.");
            put(Tower.Type.QUARRY, "The stone seems scarce. Your Quarry’s stats have decreased.");
            put(Tower.Type.WINDMILL, "The winds are calm today. Your Windmill’s stats have decreased.");
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
     * Checks if there are any unbroken unmodified towers available for the random event to apply
     * @return true if there is at least one unbroken unmodified tower, false otherwise
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
     * Applies the random event to a randomly selected unbroken and unmodified tower
     */
    @Override
    public TowerType apply() {
        GameMap map = getGameEnvironment().getMap();
        List<Tower> unbrokenTowers = map.getTowers().stream().filter(tower -> !tower.isBroken()).toList();
        if (unbrokenTowers.isEmpty())
            return null;

        int randomInt = ThreadLocalRandom.current().nextInt(0, unbrokenTowers.size());
        Tower tower = unbrokenTowers.get(randomInt);
        tower.minusReloadModifier();
        return tower.getPurchasableType();
    }
}
