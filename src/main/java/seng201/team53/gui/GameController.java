package seng201.team53.gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import seng201.team53.game.state.GameState;
import seng201.team53.game.state.GameStateHandler;
import seng201.team53.items.towers.TowerType;

public class GameController {
    @FXML private AnchorPane overlay;
    @FXML private GridPane gridPane;
    @FXML private Pane mapBackgroundPane;
    @FXML private Pane randomEventPane;
    @FXML private Pane roundCompletePane;
    @FXML private Pane gameCompletePane;
    @FXML private Text randomEventTest;
    @FXML private AnchorPane inventoryPane;
    @FXML private Button pauseButton;
    @FXML private Button startButton;
    @FXML private Button resumeButton;
    @FXML private Text roundCounterLabel;
    private final GameStateHandler stateHandler;

    public GameController(GameStateHandler stateHandler) {
        this.stateHandler = stateHandler;
    }

    public Pane getMapBackgroundPane() {
        return mapBackgroundPane;
    }

    public AnchorPane getOverlay() {
        return overlay;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    @FXML
    private void onStartButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        if (stateHandler.getState() != GameState.ROUND_NOT_STARTED)
            return;
        stateHandler.setState(GameState.ROUND_ACTIVE);
    }

    @FXML
    private void onPauseButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        if (stateHandler.getState() != GameState.ROUND_ACTIVE)
            return;
        stateHandler.setState(GameState.ROUND_PAUSE);
    }

    @FXML
    private void onResumeButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        if (stateHandler.getState() != GameState.ROUND_PAUSE)
            return;
        stateHandler.setState(GameState.ROUND_ACTIVE);
    }

    @FXML
    private void onInventoryButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        inventoryPane.setVisible(!inventoryPane.isVisible());
        inventoryPane.setDisable(!inventoryPane.isDisable());
    }

    @FXML
    private void onRandomEventDialogExistClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        if (stateHandler.getState() != GameState.RANDOM_EVENT_DIALOG_OPEN)
            return;
        stateHandler.setState(GameState.ROUND_ACTIVE);
    }

    @FXML
    private void onRoundCompleteDialogExistClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        if (stateHandler.getState() != GameState.ROUND_COMPLETE)
            return;
        stateHandler.setState(GameState.ROUND_NOT_STARTED);
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
        stateHandler.allowPlacingTower(towerType);
    }

    public void updateRoundCounter(int currentRound, int numberOfRounds) {
        roundCounterLabel.setText(currentRound + "/" + numberOfRounds);
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
