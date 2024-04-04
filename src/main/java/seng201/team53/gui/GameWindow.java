package seng201.team53.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng201.team53.App;

public class GameWindow {
    private GameController gameController;

    public void start() throws Exception {
        Stage primaryStage = App.getApp().getPrimaryStage();
        FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
        Parent root = baseLoader.load();
        gameController = baseLoader.getController();

        Scene scene = new Scene(root, 800, 640);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public GameController getGameController() {
        return gameController;
    }
}
