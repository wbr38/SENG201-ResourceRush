package seng201.team53.game.round;

import javafx.util.Duration;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.items.ResourceType;

public class GameRoundFactory {
    public GameRound getRound(int roundNumber) {

        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        GameRound round = new GameRound(roundNumber);
        
        // Add carts
        float cartVelocity = calculateCartVelocity(difficulty, roundNumber);
        int numCarts = difficulty.getNumberOfCarts(roundNumber);
        for (int i = 0; i < numCarts; i++) {
            var resourceType = switch (i % 4) {
                case 0 -> ResourceType.WOOD;
                case 1 -> ResourceType.STONE;
                case 2 -> ResourceType.ORE;
                default -> ResourceType.ENERGY;
            };

            Duration spawnDelay = Duration.seconds(0.75 * i);
            round.addCart(10, cartVelocity, resourceType, spawnDelay);
        }
        return round;
    }

    private float calculateCartVelocity(GameDifficulty difficulty, int roundNumber) {
        float minVelocity = 5;
        float roundIncrease = 1.5f * (float)Math.log(roundNumber);
        return (minVelocity + roundIncrease) * difficulty.getCartVelocityMultiplier();
    }
}
