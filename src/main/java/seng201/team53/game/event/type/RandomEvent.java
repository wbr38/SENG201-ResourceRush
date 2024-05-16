package seng201.team53.game.event.type;

/**
 * Represents the contract for random events in the game.
 * Implementing classes will provide the logic to determine if an event is available and to apply the event
 */
public interface RandomEvent {

    /**
     * Determines if the random event is currently available
     * @return true if the event is available, false otherwise
     */
    boolean isAvailable();

    /**
     * Applies the effects of the random event to the game.
     */
    void apply();
}
