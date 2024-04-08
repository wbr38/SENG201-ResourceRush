package seng201.team53.game;

import seng201.team53.game.map.AssetLoader;
import seng201.team53.game.round.GameRound;
import seng201.team53.game.round.GameRoundOne;
import seng201.team53.gui.GameController;
import seng201.team53.gui.GameWindow;

public class GameEnvironment {
    private final GameWindow gameWindow = new GameWindow();
    private final AssetLoader mapLoader = new AssetLoader();
    private final String playerName;
    private GameDifficulty difficulty;
    private final int rounds;
    private boolean paused = true;
    private GameRound gameRound;

    // TODO
    // public Inventory inventory;
    // public double money;
    // public GameRound currentRound;
    // public Shop shop;

    public GameEnvironment(String playerName, int rounds, GameDifficulty difficulty) {
        this.playerName = playerName;
        this.rounds = rounds;
        this.difficulty = difficulty;
    }

    public void init() throws Exception {
        gameWindow.start();

        GameController gameController = gameWindow.getController();
        gameController.init();
        mapLoader.init();
        gameRound = new GameRoundOne();
        gameRound.init();
    }

    public GameWindow getWindow() {
        return gameWindow;
    }
    public AssetLoader getMapLoader() {
        return mapLoader;
    }

    public GameDifficulty getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(GameDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getRounds() {
        return rounds;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public GameRound getRound() {
        return gameRound;
    }
}
