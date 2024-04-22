package seng201.team53.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import seng201.team53.App;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;

public class MainController {
    @FXML private ChoiceBox<GameDifficulty> difficultyChoiceBox;

    @FXML private TextField nameTextField;

    @FXML private ImageView nameGreenCheckmark;

    @FXML private ImageView nameRedCross;

    @FXML private Text nameNotValidLabel;

    @FXML private Slider numberOfRoundsSlider;

    @FXML private Text numberOfRoundsLabel;

    private boolean validNameChoice = false;
    private final int MIN_NAME_LENGTH = 3;
    private final int MAX_NAME_LENGTH = 15;

    @FXML
    void onNameFieldKeyPress(KeyEvent event) {
        var text = nameTextField.getText() + event.getText();

        boolean _validName = (text.length() >= MIN_NAME_LENGTH
            && text.length() <= MAX_NAME_LENGTH
            && text.matches("^[A-Za-z0-9]*$") // only letters or numbers
        );

        // Name became valid or invalid, toggle GUI elements 
        if (validNameChoice != _validName) {
            validNameChoice = _validName;
            nameRedCross.setVisible(!validNameChoice);
            nameNotValidLabel.setVisible(!validNameChoice);
            nameGreenCheckmark.setVisible(validNameChoice);
        }
    }

    @FXML
    void onStartButtonMouseClick(MouseEvent event) throws Exception {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        if (!validNameChoice) {
            var content = "Your name must be of length 3-15 and not include special characters.";
            Alert alert = new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
            alert.showAndWait();
            return;
        }
        var name = nameTextField.getText();
        var rounds = (int)numberOfRoundsSlider.getValue();
        GameDifficulty gameDifficulty = difficultyChoiceBox.getSelectionModel().getSelectedItem();
        var gameEnvironment = new GameEnvironment(name, rounds, gameDifficulty);
        App.getApp().setGameEnvironment(gameEnvironment);
        gameEnvironment.init();
    }

    public void init() {
        // Initialise Game Difficulty selector
        var difficulties = FXCollections.observableArrayList(GameDifficulty.values());
        difficultyChoiceBox.setItems(difficulties);
        difficultyChoiceBox.setValue(GameDifficulty.NORMAL);

        numberOfRoundsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            numberOfRoundsLabel.setText(String.valueOf(newValue.intValue()));
        });
    }
}
