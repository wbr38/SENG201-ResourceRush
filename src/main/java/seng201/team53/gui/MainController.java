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
import seng201.team53.service.NameValidatorService;

public class MainController {
    @FXML private ChoiceBox<GameDifficulty> difficultyChoiceBox;
    @FXML private TextField nameTextField;
    @FXML private ImageView nameGreenCheckmark;
    @FXML private ImageView nameRedCross;
    @FXML private Text nameNotValidLabel;
    @FXML private Slider numberOfRoundsSlider;
    @FXML private Text numberOfRoundsLabel;

    private final WindowManager windowManager;
    private final NameValidatorService nameValidatorService = new NameValidatorService();

    public MainController(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    @FXML
    void onNameFieldKeyPress(KeyEvent event) {
        var text = nameTextField.getText() + event.getText();
        var validName = nameValidatorService.isValid(text);
        nameRedCross.setVisible(!validName);
        nameNotValidLabel.setVisible(!validName);
        nameGreenCheckmark.setVisible(validName);
    }

    @FXML
    void onStartButtonMouseClick(MouseEvent event) throws Exception {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        var playerName = nameTextField.getText();
        if (!nameValidatorService.isValid(playerName)) {
            var content = "Your name must be of length " + NameValidatorService.MIN_NAME_LENGTH + "-" + NameValidatorService.MAX_NAME_LENGTH + " and not include special characters.";
            Alert alert = new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
            alert.showAndWait();
            return;

        }
        var rounds = (int) numberOfRoundsSlider.getValue();
        var gameDifficulty = difficultyChoiceBox.getSelectionModel().getSelectedItem();

        GameController gameController = windowManager.loadGameScreen();
        GameEnvironment.init(gameController, playerName, rounds, gameDifficulty);
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
