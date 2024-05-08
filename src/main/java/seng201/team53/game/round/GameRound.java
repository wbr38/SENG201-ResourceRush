package seng201.team53.game.round;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng201.team53.game.GameLoop;
import seng201.team53.game.state.GameState;
import seng201.team53.game.state.GameStateHandler;
import seng201.team53.game.Tickable;
import seng201.team53.game.map.Map;
import seng201.team53.items.Cart;
import seng201.team53.items.ResourceType;
import seng201.team53.items.towers.Tower;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class GameRound implements Tickable {
    private final GameStateHandler stateHandler;
    private final Map map;
    private final int roundNumber;
    private final List<Cart> carts = new ArrayList<>();
    private final int startingMoney;
    private GameLoop gameLoop;
    private int cartsCompletedPath = 0;

    public GameRound(GameStateHandler stateHandler, Map map, int roundNumber, int startingMoney) {
        this.stateHandler = stateHandler;
        this.map = map;
        this.roundNumber = roundNumber;
        this.startingMoney = startingMoney;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getStartingMoney() {
        return startingMoney;
    }

    public void addCart(Image image, int maxCapacity, float velocity, EnumSet<ResourceType> acceptedResources, int spawnAfterTicks) {
        var cart = new Cart(maxCapacity,
                velocity,
                acceptedResources,
                spawnAfterTicks,
                image);
        carts.add(cart);
    }

    public void addCartCompletedPath() {
        cartsCompletedPath++;
        if (cartsCompletedPath == carts.size()) {
            gameLoop.stop();
            stateHandler.setState(GameState.ROUND_COMPLETE);
        }
    }
    @Override
    public void tick() {
        map.getTowers().forEach(Tower::tick);
        carts.forEach(cart -> {
            if (cart.getSpawnAfterTicks() == cart.getLifetimeTicks()) {
                var polylinePath = map.getPolylinePath();
                var imageView = new ImageView(cart.getImage());
                imageView.setX(polylinePath.getPoints().get(1));
                imageView.setY(polylinePath.getPoints().get(0));
                map.getOverlay().getChildren().add(imageView);

                var pathTransition = new PathTransition();
                pathTransition.setNode(imageView);
                pathTransition.setDuration(map.calculatePathDuration(cart.getVelocity()));
                pathTransition.setPath(polylinePath);
                pathTransition.setInterpolator(Interpolator.LINEAR);
                pathTransition.play();
                pathTransition.setOnFinished(event -> {
                    addCartCompletedPath();
                    cart.setCompletedPath(true);
                    cart.setPathTransition(null);
                    map.getOverlay().getChildren().remove(imageView);
                });
                cart.setPathTransition(pathTransition);
            }
            cart.tick();
        });
    }
    public void start() {
        if (gameLoop != null)
            throw new IllegalStateException("Game round has already started and we cannot have duplicate game loops");
        gameLoop = new GameLoop(this);
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
}
