package seng201.team53.items.towers;

import seng201.team53.items.ResourceType;

public class LumberMillTower extends Tower {

    public LumberMillTower() {

        super(
                "Lumber Mill",
                "/assets/wood_tower.png",
                ResourceType.WOOD);
    }

    @Override
    public String getDescription() {
        return "A Lumber Mill produces wood";
    }

    @Override
    public Double getCostPrice() {
        return 100.0;
    }

    @Override
    public Double getSellPrice() {
        return 50.0;
    }

    @Override
    public Boolean isSellable() {
        return true;
    }

    @Override
    public void getUpgrades() {
        throw new UnsupportedOperationException("Unimplemented method 'getUpgrades'");
    }

    @Override
    public String getSpriteFilePath() {
        throw new UnsupportedOperationException("Unimplemented method 'getSpriteFilePath'");
    }
}
