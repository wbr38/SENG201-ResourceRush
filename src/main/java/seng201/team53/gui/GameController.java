package seng201.team53.gui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import seng201.team53.App;
import seng201.team53.game.map.Map;
import seng201.team53.game.map.MapInteraction;
import seng201.team53.game.map.MapLoader;
import seng201.team53.game.map.Tile;
import seng201.team53.items.towers.LumberMillTower;
import seng201.team53.items.towers.Tower;

public class GameController {
    @FXML
    private GridPane gridPane;

    @FXML
    private Canvas overlayCanvas;

    @FXML
    private Button pauseButton;

    @FXML
    private Button startButton;

    @FXML
    private Button placeTowersButton;

    @FXML
    private Text roundCounterLabel;

    // Shop Controller
    @FXML
    private Button createWoodTowerButton;

    @FXML
    void onPauseButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        App.getApp().getGameEnvironment().setPaused(false);
        startButton.setDisable(false);
        startButton.setVisible(true);
        pauseButton.setDisable(true);
        pauseButton.setVisible(false);
    }

    @FXML
    void onCreateWoodTowerBtnClick(MouseEvent event) {
        MapLoader mapLoader = App.getApp().getGameEnvironment().getMapLoader();
        Map map = mapLoader.getMap("default");

        LumberMillTower tower = new LumberMillTower();
        map.startPlacingTower(tower);
    }

    @FXML
    void onStartButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        App.getApp().getGameEnvironment().setPaused(true);
        startButton.setDisable(true);
        startButton.setVisible(false);
        pauseButton.setDisable(false);
        pauseButton.setVisible(true);
    }

    /*
     * Handle when the user clicks their mouse. Used for when interacting with the
     * map (placing towers, etc)
     */
    private void onMousePressed(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        // todo get the map from the current game round
        var map = App.getApp().getGameEnvironment().getMapLoader().getMap("default");
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

    public Canvas getOverlayCanvas() {
        return overlayCanvas;
    }

    public void init() {
        var scene = App.getApp().getPrimaryStage().getScene();
        scene.setOnMousePressed(this::onMousePressed);

        int rounds = App.getApp().getGameEnvironment().getRounds();
        roundCounterLabel.setText(1 + "/" + rounds);
    }
}
