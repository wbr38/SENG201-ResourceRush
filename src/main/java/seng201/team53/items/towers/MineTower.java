package seng201.team53.items.towers;

import seng201.team53.items.ResourceType;

public class MineTower extends Tower {

    public MineTower() {
        super(
                "Mine",
                "/assets/wood_tower.png",
                ResourceType.ORE);
    }

    @Override
    public String getDescription() {
        return "A Mine produces ores";
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSpriteFilePath'");
    }
}
