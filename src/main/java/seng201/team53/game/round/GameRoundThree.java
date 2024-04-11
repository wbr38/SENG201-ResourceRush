package seng201.team53.game.round;

import seng201.team53.game.map.Map;
import seng201.team53.items.ResourceType;

import java.util.EnumSet;

public class GameRoundThree extends GameRound {
    public GameRoundThree(Map map) {
        super(3, 1000, map);
    }

    @Override
    public void init() {
        createCart(50, 15, EnumSet.of(ResourceType.WOOD), 0);
        createCart(40, 15, EnumSet.of(ResourceType.ORE), 3);
        createCart(20, 15, EnumSet.of(ResourceType.FOOD), 6);
        createCart(50, 15, EnumSet.of(ResourceType.FOOD), 9);
        createCart(50, 15, EnumSet.of(ResourceType.FOOD), 12);
        createCart(50, 20, EnumSet.of(ResourceType.FOOD), 50);
        createCart(50, 20, EnumSet.of(ResourceType.FOOD), 55);
        createCart(50, 20, EnumSet.of(ResourceType.FOOD), 60);
        createCart(50, 30, EnumSet.of(ResourceType.FOOD), 65);
        createCart(50, 30, EnumSet.of(ResourceType.FOOD), 70);
        createCart(50, 30, EnumSet.of(ResourceType.FOOD), 72);
        createCart(50, 30, EnumSet.of(ResourceType.FOOD), 74);
        createCart(50, 30, EnumSet.of(ResourceType.FOOD), 76);
    }

    @Override
    public GameRound getNextRound() {
        return null;
    }
}
