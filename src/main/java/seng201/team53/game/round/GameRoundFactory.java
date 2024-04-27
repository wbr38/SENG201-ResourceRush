package seng201.team53.game.round;

import javafx.scene.image.Image;
import seng201.team53.game.GameStateHandler;
import seng201.team53.game.map.Map;

import java.util.EnumSet;

import static seng201.team53.items.ResourceType.WOOD;

public class GameRoundFactory {
    public GameRound getRound(GameStateHandler stateHandler, Map map, int roundNumber, Image cartImage) {
        return new GameRound(stateHandler, map, roundNumber, 100) {{
            addCart(cartImage, 10, 15, EnumSet.of(WOOD), 0);
            addCart(cartImage, 10, 15, EnumSet.of(WOOD), 10);
            addCart(cartImage, 10, 15, EnumSet.of(WOOD), 20);
        }};
    }
}
