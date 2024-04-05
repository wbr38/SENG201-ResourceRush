package seng201.team53.items.towers;

import seng201.team53.items.ResourceType;

public class QuarryTower extends Tower {

    QuarryTower(String name, ResourceType resourceType) {
        super(name, resourceType);
    }

    @Override
    public String getDescription() {
        return "A Quarry produces stone";
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
