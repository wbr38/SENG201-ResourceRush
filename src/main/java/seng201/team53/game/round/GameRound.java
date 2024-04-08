package seng201.team53.game.round;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import seng201.team53.App;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.Tickable;
import seng201.team53.game.map.Map;
import seng201.team53.items.Cart;
import seng201.team53.items.ResourceType;

public abstract class GameRound implements Tickable {
    private final int roundNumber;
    private final List<Cart> carts = new ArrayList<>();
    private final double startingMoney;
    private Map map;

    public GameRound(int roundNumber, double startingMoney) {
        this.roundNumber = roundNumber;
        this.startingMoney = startingMoney;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public Map getMap() {
        return map;
    }
    public void setMap(Map map) {
        if (this.map != null)
            throw new IllegalStateException("Map has already been set");
        this.map = map;
    }

    public void start() {
        // Spawn/populate this.carts with a number of carts, based on the GameDifficulty chosen
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }

    public Boolean checkWinCondition() {
        // Check if all carts have been filled
        throw new UnsupportedOperationException("Unimplemented method 'checkWinCondition'");
    }

    public Boolean checkLoseCondition() {
        // Check if any carts have reached the end of the track that have not been filled
        throw new UnsupportedOperationException("Unimplemented method 'checkLoseCondition'");
    }

    public abstract void init();

    @Override
    public void tick() {
        carts.forEach(Cart::tick);
    }

    protected void createCart(int maxCapacity, float velocity, EnumSet<ResourceType> acceptedResources) {
        var difficulty = App.getApp().getGameEnvironment().getDifficulty();
        var cart = new Cart(maxCapacity,
                velocity * difficulty.getCartVelocityMultiplier(),
                acceptedResources);
        carts.add(cart);
    }
}
