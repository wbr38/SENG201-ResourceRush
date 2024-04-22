package seng201.team53;

import javafx.stage.Stage;
import seng201.team53.game.GameEnvironment;
import seng201.team53.gui.MainWindow;

/**
 * Default entry point class
 * 
 * @author seng201 teaching team
 */
public class App {
    private static App app;
    private GameEnvironment gameEnvironment;
    private Stage primaryStage; // lazy way to start testing - TODO remove

    public App() {
        if (app != null)
            throw new IllegalStateException("Instance of app already exists");
        App.app = this;
    }

    public static App getApp() {
        return app;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public GameEnvironment getGameEnvironment() {
        if (gameEnvironment == null)
            throw new IllegalStateException("Game environment has not yet been created");
        return gameEnvironment;
    }

    public void setGameEnvironment(GameEnvironment gameEnvironment) {
        if (this.gameEnvironment != null)
            throw new IllegalStateException("Game environment has already been created");
        this.gameEnvironment = gameEnvironment;
    }

    /**
     * Entry point which runs the javaFX application
     * Due to how JavaFX works we must call MainWindow.launchWrapper() from here,
     * trying to run MainWindow itself will cause an error
     * 
     * @param args program arguments from command line
     */
    public static void main(String[] args) {
        new App();
        MainWindow.launchWrapper(args);
    }
}
