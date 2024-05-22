package seng201.team53.game.items.towers;

import javafx.util.Duration;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.items.Purchasable;
import seng201.team53.game.items.ResourceType;

/**
 * Represents a type of tower.
 * Each tower type has a resource type, cost price and reload speed
 */
public class TowerType implements Purchasable {
    private final String name;
    private final String description;
    private final ResourceType resourceType;
    private final int costPrice;
    private final Duration reloadSpeed;

    /**
     * Constructs a new tower type
     * @param name The name of the tower type
     * @param description The description of the tower type
     * @param resourceType The resource the tower type generates
     * @param costPrice The cost price of the tower type
     * @param reloadSpeed The reload speed of the tower type
     */
    TowerType(String name, String description, ResourceType resourceType, int costPrice, Duration reloadSpeed) {
        this.name = name;
        this.description = description;
        this.resourceType = resourceType;
        this.costPrice = costPrice;
        this.reloadSpeed = reloadSpeed;
    }

    /**
     * Retrieves the name
     * @return The name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retrieves the description
     * @return The description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the resource type that the tower type generates
     * @return The resource type the tower type generates
     */
    public ResourceType getResourceType() {
        return resourceType;
    }

    /**
     * Retrieves the reload speed duration
     * @return The reload speed duration
     */
    public Duration getReloadSpeed() {
        return reloadSpeed;
    }

    /**
     * Retrieves the cost price for purchasing
     * @return The cost price
     */
    @Override
    public int getCostPrice() {
        return costPrice;
    }

    /**
     * Calculates the sell price for selling the tower type
     * @return The sell price
     */
    @Override
    public int getSellPrice() {
        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        return (int)Math.round(costPrice * difficulty.getSellPriceModifier());
    }

    /**
     * Constructs an instance of a tower with the tower type
     * @return The instance of a tower
     */
    public Tower create() {
        return new Tower(this);
    }
}
