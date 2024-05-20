package seng201.team53.items.upgrade.type;

import seng201.team53.game.state.CartState;
import seng201.team53.items.Cart;
import seng201.team53.items.Purchasable;
import seng201.team53.items.upgrade.UpgradeItem;
import seng201.team53.items.upgrade.Upgradeable;

import java.util.List;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class UpgradeItemSlowerCart extends UpgradeItem {
    public UpgradeItemSlowerCart() {
        super("Slower Cart",
                "Temporality allow a cart to travel slower",
                100,
                true,
                false);
    }

    @Override
    public List<Upgradeable> getApplicableItems() {
        return super.getApplicableItems(getGameEnvironment().getRound().getCarts());
    }

    @Override
    public boolean canApply(Upgradeable upgradeable) {
        return upgradeable instanceof Cart cart && cart.getCartState() == CartState.TRAVERSING_PATH;
    }

    @Override
    public void apply(Upgradeable upgradeable) {
        var cart = (Cart) upgradeable;
        cart.addVelocityModifier();
        getGameEnvironment().getRound().updateMaxCartFinishTicks();
    }

    @Override
    public Purchasable getPurchasableType() {
        return this;
    }

    @Override
    public UpgradeItem create() {
        return this;
    }
}
