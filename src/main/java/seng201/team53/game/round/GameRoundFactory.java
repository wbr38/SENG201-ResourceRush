package seng201.team53.game.round;

import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.items.ResourceType;

import java.util.EnumSet;

public class GameRoundFactory {
    public GameRound getRound(int roundNumber) {

        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        float cartVelocity = calculateCartVelocity(difficulty, roundNumber);

        GameRound round = new GameRound(roundNumber);
        int counter = 0;
        for (int i = 0; i < roundNumber; i++) {
            var resourceTypes = switch (i % 4) {
                case 0 -> EnumSet.of(ResourceType.WOOD);
                case 1 -> EnumSet.of(ResourceType.ORE);
                case 2 -> EnumSet.of(ResourceType.STONE);
                default -> EnumSet.of(ResourceType.ENERGY);
            };

            for (int j = 0; j <= 1; j++) {
                round.addCart(10, cartVelocity, resourceTypes, counter * 10);
                counter++;
            }
        }
        return round;
    }

    private float calculateCartVelocity(GameDifficulty difficulty, int roundNumber) {
        float minVelocity = 5;
        float roundIncrease = 1.5f * (float)Math.log(roundNumber);
        return (minVelocity + roundIncrease) * difficulty.getCartVelocityMultiplier();
    }
}
