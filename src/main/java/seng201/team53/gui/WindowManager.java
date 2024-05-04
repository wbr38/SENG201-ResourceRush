package seng201.team53.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seng201.team53.game.state.GameStateHandler;

import java.io.IOException;

public class WindowManager {
    @FXML private Pane pane;
    private final Stage stage;

    public WindowManager(Stage stage) {
        this.stage = stage;
    }

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
    public GameController loadGameScreen() {
        pane.getChildren().clear();
        try {
            FXMLLoader setupLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
            setupLoader.setControllerFactory(param -> new GameController());

            Parent setupParent = setupLoader.load();
            GameController controller = setupLoader.getController();
            pane.getChildren().add(setupParent);
            controller.init();
            stage.setTitle("ResourceRush Game");
            return controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
