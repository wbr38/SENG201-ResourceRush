package seng201.team53.items.towers.type;

import seng201.team53.items.towers.*;

import java.util.function.Supplier;

public enum TowerType {
    LUMBER_MILL(LumberMillTower::new),
    MINE(MineTower::new),
    QUARRY(QuarryTower::new),
    WIND_MILL(WindMillTower::new);

    private Supplier<Tower> factory;

    TowerType(Supplier<Tower> factory) {
        this.factory = factory;
    }

    public Tower create() {
        return factory.get();
    }
}
