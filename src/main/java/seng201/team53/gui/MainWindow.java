package seng201.team53.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng201.team53.App;

import java.io.IOException;

/**
 * Class starts the javaFX application window
 * @author seng201 teaching team
 */
public class MainWindow extends Application {

    /**
     * Opens the gui with the fxml content specified in resources/fxml/main.fxml
     * @param primaryStage The current fxml stage, handled by javaFX Application class
     * @throws IOException if there is an issue loading fxml file
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        App.getApp().setPrimaryStage(primaryStage);
        FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = baseLoader.load();

        MainController baseController = baseLoader.getController();
        baseController.init();
        primaryStage.setTitle("ResourceRush");
        primaryStage.setResizable(false);

        Scene scene = new Scene(root, 800, 640);
        primaryStage.setScene(scene);
        primaryStage.show();

        // open the game map straight away for quick testing
//        var name = "Testing";
//        var rounds = 3;
//        GameDifficulty gameDifficulty = GameDifficulty.NORMAL;
//        var gameEnvironment = new GameEnvironment(name, rounds, gameDifficulty);
//        App.getApp().setGameEnvironment(gameEnvironment);
//        try {
//            gameEnvironment.init();
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
    }

    /**
     * Launches the FXML application, this must be called from another class (in this cass App.java) otherwise JavaFX
     * errors out and does not run
     * @param args command line arguments
     */
    public static void launchWrapper(String [] args) {
        launch(args);
    }

}
