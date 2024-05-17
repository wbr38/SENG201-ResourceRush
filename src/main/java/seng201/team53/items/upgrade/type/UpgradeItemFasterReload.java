package seng201.team53.items.upgrade.type;

import seng201.team53.items.Purchasable;
import seng201.team53.items.towers.Tower;
import seng201.team53.items.upgrade.UpgradeItem;
import seng201.team53.items.upgrade.Upgradeable;

import java.util.List;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class UpgradeItemFasterReload extends UpgradeItem {
    public UpgradeItemFasterReload() {
        super("Faster Reload",
                "Temporality allow a tower to reload faster",
                "/assets/decoration/red_cross.png",
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
        return upgradeable instanceof Tower tower && !tower.isBroken();
    }

    @Override
    public void apply(Upgradeable upgradeable) {
        ((Tower) upgradeable).addReloadSpeedModifier();
    }

    @Override
    public Purchasable<UpgradeItem> getPurchasableType() {
        return this;
    }

    @Override
    public UpgradeItem create() {
        return new UpgradeItemFasterReload();
    }
}
