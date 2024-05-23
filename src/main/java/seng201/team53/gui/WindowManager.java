package seng201.team53.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seng201.team53.gui.controller.GameController;
import seng201.team53.gui.controller.MainController;

import java.io.IOException;

/**
 * This class is responsible for managing the different screens within the game.
 * It handles the transitions between the main menu and the game screen ensuring that the appropriate
 * controllers and loaded and initialized. Their views are then added to the primary stages pane.
 */
public class WindowManager {
    @FXML private Pane pane;
    private final Stage stage;

    /**
     * Constructs a new WindowManager instance
     * @param stage The primary stage of the application
     */
    public WindowManager(Stage stage) {
        this.stage = stage;
    }

    /**
     * Loads the setup screen by clearing the current pane and loading the main menu FXML file.
     * It initializes the MainController with a reference to this WindowManager so the game window can be
     * launched later.
     */
    public void loadSetupScreen() {
        pane.getChildren().clear();
        try {
            FXMLLoader setupLoader = new FXMLLoader(getClass().getResource("/fxml/main_menu.fxml"));
            setupLoader.setControllerFactory(param -> new MainController(this));

            Parent setupParent = setupLoader.load();
            MainController controller = setupLoader.getController();
            pane.getChildren().add(setupParent);
            stage.setTitle("ResourceRush Setup");
            controller.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the game screen by clearing the current pane and loading the game FXML file.
     * It initializes and returns GameController, so it can be used to interact with the graphical interface
     * launched later.
     * @return The game controller
     */
    public GameController loadGameScreen() {
        pane.getChildren().clear();
        try {
            FXMLLoader setupLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
            setupLoader.setControllerFactory(param -> new GameController());

            Parent setupParent = setupLoader.load();
            GameController controller = setupLoader.getController();
            pane.getChildren().add(setupParent);
            stage.setTitle("ResourceRush Game");
            return controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
