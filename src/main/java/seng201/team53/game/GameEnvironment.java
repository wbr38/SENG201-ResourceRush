package seng201.team53.game;

import javafx.scene.input.MouseButton;
import seng201.team53.exceptions.TileNotFoundException;
import seng201.team53.game.assets.AssetLoader;
import seng201.team53.game.event.RandomEvents;
import seng201.team53.game.map.Map;
import seng201.team53.game.map.Tile;
import seng201.team53.game.round.GameRound;
import seng201.team53.game.round.GameRoundFactory;
import seng201.team53.game.state.GameState;
import seng201.team53.game.state.GameStateHandler;
import seng201.team53.gui.GameController;
import seng201.team53.items.Shop;

import java.util.Arrays;

public class GameEnvironment {
    private final GameStateHandler stateHandler;
    private final GameController controller;
    private final AssetLoader assetLoader = new AssetLoader();
    private final RandomEvents randomEvents = new RandomEvents();
    private final GameRoundFactory roundFactory = new GameRoundFactory();
    private final Shop shop = new Shop();
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
        assetLoader.init();
        randomEvents.init();
        map = assetLoader.loadMap("default", "/assets/maps/map_one.json", controller.getMapBackgroundPane(), controller.getGridPane(),
                                  controller.getOverlay());
        gameRound = roundFactory.getRound(stateHandler, map, 1, assetLoader.getCartImage());
        shop.addMoney(gameRound.getStartingMoney());
        controller.updateMoneyLabel(shop.getMoney());
        controller.updateShopButtons(shop.getMoney());
    }

    public void setupNextRound() {
        int nextRound = gameRound.getRoundNumber() + 1;
        gameRound = roundFactory.getRound(stateHandler, map, nextRound, assetLoader.getCartImage());
        if (gameRound == null)
            return;
        shop.addMoney(gameRound.getStartingMoney());
        controller.updateRoundCounter(nextRound, rounds);
        controller.updateMoneyLabel(shop.getMoney());
        controller.updateShopButtons(shop.getMoney());
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

    // to create the path and buildable matrix stuff
    public void enableMapCreationMode() {
        int[][] matrix = new int[16][20];
        controller.getOverlay().setOnMouseClicked(event -> {
            Tile tile;
            try {
                tile = map.getTileFromScreenPosition((int)event.getSceneX(), (int)event.getSceneY());
            } catch (TileNotFoundException e) {
                return;
            }
            if (event.getButton() == MouseButton.PRIMARY) {
                matrix[tile.getY()][tile.getX()] = 1;
                return;
            }
            if (event.getButton() == MouseButton.SECONDARY) {
                matrix[tile.getY()][tile.getX()] = 2;
                return;
            }
            if (event.getButton() == MouseButton.MIDDLE) {
                // print the matrix
                for (int[] row : matrix) {
                    System.out.println(Arrays.toString(row));
                }
            }
        });
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

    public Shop getShop() {
        return shop;
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

    public Map getMap() {
        return map;
    }
}
