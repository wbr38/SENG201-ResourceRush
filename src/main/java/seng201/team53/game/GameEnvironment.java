package seng201.team53.game;

import seng201.team53.game.map.MapLoader;
import seng201.team53.gui.GameController;
import seng201.team53.gui.GameWindow;

public class GameEnvironment {
    private final GameWindow gameWindow = new GameWindow();
    private final MapLoader mapLoader = new MapLoader();
    private final String playerName;
    private GameDifficulty difficulty;
    private final int rounds;
    private boolean paused = true;

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
    }

    public GameWindow getWindow() {
        return gameWindow;
    }
    public MapLoader getMapLoader() {
        return mapLoader;
    }

    public void setDifficulty(GameDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public int getRounds() {
        return rounds;
    }
}
