package seng201.team53.game.event;

import seng201.team53.App;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.event.type.RandomEvent;
import seng201.team53.game.event.type.RandomEventBrokenTower;
import seng201.team53.game.event.type.RandomEventTowerStatsDecrease;
import seng201.team53.game.event.type.RandomEventTowerStatsIncrease;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomEvents {
    private final List<RandomEvent> randomEvents = new ArrayList<>();

    public RandomEvent requestRandomEvent() {
        GameDifficulty difficulty = App.getApp().getGameEnvironment().getDifficulty();
        double randomDouble = ThreadLocalRandom.current().nextDouble();
        if (randomDouble > difficulty.getRandomEventOdds())
            return null;

        int randomInt = ThreadLocalRandom.current().nextInt(0, randomEvents.size());
        return randomEvents.get(randomInt);
    }

    public void init() {
        randomEvents.add(new RandomEventBrokenTower());
        randomEvents.add(new RandomEventTowerStatsDecrease());
        randomEvents.add(new RandomEventTowerStatsIncrease());
    }
}
