package seng201.team53.game;

import seng201.team53.game.assets.AssetLoader;
import seng201.team53.game.map.Map;
import seng201.team53.game.round.GameRound;
import seng201.team53.game.round.GameRoundOne;
import seng201.team53.gui.GameController;
import seng201.team53.gui.GameWindow;
import seng201.team53.items.Shop;
import seng201.team53.items.towers.Tower;

public class GameEnvironment {
    private final GameWindow gameWindow = new GameWindow();
    private final AssetLoader assetLoader = new AssetLoader();
    private final String playerName;
    private final int rounds;
    private GameDifficulty difficulty;
    private GameState gameState = GameState.ROUND_NOT_STARTED;
    private GameRound gameRound;
    private Shop shop = new Shop();

    // TODO
    // public Inventory inventory;
    // public double money;

    public GameEnvironment(String playerName, int rounds, GameDifficulty difficulty) {
        this.playerName = playerName;
        this.rounds = rounds;
        this.difficulty = difficulty;
    }

    public void init() throws Exception {
        gameWindow.start();

        GameController gameController = gameWindow.getController();
        gameController.init();
        assetLoader.init();
        gameRound = new GameRoundOne();
        this.loadRound(gameRound);
    }

    public void tryPurchaseTower(Tower tower) {
        Map map = this.gameRound.getMap();

        // Attempt to purchase tower
        Boolean purchased = this.shop.purchaseItem(tower);
        if (!purchased) {
            GameController gameController = gameWindow.getController();
            gameController.showNotification("Not enough money", 1.5);
            return;
        }

        GameController gameController = gameWindow.getController();
        gameController.updateMoneyLabel(this.shop.getMoney());
        map.startPlacingTower(tower);
    }

    public GameWindow getWindow() {
        return gameWindow;
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
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

    public GameState getState() {
        return gameState;
    }

    public void setState(GameState gameState) {
        var controller = getWindow().getController();
        var previousState = this.gameState;
        this.gameState = gameState;
        switch (gameState) {
            case ROUND_COMPLETE -> {
                controller.showStartButton();
                gameRound.stop();

                gameRound = gameRound.getNextRound();
                // check win or lose condition
                if (gameRound == null) { // put game into dead state for testing purposes
                    gameState = GameState.DEAD;
                    return;
                }

                this.loadRound(gameRound);
            }
            case ROUND_ACTIVE -> {
                controller.showPauseButton();
                if (previousState == GameState.ROUND_NOT_STARTED || previousState == GameState.ROUND_COMPLETE)
                    gameRound.start();
                else
                    gameRound.play();
            }
            case ROUND_PAUSE -> {
                controller.showResumeButton();
                gameRound.pause();
            }
            case DEAD -> {
                // TODO
            }
        }
    }

    private void loadRound(GameRound gameRound) {
        gameRound.init();
        this.shop.addMoney(gameRound.startingMoney);

        GameController gameController = gameWindow.getController();
        gameController.updateMoneyLabel(this.shop.getMoney());
        gameController.updateRoundCounter(gameRound.getRoundNumber());
    }

    public GameRound getRound() {
        return gameRound;
    }
}
