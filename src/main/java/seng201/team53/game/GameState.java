package seng201.team53.game;

public enum GameState {
    ROUND_ACTIVE,
    ROUND_PAUSE,
    ROUND_COMPLETE,
    ROUND_NOT_STARTED;


    // prob dont need both ROUND_COMPLETE & ROUND_NOT_STARTED but idk
    public boolean hasRoundStarted() {
        return this != ROUND_NOT_STARTED;
    }
}
