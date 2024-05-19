package seng201.team53.items;

import javafx.scene.image.Image;

/**
 * Represents an item that is purchasable from the shop.
 * Contains constant information used in the shop: name, cost, etc.
 * Use .create() to create an actual instance of the item.
 */
public abstract class Purchasable<I extends Item> {
    public abstract String getName();

    public abstract String getDescription();

    public abstract int getCostPrice();

    public abstract int getSellPrice();

    public abstract Image getImage();

    public abstract boolean isSellable();

    public abstract I create();
}
