package seng201.team53.items.upgrade;

import javafx.scene.image.Image;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.assets.AssetLoader;
import seng201.team53.items.Item;
import seng201.team53.items.Purchasable;
import seng201.team53.items.upgrade.type.UpgradeItemFasterReload;
import seng201.team53.items.upgrade.type.UpgradeItemFillCart;
import seng201.team53.items.upgrade.type.UpgradeItemRepairTower;
import seng201.team53.items.upgrade.type.UpgradeItemSlowerCart;

import java.util.Collection;
import java.util.List;

public abstract class UpgradeItem implements Item<UpgradeItem>, Purchasable<UpgradeItem> {
    private final String name;
    private final String description;
    private final Image image;
    private final int costPrice;
    private final boolean cartUpgrade;
    private final boolean towerUpgrade;

    public UpgradeItem(String name, String description, String imagePath, int costPrice, boolean cartUpgrade, boolean towerUpgrade) {
        this.name = name;
        this.description = description;
        this.image = AssetLoader.readImage(imagePath);
        this.costPrice = costPrice;
        this.cartUpgrade = cartUpgrade;
        this.towerUpgrade = towerUpgrade;
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
        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        int sellPrice = (int)Math.round(costPrice * difficulty.getSellPriceModifier());
        return sellPrice;
    }

    @Override
    public boolean isSellable() {
        return false;
    }

    public boolean isCartUpgrade() {
        return cartUpgrade;
    }

    public boolean isTowerUpgrade() {
        return towerUpgrade;
    }

    protected List<Upgradeable> getApplicableItems(Collection<? extends Upgradeable> collection) {
        return collection.stream().filter(this::canApply).map(x -> (Upgradeable) x).toList();
    }

    public abstract List<Upgradeable> getApplicableItems();
    public abstract boolean canApply(Upgradeable upgradeable);
    public abstract void apply(Upgradeable upgradable);

    public interface Type {
        Purchasable<UpgradeItem> REPAIR_TOWER = new UpgradeItemRepairTower();
        Purchasable<UpgradeItem> TEMP_FASTER_TOWER_RELOAD = new UpgradeItemFasterReload();
        Purchasable<UpgradeItem> TEMP_SLOWER_CART = new UpgradeItemSlowerCart();
        Purchasable<UpgradeItem> FILL_CART = new UpgradeItemFillCart();
    }
}
