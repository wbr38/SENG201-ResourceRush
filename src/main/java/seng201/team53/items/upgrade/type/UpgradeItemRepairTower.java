package seng201.team53.items.upgrade.type;

import seng201.team53.items.towers.Tower;
import seng201.team53.items.upgrade.UpgradeItem;
import seng201.team53.items.upgrade.Upgradeable;

import java.util.List;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class UpgradeItemRepairTower extends UpgradeItem {
    public UpgradeItemRepairTower() {
        super("Repair Tower",
                "Repair a broken tower",
                "/assets/items/repair_tower.png",
                100,
                false,
                true);
    }

    @Override
    public List<Upgradeable> getApplicableItems() {
        return super.getApplicableItems(getGameEnvironment().getMap().getTowers());
    }

    @Override
    public boolean canApply(Upgradeable upgradeable) {
        return upgradeable instanceof Tower tower && tower.isBroken();
    }

    @Override
    public void apply(Upgradeable upgradeable) {
        var tower = (Tower) upgradeable;
        tower.setBroken(false);
    }

    @Override
    public UpgradeItem getPurchasableType() {
        return this;
    }

    @Override
    public UpgradeItem create() {
        return this;
    }
}
