package seng201.team53.game.state;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import seng201.team53.game.GameEnvironment;

/**
 * This class manages the game state within the game environment. It keeps track of the current game state
 * and handles transitions between different states
 */
public class GameStateHandler {
    private final Property<GameState> gameStateProperty = new SimpleObjectProperty<>(GameState.ROUND_NOT_STARTED);
    private GameState previousState;

    public Property<GameState> getGameStateProperty() {
        return gameStateProperty;
    }

    /**
     * Returns the current game state
     * @return The current GameState
     */
    public GameState getState() {
        return gameStateProperty.getValue();
    }

    /**
     * Returns the previous game state
     * @return The previous GameState
     */
    public GameState getPreviousState() {
        return previousState;
    }

    /**
     * Sets the game state to the given value and performs actions specific to the new state
     * This method updates the current state and previous state and calls a specific handler method based on the new
     * state value
     * @param gameState The new game state to set.
     */
    public void setState(GameState gameState) {
        previousState = getState();
        gameStateProperty.setValue(gameState);

        switch (gameState) {
            case ROUND_NOT_STARTED -> handleChangedGameStateRoundNotStarted();
            case ROUND_ACTIVE -> handleChangedGameStateRoundActive();
            case ROUND_PAUSE -> handleChangedGameStateRoundPause();
            case ROUND_COMPLETE -> handleChangedGameStateRoundComplete();
            case RANDOM_EVENT_DIALOG_OPEN -> handleChangedGameStateRandomEventDialogOpen();
            case GAME_COMPLETE -> handleChangedGameStateGameComplete();
        }
    }

    /**
     * Handles logic specific to the ROUND_NOT_STARTED state transition.
     */
    private void handleChangedGameStateRoundNotStarted() {
        GameEnvironment gameEnv = GameEnvironment.getGameEnvironment();
        gameEnv.setupNextRound();
    }

    /**
     * Handles logic specific to the ROUND_ACTIVE state transition.
     */
    private void handleChangedGameStateRoundActive() {
        GameEnvironment gameEnv = GameEnvironment.getGameEnvironment();
        if (previousState == GameState.ROUND_NOT_STARTED) {
            gameEnv.beginRound();
            return;
        }
        gameEnv.playRound();
    }

    /**
     * Handles logic specific to the ROUND_PAUSE state transition.
     */
    private void handleChangedGameStateRoundPause() {
        GameEnvironment gameEnv = GameEnvironment.getGameEnvironment();
        gameEnv.pauseRound();
    }

    /**
     * Handles logic specific to the ROUND_COMPLETE state transition.
     */
    private void handleChangedGameStateRoundComplete() {
        GameEnvironment gameEnv = GameEnvironment.getGameEnvironment();
        gameEnv.completeRound();
    }

    /**
     * Handles logic specific to the RANDOM_EVENT_DIALOG_OPEN state transition (implementation pending).
     */
    private void handleChangedGameStateRandomEventDialogOpen() {

    }

    /**
     * Handles logic specific to the GAME_COMPLETE state transition (implementation pending).
     */
    private void handleChangedGameStateGameComplete() {

    }
}
