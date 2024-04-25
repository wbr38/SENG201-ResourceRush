package seng201.team53.game.round;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import seng201.team53.App;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.GameLoop;
import seng201.team53.game.GameState;
import seng201.team53.game.Tickable;
import seng201.team53.game.event.type.RandomEvent;
import seng201.team53.game.map.Map;
import seng201.team53.items.Cart;
import seng201.team53.items.ResourceType;

public abstract class GameRound implements Tickable {
    private final int roundNumber;
    private final List<Cart> carts = new ArrayList<>();
    private final double startingMoney;
    private GameLoop gameLoop;
    private Map map;
    private int cartsCompletedPath = 0;

    public GameRound(int roundNumber, double startingMoney) {
        this(roundNumber, startingMoney, null);
    }
    public GameRound(int roundNumber, double startingMoney, Map map) {
        this.roundNumber = roundNumber;
        this.startingMoney = startingMoney;
        this.map = map;
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

    public void addCartCompletedPath() {
        cartsCompletedPath++;
        if (cartsCompletedPath == carts.size()) {
            App.getApp().getGameEnvironment().getStateHandler().setState(GameState.ROUND_COMPLETE);
            gameLoop.stop();
        }
    }

    @Override
    public void tick() {
        carts.forEach(Cart::tick);
    }
    public void start() {
        if (gameLoop != null)
            throw new IllegalStateException("Game round has already started and we cannot have duplicate game loops");
        gameLoop = new GameLoop();
        play();
    }
    public void play() {
        gameLoop.start();
        carts.forEach(cart -> {
            if (!cart.isCompletedPath() && cart.getPathTransition() != null)
                cart.getPathTransition().play();
        });
    }
    public void pause() {
        gameLoop.stop();
        carts.forEach(cart -> {
            if (!cart.isCompletedPath() && cart.getPathTransition() != null)
                cart.getPathTransition().pause();
        });
    }

    public void begin() {
        var environment = App.getApp().getGameEnvironment();
        var randomEvent = environment.getRandomEvents().requestRandomEvent();
        if (randomEvent != null) {
            environment.getStateHandler().setState(GameState.RANDOM_EVENT_DIALOG_OPEN);
            environment.getWindow().getController().showRandomEventDialog(randomEvent.getClass().getName());
            return;
        }
        environment.getStateHandler().setState(GameState.ROUND_ACTIVE);
        start();
    }

    public abstract void init();
    public abstract GameRound getNextRound();

    protected void createCart(int maxCapacity, float velocity, EnumSet<ResourceType> acceptedResources, int spawnAfterTicks) {
        var difficulty = App.getApp().getGameEnvironment().getDifficulty();
        var cart = new Cart(maxCapacity,
                velocity * difficulty.getCartVelocityMultiplier(),
                acceptedResources,
                spawnAfterTicks);
        carts.add(cart);
    }
}