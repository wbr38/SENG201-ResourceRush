package seng201.team53.game.event.type;

import java.util.concurrent.ThreadLocalRandom;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

/**
 * Represents a random event where a tower becomes broken
 */
public class RandomEventBrokenTower implements RandomEvent {

    /**
     * Checks if there are any unbroken towers available for the random event to apply
     * @return true if there is at least one unbroken tower, false otherwise
     */
    @Override
    public boolean isAvailable() {
        var map = getGameEnvironment().getMap();
        for (var tower : map.getTowers())
            if (!tower.isBroken())
                return true;
        return false;
    }

    /**
     * Applies the random event to a randomly selected unbroken tower
     */
    @Override
    public void apply() {
        var map = getGameEnvironment().getMap();
        var unbrokenTowers = map.getTowers().stream().filter(tower -> !tower.isBroken()).toList();
        if (unbrokenTowers.isEmpty())
            return;

        int randomInt = ThreadLocalRandom.current().nextInt(0, unbrokenTowers.size());
        var tower = unbrokenTowers.get(randomInt);
        tower.setBroken(true);
    }
}
