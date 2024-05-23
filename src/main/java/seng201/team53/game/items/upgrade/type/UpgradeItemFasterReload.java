package seng201.team53.game.items.upgrade.type;

import seng201.team53.game.items.Purchasable;
import seng201.team53.game.items.towers.Tower;
import seng201.team53.game.items.upgrade.UpgradeItem;
import seng201.team53.game.items.upgrade.Upgradeable;
import seng201.team53.game.round.GameRound;

import java.util.List;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

/**
 * Represents the faster reload tower upgrade
 */
public class UpgradeItemFasterReload extends UpgradeItem {

    /**
     * Constructs a new faster reload tower upgrade
     */
    public UpgradeItemFasterReload() {
        super("Faster Reload", "Temporality allow a tower to reload faster", 100, false, true);
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
        return upgradeable instanceof Tower tower && !tower.isBroken();
    }

    /**
     * Applies this upgrade to a given upgradable item
     * @param upgradeable The upgradable item to apply this upgrade to
     */
    @Override
    public void apply(Upgradeable upgradeable) {
        GameRound round = getGameEnvironment().getRound();
        Tower tower = (Tower)upgradeable;
        tower.addReloadSpeedModifier();
        round.addOnRoundEndAction(tower::resetReloadSpeedModifier);
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
     * Retrieves the type of purchasable upgrade type
     * @return The upgrade item
     */
    @Override
    public UpgradeItem create() {
        return this;
    }
}
