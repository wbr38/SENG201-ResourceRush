package seng201.team53.gui.controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.state.GameState;
import seng201.team53.game.state.GameStateHandler;
import seng201.team53.gui.wrapper.FXWrappers;
import seng201.team53.items.Item;
import seng201.team53.items.Purchasable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class GameController {
    @FXML private Pane overlay;
    @FXML private GridPane gridPane;

    @FXML private Pane mapBackgroundPane;
    @FXML private Pane roundCompletePane;

    @FXML private Pane randomEventPane;
    @FXML private Text randomEventTest;

    // Info section (top-middle of screen)
    @FXML protected Text moneyLabel;
    @FXML private Text roundCounterLabel;
    @FXML private Text pointsLabel;
    @FXML private Text notificationLabel;
    private PauseTransition notificationPause;

    @FXML private Button pauseButton;
    @FXML private Button startButton;
    @FXML private Button resumeButton;

    // Shop
    @FXML protected Pane shopPane;
    @FXML protected Button shopTowerButton1;
    @FXML protected Button shopTowerButton2;
    @FXML protected Button shopTowerButton3;
    @FXML protected Button shopTowerButton4;

    @FXML protected Button shopItemButton1;
    @FXML protected Button shopItemButton2;
    @FXML protected Button shopItemButton3;
    @FXML protected Button shopItemButton4;

    // Sell Tower popup
    @FXML protected AnchorPane sellItemPane;
    @FXML private Text sellItemText;

    // Inventory
    @FXML private AnchorPane inventoryPane;
    @FXML protected Button inventoryButton1;
    @FXML protected Button inventoryButton2;
    @FXML protected Button inventoryButton3;
    @FXML protected Button inventoryButton4;

    // End Screen
    @FXML private Button toggleInventoryButton;
    @FXML private Pane gameEndPane;
    @FXML private Text gameEnd_WinLoseText;
    @FXML private Text gameEnd_NameText;
    @FXML private Text gameEnd_RoundsText;
    @FXML private Text gameEnd_MoneyEarnt;
    @FXML private Text gameEnd_Points;
    @FXML private Button gameEnd_ExitButton;

    // sub-controllers
    private final MapInteractionController mapInteractionController = new MapInteractionController(this);
    private final InventoryController inventoryController = new InventoryController(this, mapInteractionController);
    private final ShopController shopController = new ShopController(this);
    private final FXWrappers fxWrappers = new FXWrappers();

    public void init() {
        toggleInventoryVisible(false);
        this.showSellItemPopup(null);

        // Update round number
        getGameEnvironment().getRoundProperty().addListener(($, oldRound, newRound) -> updateRoundCounter(newRound.getRoundNumber()));
        // Update point count
        getGameEnvironment().getPointsProperty().addListener(($, oldPoints, newPoints) -> updatePointsLabel(newPoints.intValue()));

        // The following labels need to be updated here, as GameEnvironment.load() is called before these listeners are added.
        // So the labels aren't up to date when the game loads.
        updateRoundCounter(1);
        updatePointsLabel(0);

        // Background music loop
        String resource = getClass().getResource("/assets/sound/piano-groove-2.wav").toString();
        MediaPlayer music = new MediaPlayer(new Media(resource));
        music.setCycleCount(MediaPlayer.INDEFINITE);
        music.play();

        var stateHandler = getGameEnvironment().getStateHandler();
        stateHandler.getGameStateProperty().addListener(($, oldState, newState) -> {
            switch (newState) {
                case RANDOM_EVENT_DIALOG_OPEN -> showStartButton();
                case ROUND_ACTIVE -> showPauseButton();
                case ROUND_PAUSE -> showResumeButton();
                case ROUND_NOT_STARTED -> {
                    showStartButton();
                    hide(roundCompletePane);
                }
                case ROUND_COMPLETE -> {
                    boolean roundWon = getGameEnvironment().getRound().roundWon();
                    if (!roundWon) {
                        showGameEndPopup();
                        return;
                    }

                    showStartButton();
                    show(roundCompletePane);
                }
                case GAME_COMPLETE -> {
                    showGameEndPopup();
                }
            }
            if (oldState == GameState.RANDOM_EVENT_DIALOG_OPEN)
                hide(randomEventPane);
        });

        shopController.init();
        mapInteractionController.init();
        inventoryController.init();
        fxWrappers.init();
    }

    public Pane getMapBackgroundPane() {
        return mapBackgroundPane;
    }

    public Pane getOverlay() {
        return overlay;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public FXWrappers getFXWrappers() {
        return fxWrappers;
    }

    @FXML
    private void onStartButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.ROUND_NOT_STARTED)
            return;

        stateHandler.setState(GameState.ROUND_ACTIVE);
        toggleInventoryVisible(false);
        this.showSellItemPopup(null);
    }

    @FXML
    private void onPauseButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.ROUND_ACTIVE)
            return;

        getGameEnvironment().pauseRound();
        stateHandler.setState(GameState.ROUND_PAUSE);
    }

    @FXML
    private void onResumeButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.ROUND_PAUSE)
            return;

        getGameEnvironment().playRound();
        stateHandler.setState(GameState.ROUND_ACTIVE);
    }

    @FXML
    private void onInventoryButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        toggleInventoryVisible();
    }

    @FXML
    private void onSellItemButtonClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        this.mapInteractionController.sellSelectedItem();
        this.showSellItemPopup(null);
    }

    @FXML
    private void onRandomEventDialogExistClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.RANDOM_EVENT_DIALOG_OPEN)
            return;

        stateHandler.setState(GameState.ROUND_ACTIVE);
    }

    @FXML
    private void onRoundCompleteDialogExistClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.ROUND_COMPLETE)
            return;

        stateHandler.setState(GameState.ROUND_NOT_STARTED);
    }

    public void showNotification(String text, double duration) {
        notificationLabel.setText(text);
        show(notificationLabel);

        // Pause previous delay (otherwise the previous will hide the label prematurely)
        if (notificationPause != null)
            notificationPause.stop();

        // Hide the label after x seconds
        notificationPause = new PauseTransition(Duration.seconds(duration));
        notificationPause.setOnFinished(event -> hide(notificationLabel));
        notificationPause.play();
    }

    /**
     * Toggle the visibility of the inventory pane
     */
    private void toggleInventoryVisible() {
        toggleInventoryVisible(!inventoryPane.isVisible());
    }

    /**
     * Set the visibility of the inventory pane
     */
    private void toggleInventoryVisible(boolean visible) {
        if (visible)
            show(inventoryPane);
        else
            hide(inventoryPane);
    }

    /**
     * Update the appropriate elements to allow the user to sell an item.
     * @param item The item to sell. Set to `null` to hide this popup
     */
    public void showSellItemPopup(Item item) {
        if (item == null) {
            hide(sellItemPane);
            return;
        }

        sellItemText.setText("Sell ($" + item.getPurchasableType().getSellPrice() + ")");
        sellItemPane.toFront();
        show(sellItemPane);
    }

    private void updateRoundCounter(int currentRound) {
        int totalRounds = getGameEnvironment().getRounds();
        roundCounterLabel.setText(currentRound + "/" + totalRounds);
    }

    private void updatePointsLabel(int points) {
        pointsLabel.setText("" + points);
    }

    private void showStartButton() {
        show(startButton);
        hide(pauseButton);
        hide(resumeButton);
    }

    private void showPauseButton() {
        hide(startButton);
        show(pauseButton);
        hide(resumeButton);
    }

    private void showResumeButton() {
        hide(startButton);
        hide(pauseButton);
        show(resumeButton);
    }

    public void showRandomEventDialog(String text) {
        randomEventTest.setText(text);
        show(randomEventPane);
    }

    private void showGameEndPopup() {
        show(gameEndPane);

        GameEnvironment gameEnv = getGameEnvironment();
        boolean gameWon = gameEnv.getRound().roundWon();
        String playerName = gameEnv.getPlayerName();
        int currentRound = gameEnv.getRound().getRoundNumber();
        int maxRounds = gameEnv.getRounds();
        int money = gameEnv.getShop().getMoney();
        int points = gameEnv.getPoints();

        gameEnd_WinLoseText.setText(gameWon ? "You Won!" : "You Lost :(");
        gameEnd_NameText.setText("Name: " + playerName);
        gameEnd_RoundsText.setText("Rounds Completed: " + currentRound + "/" + maxRounds);
        gameEnd_MoneyEarnt.setText("Money Earnt: $" + money);
        gameEnd_Points.setText("Points: " + points);

        // Hide some elements
        hide(startButton);
        hide(pauseButton);
        hide(startButton);
        hide(resumeButton);

        hide(toggleInventoryButton);
        hide(inventoryPane);
        shopPane.setDisable(true);
    }

    @FXML
    private void onGameEnd_QuitButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        Platform.exit();
        System.exit(0);
    }

    private void show(Node node) {
        node.setVisible(true);
        node.setDisable(false);
    }

    private void hide(Node node) {
        node.setVisible(false);
        node.setDisable(true);
    }

    public void updateButton(Button button, Purchasable purchasable) {
        if (purchasable == null) {
            button.setGraphic(null);
            button.getTooltip().setText(null);
            button.setText("Add Tower");
            return;
        }
        Image image = getGameEnvironment().getAssetLoader().getItemImage(purchasable);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        button.getTooltip().setText("Cost $" + purchasable.getCostPrice());
        button.setGraphic(imageView);
        button.setText(purchasable.getName());
    }

    public MapInteractionController getMapInteractionController() {
        return mapInteractionController;
    }
}
