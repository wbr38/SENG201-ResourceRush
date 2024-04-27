package seng201.team53.game;

import seng201.team53.game.assets.AssetLoader;
import seng201.team53.game.event.RandomEvents;
import seng201.team53.game.map.Map;
import seng201.team53.game.round.GameRound;
import seng201.team53.game.round.GameRoundFactory;
import seng201.team53.gui.GameController;

public class GameEnvironment {
    private final GameStateHandler stateHandler;
    private final GameController controller;
    private final AssetLoader assetLoader = new AssetLoader();
    private final RandomEvents randomEvents = new RandomEvents();
    private final GameRoundFactory roundFactory = new GameRoundFactory();
    private final String playerName;
    private final int rounds;
    private GameDifficulty difficulty;
    private GameRound gameRound;
    private Map map;

    public GameEnvironment(GameStateHandler stateHandler, GameController controller, String playerName, int rounds, GameDifficulty difficulty) {
        this.stateHandler = stateHandler;
        this.controller = controller;
        this.playerName = playerName;
        this.rounds = rounds;
        this.difficulty = difficulty;
    }

    public void init() {
        stateHandler.setGameEnvironment(this);
        controller.init();
        assetLoader.init();
        randomEvents.init();
        map = assetLoader.loadMap("default", "/assets/maps/map_one.json", controller.getGridPane(), controller.getOverlay());
        gameRound = roundFactory.getRound(stateHandler, map, 1, assetLoader.getCartImage());
    }

    public void setupNextRound() {
        int nextRound = gameRound.getRoundNumber() + 1;
        gameRound = roundFactory.getRound(stateHandler, map, nextRound, assetLoader.getCartImage());
        if (gameRound == null)
            return;
        controller.updateRoundCounter(nextRound, rounds);
    }
    public void beginRound() {
        var randomEvent = randomEvents.requestRandomEvent(difficulty);
        if (randomEvent != null) {
            stateHandler.setState(GameState.RANDOM_EVENT_DIALOG_OPEN);
            controller.showRandomEventDialog(randomEvent.getClass().getSimpleName());
            return;
        }
        startRound();
    }
    public void startRound() {
        gameRound.start();
        controller.showPauseButton();
    }
    public void pauseRound() {
        gameRound.pause();
        controller.showResumeButton();
    }
    public void resumeRound() {
        gameRound.play();
        controller.showPauseButton();
    }
    public void completeRound() {
        if (gameRound.getRoundNumber() == rounds) {
            stateHandler.setState(GameState.GAME_COMPLETE);
            return;
        }
        controller.showStartButton();
        controller.showRoundCompleteDialog();
    }

    public GameController getController() {
        return controller;
    }
    public AssetLoader getAssetLoader() {
        return assetLoader;
    }
    public RandomEvents getRandomEvents() {
        return randomEvents;
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
}
