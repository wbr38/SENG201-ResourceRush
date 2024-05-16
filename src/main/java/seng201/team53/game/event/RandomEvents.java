package seng201.team53.game.event;

import seng201.team53.game.GameEnvironment;
import seng201.team53.game.event.type.RandomEvent;
import seng201.team53.game.event.type.RandomEventBrokenTower;
import seng201.team53.game.event.type.RandomEventTowerStatsDecrease;
import seng201.team53.game.event.type.RandomEventTowerStatsIncrease;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a collection of random events within the game
 */
public class RandomEvents {
    private final List<RandomEvent> randomEvents = new ArrayList<>();

    /**
     * Requests a random event. The odds of a random event occurring are determined by the games difficult
     * @return a random event if the conditions are met, or null if no event should occur
     */
    public RandomEvent requestRandomEvent() {
        var difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        double randomDouble = ThreadLocalRandom.current().nextDouble();
        if (randomDouble > difficulty.getRandomEventOdds())
            return null;

        var availableRandonEvents = randomEvents.stream().filter(RandomEvent::isAvailable).toList();
        if (availableRandonEvents.isEmpty())
            return null;

        int randomInt = ThreadLocalRandom.current().nextInt(0, availableRandonEvents.size());
        return availableRandonEvents.get(randomInt);
    }

    /**
     * Initializes the list of random events
     */
    public void init() {
        randomEvents.add(new RandomEventBrokenTower());
        randomEvents.add(new RandomEventTowerStatsDecrease());
        randomEvents.add(new RandomEventTowerStatsIncrease());
    }
}
