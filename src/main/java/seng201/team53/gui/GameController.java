package seng201.team53.gui;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import seng201.team53.game.state.GameState;
import seng201.team53.game.state.GameStateHandler;
import seng201.team53.items.Purchasable;
import seng201.team53.items.towers.TowerType;
import seng201.team53.items.upgrade.UpgradeItem;

import java.util.HashMap;
import java.util.Map;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class GameController {
    @FXML private Pane overlay;
    @FXML private GridPane gridPane;

    @FXML private Pane mapBackgroundPane;
    @FXML private Pane roundCompletePane;
    @FXML private Pane gameCompletePane;

    @FXML private Pane randomEventPane;
    @FXML private Text randomEventTest;

    // Info section (top-middle of screen)
    @FXML private Text moneyLabel;
    @FXML private Text roundCounterLabel;
    @FXML private Text notificationLabel;
    private PauseTransition notificationPause;

    @FXML private Button pauseButton;
    @FXML private Button startButton;
    @FXML private Button resumeButton;

    // Shop
    private final Map<Button, Purchasable> shopButtons = new HashMap<>();
    
    // Shop tower buttons
    @FXML private Button shopTowerButton1;
    @FXML private Button shopTowerButton2;
    @FXML private Button shopTowerButton3;
    @FXML private Button shopTowerButton4;

    // Shop upgrade item buttons
    @FXML private Button shopItemButton1;
    @FXML private Button shopItemButton2;
    @FXML private Button shopItemButton3;
    @FXML private Button shopItemButton4;

    // Sell Tower popup
    @FXML private AnchorPane sellTowerPane;
    @FXML private Text sellTowerText;

    // Inventory
    private Boolean inventoryVisible = false;
    @FXML private AnchorPane inventoryPane;
    private final Map<Button, TowerType> inventoryButtons = new HashMap<>();
    @FXML private Button inventoryButton1;
    @FXML private Button inventoryButton2;
    @FXML private Button inventoryButton3;
    @FXML private Button inventoryButton4;

    // sub-controllers
    private final MapInteractionController mapInteractionController = new MapInteractionController(this);
    private final InventoryController inventoryController = new InventoryController(this, mapInteractionController);

    public void init() {

        // Set shop tower buttons
        shopButtons.put(shopTowerButton1, TowerType.LUMBER_MILL);
        shopButtons.put(shopTowerButton2, TowerType.MINE);
        shopButtons.put(shopTowerButton3, TowerType.QUARRY);
        shopButtons.put(shopTowerButton4, TowerType.WIND_MILL);

        // Set shop upgrade item buttons
        shopButtons.put(shopItemButton1, UpgradeItem.Type.REPAIR_TOWER);
        shopButtons.put(shopItemButton2, UpgradeItem.Type.TEMP_FASTER_TOWER_RELOAD);
        shopButtons.put(shopItemButton3, UpgradeItem.Type.TEMP_SLOWER_CART);
        shopButtons.put(shopItemButton4, UpgradeItem.Type.FILL_CART);

        shopButtons.forEach((button, purchaseable) -> {
            ShopButton.changeItem(button, purchaseable);
            button.setOnMouseClicked(e -> this.onShopButtonClick(e, purchaseable));
        });

        // Set inventory buttons
        inventoryButtons.put(inventoryButton1, null);
        inventoryButtons.put(inventoryButton2, null);
        inventoryButtons.put(inventoryButton3, null);
        inventoryButtons.put(inventoryButton4, null);
        inventoryButtons.forEach((button, towerType) -> {
            ShopButton.changeItem(button, towerType);
            button.setOnMouseClicked(e -> this.onInventoryButtonClick(e, button));
        });

        this.setInventoryVisible(this.inventoryVisible);
        this.showSellTowerPopup(null);

        // this.mapInteractionController.init();
        this.mapInteractionController.init();
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
        this.showSellTowerPopup(null);
    }

    @FXML
    private void onPauseButtonMouseClick(MouseEvent event) {
        System.out.println("called");
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
     * Update the appropriate elements to allow the user to sell a tower.
     * @param towerType The tower type to sell. Set to `null` to hide the sell tower popup
     */
    public void showSellTowerPopup(TowerType towerType) {
        if (towerType == null) {
            this.sellTowerPane.setVisible(false);
            return;
        }

        this.sellTowerPane.setVisible(true);
        this.sellTowerText.setText("Sell ($" + towerType.getSellPrice() + ")");
        this.sellTowerPane.toFront();
    }

    @FXML
    private void onSellTowerButtonClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        // this.mapInteractionController.sellSelectedTower();
        this.showSellTowerPopup(null);
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

    private void onInventoryButtonClick(MouseEvent event, Button towerButton) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        this.inventoryController.handleInventoryTowerClick(towerButton);
    }

    private void onShopButtonClick(MouseEvent event, Purchasable purchasable) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        mapInteractionController.tryPurchase(purchasable);
    }

    /**
     * Update the opacity of the shop buttons/items if the user has enough money
     * to purchase each one
     * @param money
     */
    public void updateShopButtons(int money) {
        shopButtons.forEach((button, towerType) -> {
            int cost = towerType.getCostPrice();
            button.setOpacity(cost > money ? 0.5 : 1.0);
        });
    }

    public void updateRoundCounter(int currentRound, int numberOfRounds) {
        roundCounterLabel.setText(currentRound + "/" + numberOfRounds);
    }

    public void updateMoneyLabel(int money) {
        moneyLabel.setText("$" + money);
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

    public void showStartButton() {
        show(startButton);
        hide(pauseButton);
        hide(resumeButton);
    }

    public void showPauseButton() {
        hide(startButton);
        show(pauseButton);
        hide(resumeButton);
    }

    public void showResumeButton() {
        hide(startButton);
        hide(pauseButton);
        show(resumeButton);
    }

    public void showRandomEventDialog(String text) {
        randomEventTest.setText(text);
        show(randomEventPane);
    }

    public void hideRandomEventDialog() {
        hide(randomEventPane);
    }

    public void showRoundCompleteDialog() {
        show(roundCompletePane);
    }

    public void hideRoundCompleteDialog() {
        hide(roundCompletePane);
    }

    public void showGameCompleteDialog() {
        show(gameCompletePane);
    }

    private void show(Node node) {
        node.setVisible(true);
        node.setDisable(false);
    }

    private void hide(Node node) {
        node.setVisible(false);
        node.setDisable(true);
    }

    public MapInteractionController getMapInteractionController() {
        return mapInteractionController;
    }
}
