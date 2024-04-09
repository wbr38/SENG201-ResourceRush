package seng201.team53.game.round;

import seng201.team53.App;
import seng201.team53.items.ResourceType;

import java.util.EnumSet;

public class GameRoundOne extends GameRound {
    public GameRoundOne() {
        super(1, 1000.0);
    }

    @Override
    public void init() {
        var gameEnvironment = App.getApp().getGameEnvironment();
        gameEnvironment.getWindow().getController().updateRoundCounter(getRoundNumber());
        setMap(gameEnvironment.getMapLoader().loadMap("default", "/assets/maps/map_one.json"));
        createCart(50, 1, EnumSet.of(ResourceType.WOOD), 0);
        createCart(40, 2, EnumSet.of(ResourceType.ORE), 20);
        createCart(20, 3, EnumSet.of(ResourceType.FOOD), 40);
    }
    @Override
    public GameRound getNextRound() {
        // if we are going to use the same map for the next round
        // then we can call new GameRoundTwo(-, -, map)
        // then we don't have to load and re draw the map - would be pointless
        throw new IllegalStateException("No next round");
    }
}
