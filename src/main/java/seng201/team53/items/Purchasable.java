package seng201.team53.items;

/**
 * Represents an item that is purchasable from the shop.
 * Contains constant information used in the shop: name, cost, etc.
 * Use .create() to create an actual instance of the item.
 */
public interface Purchasable {
    String getName();

    String getDescription();

    int getCostPrice();

    int getSellPrice();

    Item create();
}
