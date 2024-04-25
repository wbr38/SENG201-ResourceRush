package seng201.team53.game;

import javafx.scene.control.TextField;

import static seng201.team53.App.getGameEnvironment;

/**
 * This class manages the game state within the game environment. It keeps track of the current game state
 *  and handles transitions between different states
 */
public class GameStateHandler {
    private GameState state = GameState.ROUND_NOT_STARTED;
    private GameState previousState;

    /**
     * Returns the current game state
     * @return The current GameState
     */
    public GameState getState() {
        return state;
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
        this.previousState = this.state;
        this.state = gameState;

        // debug
        TextField field = getGameEnvironment().getController().stateTextField;
        field.setText(gameState.name() + " --- " + field.getText());

        switch (state) {
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
        if (!getGameEnvironment().goNextRound())
            return; // atm goes into dead state
        getGameEnvironment().getController().showStartButton();
    }

    /**
     * Handles logic specific to the ROUND_ACTIVE state transition.
     */
    private void handleChangedGameStateRoundActive() {
        var round = getGameEnvironment().getRound();
        getGameEnvironment().getController().showPauseButton();
        if (previousState == GameState.ROUND_NOT_STARTED || previousState == GameState.RANDOM_EVENT_DIALOG_OPEN) {
            round.start();
            return;
        }
        round.play();
    }

    /**
     * Handles logic specific to the ROUND_PAUSE state transition.
     */
    private void handleChangedGameStateRoundPause() {
        var round = getGameEnvironment().getRound();
        getGameEnvironment().getController().showResumeButton();
        round.pause();
    }

    /**
     * Handles logic specific to the ROUND_COMPLETE state transition.
     */
    private void handleChangedGameStateRoundComplete() {
        getGameEnvironment().getController().showRoundCompleteDialog();
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
