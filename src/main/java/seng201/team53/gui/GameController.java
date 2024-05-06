package seng201.team53.gui;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.map.Map;
import seng201.team53.game.state.GameState;
import seng201.team53.game.state.GameStateHandler;
import seng201.team53.items.towers.Tower;
import seng201.team53.items.towers.TowerType;

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
    @FXML private Button lumberTowerButton;
    @FXML private Button mineTowerButton;
    @FXML private Button quarryTowerButton;
    @FXML private Button windTowerButton;
    @FXML private Tooltip lumberTowerTooltip;
    @FXML private Tooltip mineTowerTooltip;
    @FXML private Tooltip quarryTowerTooltip;
    @FXML private Tooltip windTowerTooltip;

    // Sell Tower popup
    @FXML private AnchorPane sellTowerPane;
    @FXML private Text sellTowerText;

    // Inventory
    @FXML private Pane inventoryPane;
    private Boolean inventoryVisible = false;

    public GameController() {
    }

    public void init() {
        lumberTowerButton.setOnMouseClicked(e -> this.onShopTowerClick(e, TowerType.LUMBER_MILL));
        mineTowerButton.setOnMouseClicked(e -> this.onShopTowerClick(e, TowerType.MINE));
        quarryTowerButton.setOnMouseClicked(e -> this.onShopTowerClick(e, TowerType.QUARRY));
        windTowerButton.setOnMouseClicked(e -> this.onShopTowerClick(e, TowerType.WIND_MILL));

        lumberTowerTooltip.setText("Cost $" + TowerType.LUMBER_MILL.getCostPrice());
        mineTowerTooltip.setText("Cost $" + TowerType.MINE.getCostPrice());
        quarryTowerTooltip.setText("Cost $" + TowerType.QUARRY.getCostPrice());
        windTowerTooltip.setText("Cost $" + TowerType.WIND_MILL.getCostPrice());

        this.setInventoryVisible(this.inventoryVisible);
        this.showSellTowerPopup(null);
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

        GameStateHandler stateHandler = GameEnvironment.getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.ROUND_NOT_STARTED)
            return;

        stateHandler.setState(GameState.ROUND_ACTIVE);
    }

    @FXML
    private void onPauseButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = GameEnvironment.getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.ROUND_ACTIVE)
            return;

        stateHandler.setState(GameState.ROUND_PAUSE);
    }

    @FXML
    private void onResumeButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = GameEnvironment.getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.ROUND_PAUSE)
            return;

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
     * @param tower The tower to sell. Set to `null` to hide the sell tower popup
     */
    public void showSellTowerPopup(Tower tower) {
        if (tower == null) {
            this.sellTowerPane.setVisible(false);
            return;
        }

        this.sellTowerPane.setVisible(true);
        this.sellTowerText.setText("Sell ($" + tower.getType().getSellPrice() + ")");
    }

    @FXML
    private void onSellTowerButtonClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        Map map = GameEnvironment.getGameEnvironment().getMap();
        map.sellSelectedTower();
    }

    @FXML
    private void onRandomEventDialogExistClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = GameEnvironment.getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.RANDOM_EVENT_DIALOG_OPEN)
            return;

        stateHandler.setState(GameState.ROUND_ACTIVE);
    }

    @FXML
    private void onRoundCompleteDialogExistClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = GameEnvironment.getGameEnvironment().getStateHandler();
        if (stateHandler.getState() != GameState.ROUND_COMPLETE)
            return;

        stateHandler.setState(GameState.ROUND_NOT_STARTED);
    }

    private void onShopTowerClick(MouseEvent event, TowerType towerType) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameStateHandler stateHandler = GameEnvironment.getGameEnvironment().getStateHandler();
        stateHandler.tryStartingPlacingTower(towerType, event.getX(), event.getY());
    }

    /**
     * Update the opacity of the shop buttons/items if the user has enough money
     * to purchase each one
     *
     * @param money
     */
    public void updateShopButtons(int money) {
        lumberTowerButton.setOpacity(TowerType.LUMBER_MILL.getCostPrice() > money ? 0.5 : 1.0);
        mineTowerButton.setOpacity(TowerType.MINE.getCostPrice() > money ? 0.5 : 1.0);
        quarryTowerButton.setOpacity(TowerType.QUARRY.getCostPrice() > money ? 0.5 : 1.0);
        windTowerButton.setOpacity(TowerType.WIND_MILL.getCostPrice() > money ? 0.5 : 1.0);
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
}
