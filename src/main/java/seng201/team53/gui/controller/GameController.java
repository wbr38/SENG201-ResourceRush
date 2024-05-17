package seng201.team53.gui.controller;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import seng201.team53.game.state.GameState;
import seng201.team53.game.state.GameStateHandler;
import seng201.team53.items.Item;
import seng201.team53.items.Purchasable;

public class GameController {
    @FXML private Pane overlay;
    @FXML private GridPane gridPane;

    @FXML private Pane mapBackgroundPane;
    @FXML private Pane roundCompletePane;
    @FXML private Pane gameCompletePane;

    @FXML private Pane randomEventPane;
    @FXML private Text randomEventTest;

    // Info section (top-middle of screen)
    @FXML protected Text moneyLabel;
    @FXML private Text roundCounterLabel;
    @FXML private Text notificationLabel;
    private PauseTransition notificationPause;

    @FXML private Button pauseButton;
    @FXML private Button startButton;
    @FXML private Button resumeButton;

    // Shop
    //protected final Map<Button, TowerType> shopButtons = new HashMap<>();
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
    private Boolean inventoryVisible = false;
    @FXML private AnchorPane inventoryPane;
    @FXML protected Button inventoryButton1;
    @FXML protected Button inventoryButton2;
    @FXML protected Button inventoryButton3;
    @FXML protected Button inventoryButton4;

    // sub-controllers
    private final MapInteractionController mapInteractionController = new MapInteractionController(this);
    private final InventoryController inventoryController = new InventoryController(this, mapInteractionController);
    private final ShopController shopController = new ShopController(this);

    public void init() {
        // Set inventory buttons
        this.setInventoryVisible(this.inventoryVisible);
        this.showSellItemPopup(null);

        var rounds = getGameEnvironment().getRounds();
        getGameEnvironment().getRoundProperty().addListener(($, oldRound, newRound) ->
                roundCounterLabel.setText(newRound.getRoundNumber() + "/" + rounds));

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
                    showStartButton();
                    show(roundCompletePane);
                }
                case GAME_COMPLETE -> {
                    showStartButton();
                    show(gameCompletePane);
                }
            }
            if (oldState == GameState.RANDOM_EVENT_DIALOG_OPEN)
                hide(randomEventPane);
        });

        shopController.init();
        mapInteractionController.init();
        inventoryController.init();
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

    @FXML
    private void onStartButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.ROUND_NOT_STARTED)
            return;

        stateHandler.setState(GameState.ROUND_ACTIVE);
        this.setInventoryVisible(false);
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

        getGameEnvironment().resumeRound();
        stateHandler.setState(GameState.ROUND_ACTIVE);
    }

    @FXML
    private void onInventoryButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        this.inventoryVisible = !this.inventoryVisible;
        this.setInventoryVisible(inventoryVisible);
    }

    public void setInventoryVisible(Boolean enabled) {
        this.inventoryVisible = enabled;
        inventoryPane.setVisible(enabled);
        inventoryPane.setDisable(!enabled);
    }

    /**
     * Update the appropriate elements to allow the user to sell an item.
     * @param item The item to sell. Set to `null` to hide this popup
     */
    public void showSellItemPopup(Item<?> item) {
        if (item == null) {
            hide(sellItemPane);
            return;
        }

        sellItemText.setText("Sell ($" + item.getPurchasableType().getSellPrice() + ")");
        sellItemPane.toFront();
        show(sellItemPane);
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

    private void show(Node node) {
        node.setVisible(true);
        node.setDisable(false);
    }

    private void hide(Node node) {
        node.setVisible(false);
        node.setDisable(true);
    }

    public void updateButton(Button button, Purchasable<?> purchasable) {
        if (purchasable == null) {
            button.setGraphic(null);
            button.getTooltip().setText("");
            button.setText("Add Tower");
            return;
        }
        ImageView imageView = new ImageView(purchasable.getImage());
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
