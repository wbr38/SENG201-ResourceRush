package seng201.team53.game.map;

/**
 * Enumerations the types of map interactions that can occur on the game map
 */
public enum MapInteraction {

    /**
     * Indicates that no interaction is currently taking place
     */
    NONE,

    /**
     * Indicates that a tower is being moved with the cursor
     */
    PLACE_TOWER,

    /**
     * Indicates that an upgrade is currently being moved with the cursor
     */
    PLACE_UPGRADE;
}
