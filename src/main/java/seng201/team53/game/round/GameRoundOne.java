package seng201.team53.game.round;

import seng201.team53.items.ResourceType;

import java.util.EnumSet;

import static seng201.team53.App.getGameEnvironment;

public class GameRoundOne extends GameRound {
    public GameRoundOne() {
        super(1, 1000.0);
    }

    @Override
    public void init() {
        setMap(getGameEnvironment().getAssetLoader().loadMap("default", "/assets/maps/map_one.json"));
        createCart(50, 10, EnumSet.of(ResourceType.WOOD), 0);
        createCart(40, 10, EnumSet.of(ResourceType.ORE), 20);
        createCart(20, 10, EnumSet.of(ResourceType.FOOD), 40);
    }

    @Override
    public GameRound getNextRound() {
        return new GameRoundTwo(getMap());
    }
}
