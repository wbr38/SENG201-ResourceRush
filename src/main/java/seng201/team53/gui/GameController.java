package seng201.team53.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import seng201.team53.App;
import seng201.team53.game.GameState;
import seng201.team53.game.map.Map;
import seng201.team53.game.map.MapInteraction;
import seng201.team53.game.map.Tile;
import seng201.team53.items.towers.Tower;
import seng201.team53.items.towers.TowerType;

public class GameController {
    @FXML public AnchorPane test;
    @FXML private GridPane gridPane;
    @FXML private AnchorPane inventoryPane;
    @FXML private Button pauseButton;
    @FXML private Button startButton;
    @FXML private Button resumeButton;
    @FXML private Text roundCounterLabel;
    @FXML private Text moneyLabel;

    public void init() {
        var scene = App.getApp().getPrimaryStage().getScene();
        scene.setOnMousePressed(this::onMousePressed);
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

    @FXML
    private void onShopLumberMillTowerClick(MouseEvent event) {
        onShopTowerClick(event, TowerType.LUMBER_MILL);
    }

    @FXML
    private void onShopMineTowerClick(MouseEvent event) {
        onShopTowerClick(event, TowerType.MINE);
    }

    @FXML
    private void onShopQuarryTowerClick(MouseEvent event) {
        onShopTowerClick(event, TowerType.QUARRY);
    }

    @FXML
    private void onShopWindMillTowerClick(MouseEvent event) {
        onShopTowerClick(event, TowerType.WIND_MILL);
    }

    private void onShopTowerClick(MouseEvent event, TowerType towerType) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        Map map = App.getApp().getGameEnvironment().getRound().getMap();
        if (map.getCurrentInteraction() != MapInteraction.NONE) // prevents starting the "placing" methods running again
            return;
        Tower tower = towerType.create();
        map.startPlacingTower(tower);
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

        int mouseX = (int) Math.round(event.getSceneX());
        int mouseY = (int) Math.round(event.getSceneY());
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
