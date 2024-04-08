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
        setMap(App.getApp().getGameEnvironment().getMapLoader().loadMap("default", "/assets/maps/map_one.json"));
        createCart(100, 1, EnumSet.of(ResourceType.WOOD));
        createCart(100, 1, EnumSet.of(ResourceType.ORE));
        createCart(100, 1, EnumSet.of(ResourceType.FOOD));
    }
}
