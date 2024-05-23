package seng201.team53.game.items.upgrade.type;

import seng201.team53.game.items.upgrade.Upgradeable;
import seng201.team53.game.items.Purchasable;
import seng201.team53.game.items.towers.Tower;
import seng201.team53.game.items.upgrade.UpgradeItem;

import java.util.List;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

/**
 * Represents the repair tower upgrade
 */
public class UpgradeItemRepairTower extends UpgradeItem {

    /**
     * Constructs a new repair tower upgrade
     */
    public UpgradeItemRepairTower() {
        super("Repair Tower", "Repair a broken tower", 100, false, true);
    }

    /**
     * Returns a list of upgradable items that can apply this upgrade
     * @return A list of upgradable items that can apply this upgrade
     */
    @Override
    public List<Upgradeable> getApplicableItems() {
        return super.getApplicableItems(getGameEnvironment().getMap().getTowers());
    }

    /**
     * Determines if this upgrade can be applied to a given upgradable item
     * @param upgradeable The item to check
     * @return true if this upgrade can be applied to the item, false otherwise
     */
    @Override
    public boolean canApply(Upgradeable upgradeable) {
        return upgradeable instanceof Tower tower && tower.isBroken();
    }

    /**
     * Applies this upgrade to a given upgradable item
     * @param upgradeable The upgradable item to apply this upgrade to
     */
    @Override
    public void apply(Upgradeable upgradeable) {
        Tower tower = (Tower)upgradeable;
        tower.setBroken(false);
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
