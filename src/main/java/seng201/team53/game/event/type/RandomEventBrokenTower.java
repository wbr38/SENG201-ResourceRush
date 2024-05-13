package seng201.team53.game.event.type;

import java.util.concurrent.ThreadLocalRandom;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class RandomEventBrokenTower implements RandomEvent {

    @Override
    public boolean isAvailable() {
        var map = getGameEnvironment().getMap();
        for (var tower : map.getTowers())
            if (!tower.isBroken())
                return true;
        return false;
    }

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
