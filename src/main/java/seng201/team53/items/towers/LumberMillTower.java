package seng201.team53.items.towers;

import seng201.team53.items.ResourceType;

public class LumberMillTower extends Tower {

    public LumberMillTower() {

        super(
            "Lumber Mill", "/assets/wood_tower.png", ResourceType.WOOD);
    }

    @Override
    public String getDescription() {
        return "A Lumber Mill produces wood";
    }

    @Override
    public int getCostPrice() {
        return 100;
    }

    @Override
    public Double getSellPrice() {
        return 50.0;
    }

    @Override
    public Boolean isSellable() {
        return true;
    }
}
