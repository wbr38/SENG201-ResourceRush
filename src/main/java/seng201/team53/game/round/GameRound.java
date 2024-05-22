package seng201.team53.game.round;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.util.Duration;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.items.Cart;
import seng201.team53.items.ResourceType;


public class GameRound {
    private final int roundNumber;
    private final List<Cart> carts = new ArrayList<>();
    private final Set<Runnable> roundEndActions = new HashSet<>();

    public GameRound(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * @return The amount of money the player should earn for reaching this round.
     */
    public int getMoneyEarned() {
        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        int moneyEarned = (int)Math.round(this.getRoundNumber() * difficulty.getMoneyEarnMultiplier());
        return moneyEarned;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void addCart(int maxCapacity, float velocity, ResourceType resourceType, Duration spawnDelay) {
        var cart = new Cart(maxCapacity,
            velocity,
            resourceType,
            spawnDelay);
        carts.add(cart);
    }

    public void runRoundEndActions() {
        roundEndActions.forEach(Runnable::run);
    }

    public void addOnRoundEndAction(Runnable runnable) {
        roundEndActions.add(runnable);
    }

    /**
     * @return Whether all the carts for this round were filled
     */
    public boolean roundWon() {
        List<Cart> carts = getCarts();
        boolean allCartsFull = carts.stream().allMatch(Cart::isFull);
        return allCartsFull;
    }
}
