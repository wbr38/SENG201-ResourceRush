package seng201.team53.items.upgrade.type;

import seng201.team53.game.state.CartState;
import seng201.team53.items.Cart;
import seng201.team53.items.Purchasable;
import seng201.team53.items.upgrade.UpgradeItem;
import seng201.team53.items.upgrade.Upgradeable;

import java.util.List;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

/**
 * Represents the slower cart upgrade
 */
public class UpgradeItemSlowerCart extends UpgradeItem {

    /**
     * Constructs a new slower cart upgrade
     */
    public UpgradeItemSlowerCart() {
        super("Slower Cart",
                "Temporality allow a cart to travel slower",
                100,
                true,
                false);
    }

    /**
     * Returns a list of upgradable items that can apply this upgrade
     * @return A list of upgradable items that can apply this upgrade
     */
    @Override
    public List<Upgradeable> getApplicableItems() {
        return super.getApplicableItems(getGameEnvironment().getRound().getCarts());
    }

    /**
     * Determines if this upgrade can be applied to a given upgradable item
     * @param upgradeable The item to check
     * @return true if this upgrade can be applied to the item, false otherwise
     */
    @Override
    public boolean canApply(Upgradeable upgradeable) {
        return upgradeable instanceof Cart cart && cart.getCartState() == CartState.TRAVERSING_PATH;
    }

    /**
     * Applies this upgrade to a given upgradable item
     * @param upgradeable The upgradable item to apply this upgrade to
     */
    @Override
    public void apply(Upgradeable upgradeable) {
        var cart = (Cart) upgradeable;
        cart.decreaseVelocityModifier();
    }

    /**
     * Retrieves the type of purchasable upgrade type
     * @return The purchase type
     */
    @Override
    public Purchasable getPurchasableType() {
        return this;
    }

    /**
     * R
     * Retrieves the type of purchasable upgrade type
     * @return The upgrade item
     */
    @Override
    public UpgradeItem create() {
        return this;
    }
}
