package seng201.team53.game.round;

import seng201.team53.game.map.Map;
import seng201.team53.items.ResourceType;

import java.util.EnumSet;

public class GameRoundTwo extends GameRound {
    public GameRoundTwo(Map map) {
        super(2, 1000, map);
    }

    @Override
    public void init() {
        createCart(50, 15, EnumSet.of(ResourceType.WOOD), 0);
        createCart(40, 15, EnumSet.of(ResourceType.ORE), 10);
        createCart(20, 10, EnumSet.of(ResourceType.FOOD), 30);
        createCart(50, 10, EnumSet.of(ResourceType.FOOD), 40);
        createCart(50, 10, EnumSet.of(ResourceType.FOOD), 50);
    }

    @Override
    public GameRound getNextRound() {
        return new GameRoundThree(getMap());
    }
}
