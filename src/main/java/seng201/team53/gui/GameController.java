package seng201.team53.gui;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import seng201.team53.App;
import seng201.team53.game.GameState;
import seng201.team53.game.map.Map;
import seng201.team53.game.map.MapInteraction;
import seng201.team53.game.map.Tile;
import seng201.team53.items.towers.LumberMillTower;
import seng201.team53.items.towers.MineTower;
import seng201.team53.items.towers.QuarryTower;
import seng201.team53.items.towers.Tower;
import seng201.team53.items.towers.TowerType;
import seng201.team53.items.towers.WindMillTower;

public class GameController {
    @FXML public AnchorPane test;
    @FXML private GridPane gridPane;
    @FXML private AnchorPane inventoryPane;
    @FXML private Button pauseButton;
    @FXML private Button startButton;
    @FXML private Button resumeButton;
    @FXML private Text roundCounterLabel;
    @FXML private Text moneyLabel;
    @FXML private Text notificationLabel;

    // Shop
    @FXML private Button lumberTowerButton;
    @FXML private Button mineTowerButton;
    @FXML private Button quarryTowerButton;
    @FXML private Button windTowerButton;

    @FXML private Tooltip lumberTowerTooltip;
    @FXML private Tooltip mineTowerTooltip;
    @FXML private Tooltip quarryTowerTooltip;
    @FXML private Tooltip windTowerTooltip;

    private PauseTransition notificationPause;

    public void init() {
        var scene = App.getApp().getPrimaryStage().getScene();
        scene.setOnMousePressed(this::onMousePressed);

        notificationLabel.setVisible(false);

        lumberTowerButton.setOnMouseClicked(e -> this.onShopTowerClick(e, TowerType.LUMBER_MILL));
        mineTowerButton.setOnMouseClicked(e -> this.onShopTowerClick(e, TowerType.MINE));
        quarryTowerButton.setOnMouseClicked(e -> this.onShopTowerClick(e, TowerType.QUARRY));
        windTowerButton.setOnMouseClicked(e -> this.onShopTowerClick(e, TowerType.WIND_MILL));

        lumberTowerTooltip.setText("Cost $" + LumberMillTower.COST);
        mineTowerTooltip.setText("Cost $" + MineTower.COST);
        quarryTowerTooltip.setText("Cost $" + QuarryTower.COST);
        windTowerTooltip.setText("Cost $" + WindMillTower.COST);
    }

    /**
     * Update the opacity of the shop buttons/items if the user has enough money
     * to purchase each one
     * 
     * @param money
     */
    public void updateShopButtons(int money) {
        lumberTowerButton.setOpacity(LumberMillTower.COST > money ? 0.5 : 1.0);
        mineTowerButton.setOpacity(MineTower.COST > money ? 0.5 : 1.0);
        quarryTowerButton.setOpacity(QuarryTower.COST > money ? 0.5 : 1.0);
        windTowerButton.setOpacity(WindMillTower.COST > money ? 0.5 : 1.0);
    }

    @FXML
    private void onStartButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        App.getApp().getGameEnvironment().setState(GameState.ROUND_ACTIVE);
    }

    @FXML
    private void onPauseButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        App.getApp().getGameEnvironment().setState(GameState.ROUND_PAUSE);
    }

    @FXML
    private void onResumeButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        App.getApp().getGameEnvironment().setState(GameState.ROUND_ACTIVE);
    }

    @FXML
    private void onInventoryButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        inventoryPane.setVisible(!inventoryPane.isVisible());
        inventoryPane.setDisable(!inventoryPane.isDisable());
    }

    private void onShopTowerClick(MouseEvent event, TowerType towerType) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        Map map = App.getApp().getGameEnvironment().getRound().getMap();
        if (map.getCurrentInteraction() != MapInteraction.NONE) // prevents starting the "placing" methods running again
            return;

        Tower tower = towerType.create();
        App.getApp().getGameEnvironment().tryPurchaseTower(tower);
    }

    /*
     * Handle when the user clicks their mouse. Used for when interacting with the
     * map (placing towers, etc)
     */
    private void onMousePressed(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        var map = App.getApp().getGameEnvironment().getRound().getMap();
        if (map.getCurrentInteraction() == MapInteraction.NONE)
            return;

        int mouseX = (int)Math.round(event.getSceneX());
        int mouseY = (int)Math.round(event.getSceneY());
        Tile tile = map.getTileFromScreenPosition(mouseX, mouseY);
        switch (map.getCurrentInteraction()) {
            case PLACE_TOWER:
                if (!tile.isBuildable() || tile.getTower() != null)
                    return;
                var selectedTower = map.getSelectedTower();
                map.placeTower(selectedTower, tile);
                map.setInteraction(MapInteraction.NONE);
                map.stopPlacingTower();
                break;
            default:
                break;
        }
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public void updateRoundCounter(int currentRound) {
        int rounds = App.getApp().getGameEnvironment().getRounds();
        roundCounterLabel.setText(currentRound + "/" + rounds);
    }

    public void updateMoneyLabel(int money) {
        moneyLabel.setText("$" + money);
    }

    public void showNotification(String text, double duration) {
        notificationLabel.setText(text);
        notificationLabel.setVisible(true);

        // Pause previous delay (otherwise the previous will hide the label prematurely)
        if (notificationPause != null)
            notificationPause.stop();

        // Hide the label after x seconds
        notificationPause = new PauseTransition(Duration.seconds(duration));
        notificationPause.setOnFinished(event -> notificationLabel.setVisible(false));
        notificationPause.play();
    }

    public void showStartButton() {
        showButton(startButton, true);
        showButton(pauseButton, false);
        showButton(resumeButton, false);
    }

    public void showPauseButton() {
        showButton(startButton, false);
        showButton(pauseButton, true);
        showButton(resumeButton, false);
    }

    public void showResumeButton() {
        showButton(startButton, false);
        showButton(pauseButton, false);
        showButton(resumeButton, true);
    }

    private void showButton(Button button, boolean show) {
        button.setVisible(show);
        button.setDisable(!show);
    }
}
