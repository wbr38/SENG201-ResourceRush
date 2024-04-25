package seng201.team53.game;

import seng201.team53.game.assets.AssetLoader;
import seng201.team53.game.event.RandomEvents;
import seng201.team53.game.round.GameRound;
import seng201.team53.game.round.GameRoundOne;
import seng201.team53.gui.GameController;
import seng201.team53.gui.GameWindow;

public class GameEnvironment {
    private final GameWindow gameWindow = new GameWindow();
    private final AssetLoader assetLoader = new AssetLoader();
    private final RandomEvents randomEvents = new RandomEvents();
    private final GameStateHandler stateHandler = new GameStateHandler();
    private final String playerName;
    private final int rounds;
    private GameDifficulty difficulty;
    private GameRound gameRound;

    // TODO
    // public Inventory inventory;
    // public double money;
    // public Shop shop;

    public GameEnvironment(String playerName, int rounds, GameDifficulty difficulty) {
        this.playerName = playerName;
        this.rounds = rounds;
        this.difficulty = difficulty;
    }

    public void init() throws Exception {
        gameWindow.start();

        GameController gameController = gameWindow.getController();
        gameController.setGame(this);
        gameController.init();
        assetLoader.init();
        randomEvents.init();
        gameRound = new GameRoundOne();
        gameRound.init();
    }

    public GameWindow getWindow() {
        return gameWindow;
    }
    public GameController getController() {
        return gameWindow.getController();
    }
    public AssetLoader getAssetLoader() {
        return assetLoader;
    }
    public RandomEvents getRandomEvents() {
        return randomEvents;
    }
    public GameStateHandler getStateHandler() {
        return stateHandler;
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

    public GameRound getRound() {
        return gameRound;
    }
    public boolean goNextRound() {
        // check win condition here?
        gameRound = gameRound.getNextRound();
        if (gameRound == null)
            return false;
        gameRound.init();
        getController().updateRoundCounter(gameRound.getRoundNumber());
        return true;
    }
}
