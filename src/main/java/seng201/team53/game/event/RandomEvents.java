package seng201.team53.game.event;

import seng201.team53.game.event.type.RandomEvent;
import seng201.team53.game.event.type.RandomEventBrokenTower;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class RandomEvents {
    private final List<RandomEvent> randomEvents = new ArrayList<>();

    public RandomEvent requestRandomEvent() {
        var difficulty = getGameEnvironment().getDifficulty();
        double randomDouble = ThreadLocalRandom.current().nextDouble();
        if (randomDouble > difficulty.getRandomEventOdds())
            return null;

        var availableRandonEvents = randomEvents.stream().filter(RandomEvent::isAvailable).toList();
        if (availableRandonEvents.isEmpty())
            return null;

        int randomInt = ThreadLocalRandom.current().nextInt(0, availableRandonEvents.size());
        return availableRandonEvents.get(randomInt);
    }

    public void init() {
        randomEvents.add(new RandomEventBrokenTower());
        //randomEvents.add(new RandomEventTowerStatsDecrease());
        //randomEvents.add(new RandomEventTowerStatsIncrease());
    }
}
