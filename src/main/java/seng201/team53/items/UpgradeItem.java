package seng201.team53.items;


import javafx.scene.image.Image;
import seng201.team53.game.assets.AssetLoader;
import seng201.team53.items.towers.Tower;

import java.util.function.Consumer;
import java.util.function.Predicate;

public enum UpgradeItem implements Purchasable {
    REPAIR_TOWER("Repair Tower",
            "Repair a broken tower",
            "/assets/items/repair_tower.png",
            100,
            Tower::isBroken,
            tower -> tower.setBroken(false),
            cart -> false,
            cart -> { throw new IllegalStateException("Cannot apply repair tower to a cart"); }),
    TEMP_FASTER_TOWER_RELOAD("Faster Reload",
            "Temporality allow a tower to reload faster",
            "/assets/decoration/red_cross.png",
            100,
            tower -> !tower.isBroken(),
            tower -> {}, // todo buff the tower temporality//
            cart -> false,
            cart -> { throw new IllegalStateException("Cannot apply repair tower to a cart"); }),
    TEMP_SLOWER_CART("Slower Cart",
            "Temporality allow a cart to travel slower",
            "/assets/decoration/red_cross.png",
            100,
            tower -> false,
            tower -> { throw new IllegalStateException("Cannot apply slower cart to a tower"); },
            cart -> true,
            cart -> { }),
    FILL_CART("Fill Cart",
            "Choose a cart to instantly fill up",
            "/assets/items/cart_full.png",
            100,
            tower -> false, tower -> { throw new IllegalStateException("Cannot apply slower cart to a tower"); },
            cart -> !cart.isFull(),
            Cart::fill);

    private final String name;
    private final String description;
    private final Image image;
    private final int costPrice;
    private final Predicate<Tower> canApplyToTowerPredicate;
    private final Consumer<Tower> applyToTowerAction;
    private final Predicate<Cart> canApplyToCartPredicate;
    private final Consumer<Cart> applyToCartAction;

    UpgradeItem(String name, String description, String imagePath, int costPrice, Predicate<Tower> canApplyToTowerPredicate, Consumer<Tower> applyToTowerAction, Predicate<Cart> canApplyToCartPredicate, Consumer<Cart> applyToCartAction) {
        this.name = name;
        this.description = description;
        this.image = AssetLoader.readImage(imagePath);
        this.costPrice = costPrice;
        this.canApplyToTowerPredicate = canApplyToTowerPredicate;
        this.applyToTowerAction = applyToTowerAction;
        this.canApplyToCartPredicate = canApplyToCartPredicate;
        this.applyToCartAction = applyToCartAction;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public int getCostPrice() {
        return costPrice;
    }

    @Override
    public int getSellPrice() {
        throw new IllegalStateException("An upgrade item cannot be sold");
    }

    @Override
    public boolean isSellable() {
        return false;
    }

    public boolean canApply(Tower tower) {
        return canApplyToTowerPredicate.test(tower);
    }

    public boolean canApply(Cart cart) {
        return canApplyToCartPredicate.test(cart);
    }

    public void apply(Tower tower) {
        applyToTowerAction.accept(tower);
    }

    public void apply(Cart cart) {
        applyToCartAction.accept(cart);
    }
}
