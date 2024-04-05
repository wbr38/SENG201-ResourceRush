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
import seng201.team53.game.GameEnvironment;

public class MainController {
    @FXML
    private ChoiceBox<String> difficultyChoiceBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private ImageView nameGreenCheckmark;

    @FXML
    private ImageView nameRedCross;

    @FXML
    private Text nameNotValidLabel;

    @FXML
    private Slider numberOfRoundsSlider;

    @FXML
    private Text numberOfRoundsLabel;

    private boolean validNameChoice = false;

    @FXML
    void onNameFieldKeyPress(KeyEvent event) {
        var text = nameTextField.getText() + event.getText();
        if ((text.length() >= 3 && text.length() <= 15 && text.matches("^[A-Za-z0-9]*$")) == !validNameChoice) {
            validNameChoice = !validNameChoice;
            nameRedCross.setVisible(!nameRedCross.isVisible());
            nameGreenCheckmark.setVisible(!nameGreenCheckmark.isVisible());
            nameNotValidLabel.setVisible(!nameNotValidLabel.isVisible());
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
        var rounds = (int) numberOfRoundsSlider.getValue();
        var gameEnvironment = new GameEnvironment(name, rounds);
        App.getApp().setGameEnvironment(gameEnvironment);
        gameEnvironment.init();
    }

    public void init() {
        var difficulties = FXCollections.observableArrayList("Easy", "Normal", "Hard");
        difficultyChoiceBox.setValue("Normal");
        difficultyChoiceBox.setItems(difficulties);

        numberOfRoundsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            numberOfRoundsLabel.setText(String.valueOf(newValue.intValue()));
        });
    }
}
