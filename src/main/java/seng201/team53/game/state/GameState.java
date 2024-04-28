package seng201.team53.game.state;

/**
 * An enum representing the various states the game can be in.
 */
public enum GameState {
    /**
     * The round has initialised but has not yet been started
     */
    ROUND_NOT_STARTED,
    /**
     * A random event has been triggered for a round and the game is paused
     */
    RANDOM_EVENT_DIALOG_OPEN,
    /**
     * The round active where carts are moving
     */
    ROUND_ACTIVE,
    /**
     * The round is paused. Carts will remain stationary until the round is resumed
     */
    ROUND_PAUSE,
    /**
     * The round has been completed and waiting for the next round to start
     */
    ROUND_COMPLETE,
    /**
     * The game has been complete and is no longer playable. This is the final state
     */
    GAME_COMPLETE;
}
