package seng201.team53.game.items.towers;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.util.Duration;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.items.Cart;
import seng201.team53.game.items.Item;
import seng201.team53.game.items.ResourceType;
import seng201.team53.game.items.upgrade.Upgradeable;
import seng201.team53.game.state.CartState;
import seng201.team53.game.state.GameState;

/**
 * Represents a tower in the game.
 * Each tower has a type, broken property, last generate property, generate timeline, reload speed modifier
 * and an XP level
 */
public class Tower implements Item, Upgradeable {
    private final TowerType type;
    private final BooleanProperty brokenProperty = new SimpleBooleanProperty(false);
    private final LongProperty lastGenerateTimeProperty = new SimpleLongProperty(System.currentTimeMillis());
    private final Timeline generateTimeline;
    private double reloadSpeedModifier = 1;
    // private int xpLevel = 0;
    private IntegerProperty xpLevel = new SimpleIntegerProperty(1);
    private boolean inInventory = false;

    /**
     * Constructs a new tower with the given type
     * @param type The type of the tower
     */
    protected Tower(TowerType type) {
        this.type = type;

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
            
            if (newState == GameState.ROUND_COMPLETE)
                increaseLevel();
        });
    }

    /**
     * Generates the resources for the carts in the game round.
     * If the tower can generate, it will loop all the carts in the game round and attempt to give it a resource
     * of the specified type. The last generate time is set to the current time, if it generates.
     */
    private void generate() {
        if (!canGenerate())
            return;

        List<Cart> carts = GameEnvironment.getGameEnvironment().getRound().getCarts();
        ResourceType resourceType = this.getPurchasableType().getResourceType();
        carts.forEach(cart -> cart.addResource(resourceType));
        setLastGenerateTime(System.currentTimeMillis());
    }

    /**
     * Checks if the tower is ready to generate a resource
     * @return true if the tower is ready to generate, false otherwise
     */
    public boolean canGenerate() {
        if (isBroken())
            return false;

        if (isInInventory())
            return false;

        GameState gameState = getGameEnvironment().getStateHandler().getGameStateProperty().getValue();
        if (gameState != GameState.ROUND_ACTIVE)
            return false;

        List<Cart> carts = GameEnvironment.getGameEnvironment().getRound().getCarts();
        ResourceType resourceType = getPurchasableType().getResourceType();
        return carts.stream().anyMatch(cart -> !cart.isFull()
            && cart.getResourceType() == resourceType
            && cart.getCartState() == CartState.TRAVERSING_PATH);
    }

    /**
     * Adds a reload speed modifier of 0.25. Everytime this method is called, it will generate resources 50% faster,
     * until the modifier is reset
     */
    public void addReloadSpeedModifier() {
        reloadSpeedModifier += 0.25;
        updateGenerateDelay();
    }

    /**
     * Takes away a reload speed modifier of 0.25. Everytime this method is called, it will generate resources 50% faster,
     * until the modifier is reset
     */
    public void minusReloadModifier() {
        reloadSpeedModifier -= 0.25;
    }

    /**
     * Resets the reload speed modifier. The tower will generate at its original rate
     */
    public void resetReloadSpeedModifier() {
        reloadSpeedModifier = 1;
        updateGenerateDelay();
    }

    /**
     * Updates the generate timeline with a new reload delay.
     */
    private void updateGenerateDelay() {
        generateTimeline.stop();
        Duration delay = Duration.millis(getReloadMS());
        generateTimeline.getKeyFrames().set(1, new KeyFrame(delay));
        generateTimeline.play();
    }

    /**
     * Calculates the reload speed in milliseconds
     * @return The modified reload speed of this tower in milliseconds.
     */
    private long getReloadMS() {
        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        double reloadSpeed = type.getReloadSpeed().toMillis();
        reloadSpeed /= difficulty.getTowerReloadModifier();
        reloadSpeed /= reloadSpeedModifier;
        reloadSpeed /= 1 + (double)getXpLevel() / 10;
        return (long)reloadSpeed;
    }

    /**
     * Retrieves the towers purchasable type
     * @return The type of tower
     */
    public TowerType getPurchasableType() {
        return type;
    }

    /**
     * Retrieves the broken property.
     * This property is observable meaning it can be watched for changes
     * @return The observable broken property
     */
    public BooleanProperty getBrokenProperty() {
        return brokenProperty;
    }

    /**
     * Retrieves the broken boolean of the tower
     * @return true if the tower is broken, false otherwise
     */
    public boolean isBroken() {
        return brokenProperty.get();
    }

    /**
     * Sets the towers broken status
     * @param broken true if the tower is broken, false otherwise
     */
    public void setBroken(boolean broken) {
        brokenProperty.set(broken);
    }

    /**
     * Retrieves the last generate time property.
     * This property is observable, meaning it can be watched for changes
     * @return The last generate time property
     */
    public LongProperty getLastGenerateTimeProperty() {
        return lastGenerateTimeProperty;
    }

    /**
     * Sets the last generate time
     * @param time The last generate time
     */
    public void setLastGenerateTime(long time) {
        lastGenerateTimeProperty.set(time);
    }

    /**
     * Retrieves the current XP level
     * @return The XP level
     */
    public int getXpLevel() {
        return xpLevel.get();
    }

    public IntegerProperty getXpLevelProperty() {
        return xpLevel;
    }

    public void increaseLevel() {
        xpLevel.set(getXpLevel() + 1);
        System.out.println("Tower Level: " + getXpLevel());
    }

    /**
     * Set whether this tower is in inventory or not.
     * @param inInventory Whether this item is now in inventory.
     */
    public void setInInventory(boolean inInventory) {
        this.inInventory = inInventory;
    }

    /**
     * @return Whether this Tower is currently stored in inventory.
     */
    public boolean isInInventory() {
        return inInventory;
    }

    /**
     * This interface represents the types of towers
     */
    public interface Type {

        /**
         * Represents the lumber mill tower type
         */
        TowerType LUMBER_MILL = new TowerType("Lumber Mill Tower",
            "A Lumber Mill produces wood",
            ResourceType.WOOD,
            100,
            Duration.seconds(1));

        /**
         * Represents the mine tower type
         */
        TowerType MINE = new TowerType("Mine Tower",
            "A Mine produces ores",
            ResourceType.STONE,
            120,
            Duration.seconds(1));

        /**
         * Represents the quarry tower type
         */
        TowerType QUARRY = new TowerType("Quarry Tower",
            "A Quarry produces stone",
            ResourceType.ORE,
            150,
            Duration.seconds(1));

        /**
         * Represents the windmill tower type
         */
        TowerType WINDMILL = new TowerType("Windmill Tower",
            "A windmill produces energy",
            ResourceType.ENERGY,
            200,
            Duration.seconds(1));
    }
}