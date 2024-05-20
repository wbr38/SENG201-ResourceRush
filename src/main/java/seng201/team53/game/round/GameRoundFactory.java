package seng201.team53.game.round;

import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.items.ResourceType;

import java.util.EnumSet;

public class GameRoundFactory {
    public GameRound getRound(int roundNumber) {

        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        GameRound round = new GameRound(roundNumber);
        
        // Add carts
        float cartVelocity = calculateCartVelocity(difficulty, roundNumber);
        int numCarts = difficulty.getNumberOfCarts(roundNumber);
        for (int i = 0; i < numCarts; i++) {
            var resourceTypes = switch (i % 4) {
                case 0 -> EnumSet.of(ResourceType.WOOD);
                case 1 -> EnumSet.of(ResourceType.STONE);
                case 2 -> EnumSet.of(ResourceType.ORE);
                default -> EnumSet.of(ResourceType.ENERGY);
            };

            final int spawnDelayTicks = 10;
            round.addCart(10, cartVelocity, resourceTypes, i * spawnDelayTicks);
        }
        return round;
    }

    private float calculateCartVelocity(GameDifficulty difficulty, int roundNumber) {
        float minVelocity = 5;
        float roundIncrease = 1.5f * (float)Math.log(roundNumber);
        return (minVelocity + roundIncrease) * difficulty.getCartVelocityMultiplier();
    }
}
