package seng201.team53.game.round;

import java.util.ArrayList;
import java.util.List;

import seng201.team53.items.Cart;

public abstract class GameRound {
    private final int roundNumber;
    private final List<Cart> carts = new ArrayList<>();
    private final double startingMoney;

    GameRound(int roundNumber, int startingMoney) {
        this.roundNumber = roundNumber;
        this.startingMoney = startingMoney;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void start() {
        // Spawn/populate this.carts with a number of carts, based on the GameDifficulty chosen
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }

    public Boolean checkWinCondition() {
        // Check if all carts have been filled
        throw new UnsupportedOperationException("Unimplemented method 'checkWinCondition'");
    }

    public Boolean checkLoseCondition() {
        // Check if any carts have reached the end of the track that have not been filled
        throw new UnsupportedOperationException("Unimplemented method 'checkLoseCondition'");
    }

    private void update() {
        // Game tick / update function
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
}
