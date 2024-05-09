package seng201.team53.game.round;

import javafx.scene.image.Image;
import seng201.team53.game.state.GameStateHandler;
import seng201.team53.game.map.GameMap;
import seng201.team53.items.ResourceType;

import java.util.EnumSet;

public class GameRoundFactory {
    public GameRound getRound(GameStateHandler stateHandler, GameMap map, int roundNumber, Image cartImage) {
        int startingMoney = 2000 - (roundNumber * 10);
        float velocity = 5 + (roundNumber * 2);
        var round = new GameRound(stateHandler, map, roundNumber, startingMoney);
        int counter = 0;
        for (int i = 0; i < roundNumber; i++) {
            var resourceTypes = switch (i % 4) {
                case 0 -> EnumSet.of(ResourceType.WOOD);
                case 1 -> EnumSet.of(ResourceType.ORE);
                case 2 -> EnumSet.of(ResourceType.STONE);
                default -> EnumSet.of(ResourceType.ENERGY);
            };
            for (int j = 0; j <= 1; j++) {
                round.addCart(cartImage, 10, velocity, resourceTypes, counter * 10);
                counter++;
            }
        }
        return round;
    }
}
