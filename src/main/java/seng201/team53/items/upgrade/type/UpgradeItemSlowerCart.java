package seng201.team53.items.upgrade.type;

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
                "/assets/decoration/red_cross.png",
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
        return upgradeable instanceof Cart cart && !cart.isCompletedPath();
    }

    @Override
    public void apply(Upgradeable upgradeable) {
        // todo - maybe add something to game loop where it removes upgrade after a lil bit
    }

    @Override
    public Purchasable<UpgradeItem> getPurchasableType() {
        return this;
    }

    @Override
    public UpgradeItem create() {
        return new UpgradeItemSlowerCart();
    }
}
