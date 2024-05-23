package seng201.team53.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import seng201.team53.game.assets.AssetLoader;
import seng201.team53.game.event.RandomEvents;
import seng201.team53.game.map.GameMap;
import seng201.team53.game.round.GameRound;
import seng201.team53.game.round.GameRoundFactory;
import seng201.team53.game.state.GameState;
import seng201.team53.game.state.GameStateHandler;
import seng201.team53.gui.controller.GameController;
import seng201.team53.game.items.Shop;

/**
 * The overarching, main class of the game
 * This class is a Singleton; use .getGameEnvironment() to get the instance of this class and all its subcomponents;
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

    /**
     * Constructs a new game environment with the given parameters
     * @param controller The graphical controller of the game
     * @param playerName The name of the play
     * @param rounds The number of rounds to be played
     * @param difficulty The initial difficulty of the game
     */
    private GameEnvironment(GameController controller, String playerName, int rounds, GameDifficulty difficulty) {
        this.controller = controller;
        this.playerName = playerName;
        this.rounds = rounds;
        this.difficulty = difficulty;
    }

    /**
     * Loads the game environment. This includes initialising the asset loader, random events, map and creating
     * the first round
     */
    public void load() {
        assetLoader.init();
        randomEvents.init();
        map = assetLoader.loadMap("default", "/assets/maps/map_one.json", controller.getMapBackgroundPane());
        setRound(roundFactory.getRound(1));

        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        shop.addMoney(difficulty.getStartingMoney());
    }

    /**
     * Sets up the next round of the game
     */
    public void setupNextRound() {
        int nextRound = getRound().getRoundNumber() + 1;
        setRound(roundFactory.getRound(nextRound));
        shop.addMoney(getRound().getMoneyEarned());
    }

    /**
     * Begins the current round
     */
    public void beginRound() {
        var randomEvent = randomEvents.requestRandomEvent();
        if (randomEvent != null) {
            var towerType = randomEvent.apply();
            if (towerType != null) {
                stateHandler.setState(GameState.RANDOM_EVENT_DIALOG_OPEN);
                controller.showRandomEventDialog(randomEvent.getDescription(towerType));
            }
        }
    }

    /**
     * Completes the current round.
     * This method will run the current rounds end actions
     */
    public void completeRound() {
        getRound().runRoundEndActions();
        if (getRound().getRoundNumber() == rounds) {
            stateHandler.setState(GameState.GAME_COMPLETE);
        }
    }

    /**
     * Constructs a new game environment and initialises the singleton
     * After calling this function, call GameEnvironment#load();
     * @param controller The game controller
     * @param playerName The players name
     * @param rounds The number of rounds to be played
     * @param difficulty The game difficulty
     * @return The game environment
     */
    public static GameEnvironment init(GameController controller, String playerName, int rounds, GameDifficulty difficulty) {
        instance = new GameEnvironment(controller, playerName, rounds, difficulty);
        return instance;
    }

    /**
     * Retrieves the instance of the game environment singleton
     * @return The instance of the game environment singleton
     */
    public static GameEnvironment getGameEnvironment() {
        if (instance == null) {
            throw new RuntimeException("GameEnvironment has not been initialized!");
        }
        return instance;
    }

    /**
     * Retrieves the state handler
     * @return The state handler
     */
    public GameStateHandler getStateHandler() {
        return this.stateHandler;
    }

    /**
     * Retrieves the game controller
     * @return The game controller
     */
    public GameController getController() {
        return controller;
    }

    /**
     * Retrieves the random events
     * @return The random events
     */
    public RandomEvents getRandomEvents() {
        return randomEvents;
    }

    /**
     * Retrieves the asset loader
     * @return The asset loader
     */
    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    /**
     * Retrieves the shop
     * @return The shop
     */
    public Shop getShop() {
        return shop;
    }

    /**
     * Retrieves the game round property.
     * This property is observable, meaning it can be watched for changes
     * @return The observable game round property
     */
    public Property<GameRound> getRoundProperty() {
        return gameRoundProperty;
    }

    /**
     * Retrieves the current round
     * @return The current game round
     */
    public GameRound getRound() {
        return gameRoundProperty.getValue();
    }

    /**
     * Sets the current game round
     * @param round The new game round
     */
    private void setRound(GameRound round) {
        gameRoundProperty.setValue(round);
    }

    /**
     * Retrieves the current game difficulty
     * @return The game difficulty
     */
    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the new game difficulty
     * @param difficulty The new game difficulty
     */
    public void setDifficulty(GameDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Retrieves the number of rounds to be played
     * @return The number of total rounds
     */
    public int getRounds() {
        return rounds;
    }

    /**
     * Retrieves the game map
     * @return The game map
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Retrieves the points property.
     * This property is observable, meaning it can be watched for changes
     * @return The observable points property
     */
    public IntegerProperty getPointsProperty() {
        return pointsProperty;
    }

    /**
     * Retrieves the number of points gained throughout the game
     * @return The number of points
     */
    public int getPoints() {
        return pointsProperty.get();
    }

    /**
     * Increases the number of points gained throughout the game
     * @param points The points to be added
     */
    public void addPoints(int points) {
        pointsProperty.set(pointsProperty.get() + points);
    }

    /**
     * Retrieves the players name
     * @return The players name
     */
    public String getPlayerName() {
        return playerName;
    }
}
