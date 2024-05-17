package seng201.team53.items;

/*
 * Represents an instance of an Item (a Tower or UpgradeItem).
 */
public interface Item<I extends Item<?>> {
    /**
     * @return The Purchasable<Item> representation of this class.
     * For Towers, this would return the TowerType.
     */
    Purchasable<I> getPurchasableType();
}
