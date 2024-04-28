package seng201.team53.items.towers;

import seng201.team53.items.ResourceType;

public class LumberMillTower extends Tower {

    public static final int COST = 100;

    public LumberMillTower() {

        super(
                "Lumber Mill",
                "/assets/items/wood_tower.png",
                ResourceType.WOOD);
    }

    @Override
    public String getDescription() {
        return "A Lumber Mill produces wood";
    }

    @Override
    public int getCostPrice() {
        return COST;
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
