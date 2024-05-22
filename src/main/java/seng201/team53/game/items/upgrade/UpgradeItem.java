package seng201.team53.game.items.upgrade;

import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.items.upgrade.type.UpgradeItemRepairTower;
import seng201.team53.game.items.Item;
import seng201.team53.game.items.Purchasable;
import seng201.team53.game.items.upgrade.type.UpgradeItemFasterReload;
import seng201.team53.game.items.upgrade.type.UpgradeItemFillCart;
import seng201.team53.game.items.upgrade.type.UpgradeItemSlowerCart;

import java.util.Collection;
import java.util.List;

/**
 * Represents an upgrade item that can be purchased and applied to a tower or cart
 */
public abstract class UpgradeItem implements Purchasable, Item {
    private final String name;
    private final String description;
    private final int costPrice;
    private final boolean cartUpgrade;
    private final boolean towerUpgrade;

    /**
     * Constructs an upgrade item with the given parameters
     * @param name The name of the upgrade
     * @param description The description of the upgrade
     * @param costPrice The cost price of the upgrade
     * @param cartUpgrade Whether the upgrade can be used on a cart
     * @param towerUpgrade Whether the upgrade can be used on a tower
     */
    public UpgradeItem(String name, String description, int costPrice, boolean cartUpgrade, boolean towerUpgrade) {
        this.name = name;
        this.description = description;
        this.costPrice = costPrice;
        this.cartUpgrade = cartUpgrade;
        this.towerUpgrade = towerUpgrade;
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
     * Retrieves the cost price
     * @return The cost price
     */
    @Override
    public int getCostPrice() {
        return costPrice;
    }

    /**
     * Calculates the sell price based on the game difficulty
     * @return The sell price
     */
    @Override
    public int getSellPrice() {
        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        return (int)Math.round(costPrice * difficulty.getSellPriceModifier());
    }

    /**
     * Retrieves if the upgrade can be applied to a cart
     * @return true if the upgrade is for a cart, false otherwise
     */
    public boolean isCartUpgrade() {
        return cartUpgrade;
    }

    /**
     * Retrieves if the upgrade can be applied to a tower
     * @return true if the upgrade is for a tower, false otherwise
     */
    public boolean isTowerUpgrade() {
        return towerUpgrade;
    }

    /**
     * Returns a list of upgradable items from a collection that can apply this upgrade. The supplied collection
     * can be a list of carts or towers depending on the implementation of the specific upgrade
     * @param collection The collection of upgradable items
     * @return A list of upgradable items that can apply this upgrade
     */
    protected List<Upgradeable> getApplicableItems(Collection<? extends Upgradeable> collection) {
        return collection.stream().filter(this::canApply).map(x -> (Upgradeable) x).toList();
    }

    /**
     * Returns a list of upgradable items that can apply this upgrade
     * @return A list of upgradable items that can apply this upgrade
     */
    public abstract List<Upgradeable> getApplicableItems();

    /**
     * Determines if this upgrade can be applied to a given upgradable item
     * @param upgradeable The item to check
     * @return true if this upgrade can be applied to the item, false otherwise
     */
    public abstract boolean canApply(Upgradeable upgradeable);

    /**
     * Applies this upgrade to a given upgradable item
     * @param upgradable The upgradable item to apply this upgrade to
     */
    public abstract void apply(Upgradeable upgradable);

    /**
     * This interface represents the types of Upgrade Items
     */
    public interface Type {

        /**
         * Represents the repair tower upgrade
         */
        Purchasable REPAIR_TOWER = new UpgradeItemRepairTower();

        /**
         * Represents the faster reload tower upgrade
         */
        Purchasable TEMP_FASTER_TOWER_RELOAD = new UpgradeItemFasterReload();

        /**
         * Represents the slower cart upgrade
         */
        Purchasable TEMP_SLOWER_CART = new UpgradeItemSlowerCart();

        /**
         * Represents the fill cart upgrade
         */
        Purchasable FILL_CART = new UpgradeItemFillCart();
    }
}
