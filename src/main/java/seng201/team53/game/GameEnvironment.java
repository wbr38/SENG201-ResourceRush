package seng201.team53.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseButton;
import seng201.team53.exceptions.TileNotFoundException;
import seng201.team53.game.assets.AssetLoader;
import seng201.team53.game.event.RandomEvents;
import seng201.team53.game.map.GameMap;
import seng201.team53.game.map.Tile;
import seng201.team53.game.round.GameRound;
import seng201.team53.game.round.GameRoundFactory;
import seng201.team53.game.state.GameState;
import seng201.team53.game.state.GameStateHandler;
import seng201.team53.gui.controller.GameController;
import seng201.team53.items.Cart;
import seng201.team53.items.Shop;

import java.util.Arrays;
import java.util.List;

/**
 * The overarching, main class of the game
 * This class is a Singleton; use .getGameEnvironment() to get the instance of this class and all its sub-components;
 */
public class GameEnvironment {
    private final GameStateHandler stateHandler = new GameStateHandler();
    private final GameController controller;
    private final AssetLoader assetLoader = new AssetLoader();
    private final RandomEvents randomEvents = new RandomEvents();
    private final GameRoundFactory roundFactory = new GameRoundFactory();
    private final Shop shop = new Shop();
    private final String playerName;
    private final int rounds;
    private GameDifficulty difficulty;
    private final IntegerProperty pointsProperty = new SimpleIntegerProperty(0);
    private final Property<GameRound> gameRoundProperty = new SimpleObjectProperty<>();
    private GameMap map;

    private static GameEnvironment instance;

    private GameEnvironment(GameController controller, String playerName, int rounds, GameDifficulty difficulty) {
        this.controller = controller;
        this.playerName = playerName;
        this.rounds = rounds;
        this.difficulty = difficulty;
    }

    /**
     * Initialises the GameEnvironment singleton. After calling this function, call gameEnvironment.load();
     */
    public static GameEnvironment init(GameController controller, String playerName, int rounds, GameDifficulty difficulty) {
        instance = new GameEnvironment(controller, playerName, rounds, difficulty);
        return instance;
    }

    public static GameEnvironment getGameEnvironment() {
        if (instance == null) {
            throw new RuntimeException("GameEnvironment has not been initialized!");
        }
        return instance;
    }

    public void load() {
        assetLoader.init();
        randomEvents.init();
        map = assetLoader.loadMap("default", "/assets/maps/map_one.json", controller.getMapBackgroundPane());
        setRound(roundFactory.getRound(1));

        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        shop.addMoney(difficulty.getStartingMoney());
    }

    public void setupNextRound() {
        int nextRound = getRound().getRoundNumber() + 1;
        setRound(roundFactory.getRound(nextRound));
        shop.addMoney(getRound().getMoneyEarned());
    }

    public void beginRound() {
        var randomEvent = randomEvents.requestRandomEvent();
        if (randomEvent != null) {
            randomEvent.apply();
            stateHandler.setState(GameState.RANDOM_EVENT_DIALOG_OPEN);
            controller.showRandomEventDialog(randomEvent.getClass().getSimpleName());
            return;
        }
        playRound();
    }

    public void playRound() {
        getRound().play();
    }

    public void pauseRound() {
        getRound().pause();
    }

    public void completeRound() {
        getRound().runRoundEndActions();
        addPoints(20);
        // todo - check if player won
        if (getRound().getRoundNumber() == rounds) {
            stateHandler.setState(GameState.GAME_COMPLETE);
            return;
        }
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

    public GameStateHandler getStateHandler() {
        return this.stateHandler;
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

    public Property<GameRound> getRoundProperty() {
        return gameRoundProperty;
    }

    public GameRound getRound() {
        return gameRoundProperty.getValue();
    }

    private void setRound(GameRound round) {
        gameRoundProperty.setValue(round);
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

    public GameMap getMap() {
        return map;
    }

    public int getPoints() {
        return pointsProperty.get();
    }

    public IntegerProperty getPointsProperty() {
        return pointsProperty;
    }

    public void addPoints(int points) {
        pointsProperty.set(pointsProperty.get() + points);
    }

    public String getPlayerName() {
        return playerName;
    }
}
