package seng201.team53.game.event.type;

/**
 * Represents a random event where a tower has its stats decreased
 */
public class RandomEventTowerStatsDecrease implements RandomEvent {

    /**
     * Checks if there are any unbroken unmodified towers available for the random event to apply
     * @return true if there is at least one unbroken unmodified tower, false otherwise
     */
    @Override
    public boolean isAvailable() {
        // todo
        return false;
    }

    /**
     * Applies the random event to a randomly selected unbroken and unmodified tower
     */
    @Override
    public void apply() {
        // todo
    }
}
