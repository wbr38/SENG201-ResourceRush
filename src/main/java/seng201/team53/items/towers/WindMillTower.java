package seng201.team53.items.towers;

import seng201.team53.items.ResourceType;

public class WindMillTower extends Tower {

    public static final int COST = 200;

    public WindMillTower() {
        super(
                "Wind Mill",
                "/assets/items/wind_mill_tower.png",
                ResourceType.ORE);
    }

    @Override
    public String getDescription() {
        return "A wind mill produces energy";
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
