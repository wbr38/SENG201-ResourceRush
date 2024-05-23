package seng201.team53.game.round;

import javafx.util.Duration;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.items.ResourceType;

/**
 * This class is responsible for creating instances of the GameRound class
 * It uses the games difficulty and round number to determine the number of carts and velocities
 */
public class GameRoundFactory {

    /**
     * Creates a new GameRound instance
     * The properties of the round are determined based on the games difficulty and round number
     * @param roundNumber The number of the round
     * @return A new GameRound instance
     */
    public GameRound getRound(int roundNumber) {
        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        GameRound round = new GameRound(roundNumber);

        float cartVelocity = calculateCartVelocity(difficulty, roundNumber);
        int numCarts = difficulty.getNumberOfCarts(roundNumber);
        for (int i = 0; i < numCarts; i++) {
            ResourceType resourceType = switch (i % 4) {
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

    /**
     * Calculates the velocity of the carts in a round
     * The velocity is determined based on the games difficulty and round number
     * @param difficulty The games difficulty
     * @param roundNumber The number of the round
     * @return The calculated cart velocity
     */
    private float calculateCartVelocity(GameDifficulty difficulty, int roundNumber) {
        float minVelocity = 5;
        float roundIncrease = 1.5f * (float)Math.log(roundNumber);
        return (minVelocity + roundIncrease) * difficulty.getCartVelocityMultiplier();
    }
}
