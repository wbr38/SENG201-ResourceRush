package seng201.team53.game.items;

/**
 * Represents an item that is purchasable from the shop.
 * Contains constant information used in the shop: name, cost, etc.
 * Use .create() to create an actual instance of the item.
 */
public interface Purchasable {
    /**
     * Retrieves the name
     * @return The name
     */
    String getName();

    /**
     * Retrieves the description
     * @return The description
     */
    String getDescription();

    /**
     * Retrieves the cost price for purchasing
     * @return The cost price
     */
    int getCostPrice();

    /**
     * Retrieves the sell price for selling
     * @return The sell price
     */
    int getSellPrice();

    /**
     * Constructs an instance of the item
     * @return An instance of the item
     */
    Item create();
}
