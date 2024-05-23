package seng201.team53.game.event.type;

import seng201.team53.game.items.towers.TowerType;

/**
 * Represents the contract for random events in the game.
 * Implementing classes will provide the logic to determine if an event is available and to apply the event
 */
public interface RandomEvent {
    /**
     * Retrieves the description of the random event depending on the type of tower
     * @param towerType The type of tower
     * @return The description
     */
    String getDescription(TowerType towerType);

    /**
     * Determines if the random event is currently available
     * @return true if the event is available, false otherwise
     */
    boolean isAvailable();

    /**
     * Applies the effects of the random event to the game.
     * @return The type of tower that the random event was applied to
     */
    TowerType apply();
}
