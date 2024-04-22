package seng201.team53.items.towers;

import seng201.team53.items.ResourceType;

public class WindMillTower extends Tower {

    public WindMillTower() {
        super(
            "Wind Mill", "/assets/wind_mill_tower.png", ResourceType.ORE);
    }

    @Override
    public String getDescription() {
        return "A wind mill produces energy";
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
}
