package seng201.team53.items.towers;

import seng201.team53.items.ResourceType;

public class MineTower extends Tower {

    public static final int COST = 120;

    public MineTower() {
        super(
                "Mine",
                "/assets/items/wood_tower.png",
                ResourceType.ORE);
    }

    @Override
    public String getDescription() {
        return "A Mine produces ores";
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
