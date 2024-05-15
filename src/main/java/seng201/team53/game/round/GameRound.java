package seng201.team53.game.round;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.GameLoop;
import seng201.team53.game.Tickable;
import seng201.team53.game.map.GameMap;
import seng201.team53.game.state.GameState;
import seng201.team53.game.state.GameStateHandler;
import seng201.team53.items.Cart;
import seng201.team53.items.ResourceType;
import seng201.team53.items.towers.Tower;
import seng201.team53.items.upgrade.Upgradeable;

import java.util.*;
import java.util.function.Consumer;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class GameRound implements Tickable {
    private final GameStateHandler stateHandler;
    private final GameMap map;
    private final int roundNumber;
    private final List<Cart> carts = new ArrayList<>();
    private final Map<Integer, Runnable> tempUpgradeExpireActions = new HashMap<>();
    private GameLoop gameLoop;
    private int cartsCompletedPath = 0;
    private int lifetimeTicks = 0;

    public GameRound(GameStateHandler stateHandler, GameMap map, int roundNumber) {
        this.stateHandler = stateHandler;
        this.map = map;
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

    public Cart findCartAtScreenPosition(int screenX, int screenY) {
        for (Cart cart : carts) {
            var imageView = cart.getImageView();
            if (imageView == null) // cart hasn't spawned on screen yet
                continue;

            var pointInScene = imageView.localToScene(imageView.getX(), imageView.getY());
            double sceneX = pointInScene.getX();
            double sceneY = pointInScene.getY();
            if (sceneX <= screenX &&
                    (sceneX +  40) > screenX &&
                    sceneY <= screenY &&
                    (sceneY +  40) > screenY)
                return cart;
        }
        return null;
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
                var capacityLabel = new Label();
                capacityLabel.setFont(Font.font("System Regular", 16));
                capacityLabel.setTextFill(Color.WHITE);

                var imageView = new ImageView(cart.getImage());
                var pane = new StackPane();
                var overlay = getGameEnvironment().getController().getOverlay();
                imageView.setX(polylinePath.getPoints().get(1));
                imageView.setY(polylinePath.getPoints().get(0));
                overlay.getChildren().add(pane);
                pane.getChildren().addAll(imageView, capacityLabel);

                var pathTransition = new PathTransition();
                pathTransition.setNode(pane);
                pathTransition.setDuration(map.calculatePathDuration(cart.getVelocity()));
                pathTransition.setPath(polylinePath);
                pathTransition.setInterpolator(Interpolator.LINEAR);
                pathTransition.play();
                pathTransition.setOnFinished(event -> {
                    addCartCompletedPath();
                    cart.setCompletedPath(true);
                    cart.setPathTransition(null);
                    cart.setCapacityLabel(null);
                    overlay.getChildren().remove(pane);
                });
                cart.setPathTransition(pathTransition);
                cart.setCapacityLabel(capacityLabel);
                cart.setImageView(imageView);
                cart.update();
            }
            cart.tick();
        });
        
        // Fill carts 
        map.getTowers().forEach(tower -> {
            tower.tick();
            if (tower.canGenerate()) {
                carts.forEach(cart -> cart.addResource(tower.getType().getResourceType()));
            }
        });

        var expireAction = tempUpgradeExpireActions.remove(lifetimeTicks);
        if (expireAction != null)
            expireAction.run();

        lifetimeTicks++;
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

    public void addTempUpgradeExpireAction(int expireAfterSeconds, Runnable action) {
        int ticks = expireAfterSeconds * GameLoop.TICKS_PER_SECOND;
        tempUpgradeExpireActions.put(lifetimeTicks + ticks, action);
    }
}
