package seng201.team53.game.round;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.util.Duration;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.items.Cart;
import seng201.team53.game.items.ResourceType;

/**
 * Represents a single round in the game
 * Each round has a number, a list of cart and a set of actions to be preformed at the end of the round
 */
public class GameRound {
    private final int roundNumber;
    private final List<Cart> carts = new ArrayList<>();
    private final Set<Runnable> roundEndActions = new HashSet<>();

    /**
     * Constructs a new game round with the specified round number
     * @param roundNumber The number of the round
     */
    public GameRound(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    /**
     * Retrieves the number of the round
     * @return The round number
     */
    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * Calculates the amount of money the player should earn for reaching this round
     * @return The amount of money
     */
    public int getMoneyEarned() {
        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        return (int)Math.round(this.getRoundNumber() * difficulty.getMoneyEarnMultiplier());
    }

    /**
     * Retrieves the list of carts in the round
     * @return The list of carts
     */
    public List<Cart> getCarts() {
        return carts;
    }

    /**
     * Adds a new cart to the round
     * @param maxCapacity The maximum capacity of the cart
     * @param velocity The velocity of the cart in tiles per second
     * @param resourceType The type of resource the cart accepts
     * @param spawnDelay The delay before the cart spawns
     */
    public void addCart(int maxCapacity, float velocity, ResourceType resourceType, Duration spawnDelay) {
        var cart = new Cart(maxCapacity,
            velocity,
            resourceType,
            spawnDelay);
        carts.add(cart);
    }

    /**
     * Runs all the actions that are set to be preformed at the end of the round
     */
    public void runRoundEndActions() {
        roundEndActions.forEach(Runnable::run);
    }

    /**
     * Adds a new action to be preformed at the end of the round
     * Used for temporary random tower events where stats are changed only for the given round
     * @param runnable The action to be added
     */
    public void addOnRoundEndAction(Runnable runnable) {
        roundEndActions.add(runnable);
    }

    /**
     * Checks if the round has been won by making sure all the carts are full
     * @return true if the round was one, false otherwise
     */
    public boolean roundWon() {
        return carts.stream().allMatch(Cart::isFull);
    }
}
