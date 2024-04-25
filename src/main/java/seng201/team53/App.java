package seng201.team53;

import javafx.stage.Stage;
import seng201.team53.game.GameEnvironment;
import seng201.team53.gui.MainWindow;

/**
 * Default entry point class
 * @author seng201 teaching team
 */
public class App {
    private static Stage primaryStage;
    private static GameEnvironment gameEnvironment;

    public static Stage getPrimaryStage() {
        if (primaryStage == null)
            throw new IllegalStateException("Primary Stage has not yet been set");
        return primaryStage;
    }
    public static void setPrimaryStage(Stage primaryStage) {
        if (App.primaryStage != null)
            throw new IllegalStateException("Primary Stage has already been set");
        App.primaryStage = primaryStage;
    }

    public static GameEnvironment getGameEnvironment() {
        return gameEnvironment;
    }
    public static void setGameEnvironment(GameEnvironment gameEnvironment) {
        App.gameEnvironment = gameEnvironment;
    }

    /**
     * Entry point which runs the javaFX application
     * Due to how JavaFX works we must call MainWindow.launchWrapper() from here,
     * trying to run MainWindow itself will cause an error
     * @param args program arguments from command line
     */
    public static void main(String[] args) {
        MainWindow.launchWrapper(args);
    }
}
