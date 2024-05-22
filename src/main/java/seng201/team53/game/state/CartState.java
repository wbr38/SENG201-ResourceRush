package seng201.team53.game.state;

/**
 * An enum representing the various state a cart can be in
 */
public enum CartState {

    /**
     * The cart is waiting to be spawned
     */
    WAITING,

    /**
     * The cart is currently traversing the path
     */
    TRAVERSING_PATH,

    /**
     * The cart has completed the path
     */
    COMPLETE_PATH
}
