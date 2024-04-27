package seng201.team53.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.GameStateHandler;

public class MainController {
    public static final int MIN_NAME_LENGTH = 3;
    public static final int MAX_NAME_LENGTH = 15;
    @FXML private ChoiceBox<GameDifficulty> difficultyChoiceBox;
    @FXML private TextField nameTextField;
    @FXML private ImageView nameGreenCheckmark;
    @FXML private ImageView nameRedCross;
    @FXML private Text nameNotValidLabel;
    @FXML private Slider numberOfRoundsSlider;
    @FXML private Text numberOfRoundsLabel;
    private final WindowManager windowManager;
    private boolean currentNameChoiceValid = false;

    public MainController(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    @FXML
    void onNameFieldKeyPress(KeyEvent event) {
        var text = nameTextField.getText() + event.getText();
        
        boolean validName = (
            text.length() >= MIN_NAME_LENGTH
            && text.length() <= MAX_NAME_LENGTH
            && text.matches("^[A-Za-z0-9]*$"));
        if (currentNameChoiceValid != validName) {
            currentNameChoiceValid = validName;
            nameRedCross.setVisible(!currentNameChoiceValid);
            nameNotValidLabel.setVisible(!currentNameChoiceValid);
            nameGreenCheckmark.setVisible(currentNameChoiceValid);
        }
    }

    @FXML
    void onStartButtonMouseClick(MouseEvent event) throws Exception {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        if (!currentNameChoiceValid) {
            var content = "Your name must be of length 3-15 and not include special characters.";
            Alert alert = new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
            alert.showAndWait();
            return;
        }
        var name = nameTextField.getText();
        var rounds = (int) numberOfRoundsSlider.getValue();
        var gameDifficulty = difficultyChoiceBox.getSelectionModel().getSelectedItem();
        var gameStateHandler = new GameStateHandler();
        var gameController = windowManager.loadGameScreen(gameStateHandler);
        var gameEnvironment = new GameEnvironment(gameStateHandler, gameController, name, rounds, gameDifficulty);
        gameEnvironment.init();
    }

    public void init() {
        var difficulties = FXCollections.observableArrayList(GameDifficulty.values());
        difficultyChoiceBox.setItems(difficulties);
        difficultyChoiceBox.setValue(GameDifficulty.NORMAL);

        numberOfRoundsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            numberOfRoundsLabel.setText(String.valueOf(newValue.intValue()));
        });
    }
}
