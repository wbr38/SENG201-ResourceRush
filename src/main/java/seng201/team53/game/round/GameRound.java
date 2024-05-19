package seng201.team53.game.round;

import java.util.*;

import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.GameLoop;
import seng201.team53.game.Tickable;
import seng201.team53.items.Cart;
import seng201.team53.items.ResourceType;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class GameRound implements Tickable {
    private final int roundNumber;
    private final List<Cart> carts = new ArrayList<>();
    private final Set<Runnable> roundEndActions = new HashSet<>();
    private GameLoop gameLoop;

    public GameRound( int roundNumber) {
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

    public int getMaxCartFinishTicks() {
        var map = getGameEnvironment().getMap();
        return carts.stream().mapToInt(cart -> {
            var duration = map.calculatePathDuration(cart.getVelocity()).toMillis();
            return cart.getSpawnAfterTicks() + (int)Math.ceil(duration / GameLoop.MS_BETWEEN_TICKS);
        }).max().orElseGet(() -> 0); // should never happen tho
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void addCart(int maxCapacity, float velocity, EnumSet<ResourceType> acceptedResources, int spawnAfterTicks) {
        var cart = new Cart(maxCapacity,
            velocity,
            acceptedResources,
            spawnAfterTicks);
        carts.add(cart);
    }

    @Override
    public void tick(int lifetime) {

        carts.forEach(cart -> cart.tick(lifetime));
        getGameEnvironment().getMap().getTowers().forEach(tower -> {
            if (tower.canGenerate()) {
                carts.forEach(cart -> cart.addResource(tower.getPurchasableType().getResourceType()));
            }
        });
    }

    public void play() {
        if (gameLoop == null) {
            gameLoop = new GameLoop(this);
            updateMaxCartFinishTicks();
        }
        gameLoop.start();
    }

    public void pause() {
        gameLoop.stop();
    }

    public void runRoundEndActions() {
        roundEndActions.forEach(Runnable::run);
    }

    public void addOnRoundEndAction(Runnable runnable) {
        roundEndActions.add(runnable);
    }

    public void updateMaxCartFinishTicks() {
        var map = getGameEnvironment().getMap();
        int maxCartTicks = carts.stream().mapToInt(cart -> {
            var duration = map.calculatePathDuration(cart.getVelocity()).toMillis();
            return (int)Math.ceil(duration / GameLoop.MS_BETWEEN_TICKS);
        }).max().orElseGet(() -> 0);
        gameLoop.setRoundCompleteTicks(maxCartTicks);
    }
}
