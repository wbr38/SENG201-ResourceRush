package seng201.team53.items.towers;

import seng201.team53.items.ResourceType;

public class QuarryTower extends Tower {

    public static final int COST = 150;

    public QuarryTower() {
        super(
                "Quarry",
                "/assets/items/wood_tower.png",
                ResourceType.ORE,
                TowerType.QUARRY);
    }

    @Override
    public String getDescription() {
        return "A Quarry produces stone";
    }

    @Override
    public int getCostPrice() {
        return COST;
    }

    @Override
    public int getSellPrice() {
        return 50;
    }

    @Override
    public Boolean isSellable() {
        return true;
    }
}
