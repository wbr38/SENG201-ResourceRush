package seng201.team53.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.gui.WindowManager;
import seng201.team53.service.NameValidatorService;

/**
 * This class is responsible for controlling the main menu graphical interface. It consists
 * of all the required JavaFX elements and events when interacting with the main menu interface
 */
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

    /**
     * Construct the main controller with an instance of the window manager which is later used to launch the
     * game interface
     * @param windowManager The window manager
     */
    public MainController(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    /**
     * Initialises the main controller.
     * This method will add the difficulty values and add a listener which listens to changes in the value of the
     * number of rounds slider.
     */
    public void init() {
        ObservableList<GameDifficulty> difficulties = FXCollections.observableArrayList(GameDifficulty.values());
        difficultyChoiceBox.setItems(difficulties);
        difficultyChoiceBox.setValue(GameDifficulty.NORMAL);

        numberOfRoundsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            numberOfRoundsLabel.setText(String.valueOf(newValue.intValue()));
        });
    }

    /**
     * Handles when a key is pressed while focused on the name field.
     * Uses the name validator service to check if the text is valid.
     * If it is not valid, a red cross and not valid label will be shown.
     * If it is valid, a green check mark will be shown
     * @param event The key event
     */
    @FXML
    void onNameFieldKeyPress(KeyEvent event) {
        String text = nameTextField.getText() + event.getText();
        boolean validName = nameValidatorService.isValid(text);
        nameRedCross.setVisible(!validName);
        nameNotValidLabel.setVisible(!validName);
        nameGreenCheckmark.setVisible(validName);
    }

    /**
     * Handles when the start button is clicked.
     * This message checks if the name is valid. If it is not valid, an alert will be shown.
     * Otherwise, the number of rounds and selected game difficulty is fetched. The game screen is loaded and the
     * game environment is initialised and loaded.
     * @param event The mouse event
     */
    @FXML
    void onStartButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        String playerName = nameTextField.getText();
        if (!nameValidatorService.isValid(playerName)) {
            String content = "Your name must be of length " + NameValidatorService.MIN_NAME_LENGTH + "-" + NameValidatorService.MAX_NAME_LENGTH + " and not include special characters.";
            Alert alert = new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
            alert.showAndWait();
            return;

        }
        int rounds = (int)numberOfRoundsSlider.getValue();
        GameDifficulty gameDifficulty = difficultyChoiceBox.getSelectionModel().getSelectedItem();

        GameController gameController = windowManager.loadGameScreen();
        GameEnvironment gameEnv = GameEnvironment.init(gameController, playerName, rounds, gameDifficulty);
        gameEnv.load();
        gameController.init();
    }
}
