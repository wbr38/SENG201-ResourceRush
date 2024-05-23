package seng201.team53.gui.controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.state.GameState;
import seng201.team53.game.state.GameStateHandler;
import seng201.team53.gui.wrapper.FXWrappers;
import seng201.team53.game.items.Item;
import seng201.team53.game.items.Purchasable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

/**
 * This class is responsible for controlling and managing sub-controllers for the graphical game interface. It consists
 * of all the required JavaFX elements and events when interacting with the game interface
 */
public class GameController {
    @FXML private Pane overlay;
    @FXML private GridPane gridPane;

    @FXML private Pane mapBackgroundPane;

    // Round complete popup
    @FXML private ChoiceBox<GameDifficulty> difficultyChoiceBox;
    @FXML private Pane roundCompletePane;
    @FXML private Text roundCompleteInfoLabel;
    private int previousPoints = 0;

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

    /**
     * Initialises the game controller.
     * This method hides the inventory and sell item popup, adds values to the difficulty drop town box and creates
     * change listeners for handling round number, point number and state property changes.
     */
    public void init() {
        toggleInventoryVisible(false);
        this.showSellItemPopup(null);

        ObservableList<GameDifficulty> difficulties = FXCollections.observableArrayList(GameDifficulty.values());
        difficultyChoiceBox.setItems(difficulties);
        difficultyChoiceBox.setValue(getGameEnvironment().getDifficulty());
        difficultyChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue,
                                                                                    newValue) -> getGameEnvironment().setDifficulty(newValue));

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

        GameStateHandler stateHandler = getGameEnvironment().getStateHandler();
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

                    showRoundCompletePopup();
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

    /**
     * @return The pane responsible for holding the map image
     */
    public Pane getMapBackgroundPane() {
        return mapBackgroundPane;
    }

    /**
     * @return The pane responsible for displaying carts and watching for mouse clicks
     */
    public Pane getOverlay() {
        return overlay;
    }

    /**
     * @return The grid pane responsible for displaying tower images
     */
    public GridPane getGridPane() {
        return gridPane;
    }

    /**
     * @return The FX wrappers instance
     */
    public FXWrappers getFXWrappers() {
        return fxWrappers;
    }

    /**
     * Handles when the start button is clicked.
     * If the round has not yet started, change the game state to active
     * @param event The mouse event
     */
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

    /**
     * Handles when the pause button is clicked.
     * If the round is active, change the game state to pause
     * @param event The mouse event
     */
    @FXML
    private void onPauseButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.ROUND_ACTIVE)
            return;

        stateHandler.setState(GameState.ROUND_PAUSE);
    }

    /**
     * Handles when the resume button is clicked.
     * If the round is paused, change the game state to active
     * @param event The mouse event
     */
    @FXML
    private void onResumeButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.ROUND_PAUSE)
            return;

        stateHandler.setState(GameState.ROUND_ACTIVE);
    }

    /**
     * Handles when the inventory button is clicked.
     * If the inventory is currently visible, it will be hidden. If it is not visible, it will be shown
     * @param event The mouse event
     */
    @FXML
    private void onInventoryButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        toggleInventoryVisible();
    }

    /**
     * Handles when the exit random event dialog button is clicked
     * If the random event dialog is open, it will close it and set the round to active
     * @param event The mouse event
     */
    @FXML
    private void onRandomEventDialogExistClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.RANDOM_EVENT_DIALOG_OPEN)
            return;

        stateHandler.setState(GameState.ROUND_ACTIVE);
    }

    /**
     * Handles when the exit round complete button is clicked
     * If the round is complete, it will set the state to ROUND_NOT_STARTED
     * @param event The mouse event
     */
    @FXML
    private void onRoundCompleteDialogExistClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.ROUND_COMPLETE)
            return;

        stateHandler.setState(GameState.ROUND_NOT_STARTED);
    }

    /**
     * Shows a notification for a given duration and with the given text
     * @param text The text to be displayed in the notification
     * @param duration The duration the notification should shown
     */
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

    /**
     * Updates the round label to the given current round number
     * @param currentRound The current round number
     */
    private void updateRoundCounter(int currentRound) {
        int totalRounds = getGameEnvironment().getRounds();
        roundCounterLabel.setText(currentRound + "/" + totalRounds);
    }

    /**
     * Updates the point label to the given points number
     * @param points The current points
     */
    private void updatePointsLabel(int points) {
        pointsLabel.setText("" + points);
    }

    /**
     * Show the start button and hide the pause and resume button
     */
    private void showStartButton() {
        show(startButton);
        hide(pauseButton);
        hide(resumeButton);
    }

    /**
     * Show the pause button and hide the start and resume button
     */
    private void showPauseButton() {
        hide(startButton);
        show(pauseButton);
        hide(resumeButton);
    }

    /**
     * Show the resume button and hide the pause and start button
     */
    private void showResumeButton() {
        hide(startButton);
        hide(pauseButton);
        show(resumeButton);
    }

    /**
     * Show the random event dialog with a given text message
     * @param text The text to be displayed in the dialog
     */
    public void showRandomEventDialog(String text) {
        randomEventTest.setText(text);
        show(randomEventPane);
    }

    /**
     * Show the round complete dialog.
     * The dialog contains information about how many points were earned throughout the round
     */
    private void showRoundCompletePopup() {
        showStartButton();
        show(roundCompletePane);

        int pointsEarned = getGameEnvironment().getPoints() - previousPoints;
        roundCompleteInfoLabel.setText("Points Earned: " + pointsEarned);
        previousPoints = getGameEnvironment().getPoints();
    }

    /**
     * Show the game end dialog.
     * This dialog contains information about the game such as if the game was won, player name, current round,
     * max rounds, money and points earned throughout the game
     */
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

    /**
     * Handles when the quit button is clicked.
     * This will exit the game
     * @param event The mouse event
     */
    @FXML
    private void onGameEnd_QuitButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        Platform.exit();
        System.exit(0);
    }

    /**
     * This method will show a given javafx node
     * @param node The node to be shown
     */
    private void show(Node node) {
        node.setVisible(true);
        node.setDisable(false);
    }

    /**
     * This method will hide a given javafx node
     * @param node The node to be hidden
     */
    private void hide(Node node) {
        node.setVisible(false);
        node.setDisable(true);
    }

    /**
     * Updates a shop or inventory button, given the button and purchasable object.
     * If purchasable is null, the buttons graphic and tool tip will be removed.
     * Otherwise, the graphic is added to the button as well as a tool tip which displays the cost price
     * @param button
     * @param purchasable
     */
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

    /**
     * Retrieves the instance of the map interaction controller class
     * @return The map interaction controller
     */
    public MapInteractionController getMapInteractionController() {
        return mapInteractionController;
    }
}
