package seng201.team53.items.towers;

import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.util.Duration;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.state.CartState;
import seng201.team53.game.state.GameState;
import seng201.team53.items.Cart;
import seng201.team53.items.Item;
import seng201.team53.items.ResourceType;
import seng201.team53.items.upgrade.Upgradeable;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class Tower implements Item, Upgradeable {
    private final TowerType type;
    private final BooleanProperty brokenProperty = new SimpleBooleanProperty(false);
    private final LongProperty lastGenerateTimeProperty = new SimpleLongProperty(System.currentTimeMillis());
    private double reloadSpeedModifier = 1;
    private int xpLevel = 0;

    private Timeline generateTimeline;

    protected Tower(TowerType type) {
        this.type = type;

        // Timeline for Tower generating. They keyframe at index 1 is used to modify the delay/reload speed
        generateTimeline = new Timeline(
            new KeyFrame(Duration.ZERO, event -> generate()),
            new KeyFrame(Duration.millis(getReloadMS()))
        );
        generateTimeline.setCycleCount(Timeline.INDEFINITE);

        getGameEnvironment().getStateHandler().getGameStateProperty().addListener(($, oldState, newState) -> {
            if (newState == GameState.ROUND_ACTIVE)
                generateTimeline.play();
            else if (newState == GameState.ROUND_PAUSE)
                generateTimeline.pause();
        });
    }

    private void generate() {
        if (!canGenerate())
            return;

        List<Cart> carts = GameEnvironment.getGameEnvironment().getRound().getCarts();
        ResourceType resourceType = this.getPurchasableType().getResourceType();
        carts.forEach(cart -> cart.addResource(resourceType));
        setLastGenerateTime(System.currentTimeMillis());
    }

    public boolean canGenerate() {
        if (isBroken())
            return false;

        GameState gameState = getGameEnvironment().getStateHandler().getGameStateProperty().getValue();
        if (gameState != GameState.ROUND_ACTIVE)
            return false;

        // Tower should only generate if there are carts to be filled
        // Check if there are any unfilled carts that can be filled by this tower.
        List<Cart> carts = GameEnvironment.getGameEnvironment().getRound().getCarts();
        final ResourceType resourceType = getPurchasableType().getResourceType();
        boolean cartsToFill = carts.stream().anyMatch(cart -> {
            return !cart.isFull()
                && cart.getResourceType() == resourceType
                && cart.getCartState() == CartState.TRAVERSING_PATH;
        });

        if (!cartsToFill)
            return false;

        return true;
    }

    public void addReloadSpeedModifier() {
        reloadSpeedModifier += 0.5;
        updateGenerateDelay();
    }

    public void resetReloadSpeedModifier() {
        reloadSpeedModifier = 1;
        updateGenerateDelay();
    }

    /**
     * Update the generate timeline with a new reload delay.
     */
    private void updateGenerateDelay() {
        // Need to restart the timeline for changes to take affect
        generateTimeline.stop();
        Duration delay = Duration.millis(getReloadMS());
        generateTimeline.getKeyFrames().set(1, new KeyFrame(delay));
        generateTimeline.play();
    }

    /**
     * @return The modified reload speed of this tower in milliseconds.
     */
    private long getReloadMS() {
        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        long reloadSpeed = type.getReloadSpeed().toMillis();
        reloadSpeed /= difficulty.getTowerReloadModifier();
        reloadSpeed /= reloadSpeedModifier;
        return reloadSpeed;
    }


    public TowerType getPurchasableType() {
        return type;
    }

    public BooleanProperty getBrokenProperty() {
        return brokenProperty;
    }

    public boolean isBroken() {
        return brokenProperty.get();
    }

    public void setBroken(boolean broken) {
        brokenProperty.set(broken);
    }

    public LongProperty getLastGenerateTimeProperty() {
        return lastGenerateTimeProperty;
    }

    public long getLastGenerateTime() {
        return lastGenerateTimeProperty.get();
    }

    public void setLastGenerateTime(long time) {
        lastGenerateTimeProperty.set(time);
    }

    public int getXpLevel() {
        return xpLevel;
    }

    public void incrementXpLevel(int amount) {
        this.xpLevel += amount;
    }


    public interface Type {
        TowerType LUMBER_MILL = new TowerType("Lumber Mill Tower",
            "A Lumber Mill produces wood",
            ResourceType.WOOD,
            100,
            1,
            java.time.Duration.ofSeconds(1));


        TowerType MINE = new TowerType("Mine Tower",
            "A Mine produces ores",
            ResourceType.STONE,
            120,
            1,
            java.time.Duration.ofSeconds(1));

        TowerType QUARRY = new TowerType("Quarry Tower",
            "A Quarry produces stone",
            ResourceType.ORE,
            150,
            1,
            java.time.Duration.ofSeconds(1));

        TowerType WIND_MILL = new TowerType("Windmill Tower",
            "A wind mill produces energy",
            ResourceType.ENERGY,
            200,
            1,
            java.time.Duration.ofSeconds(1));
    }
}