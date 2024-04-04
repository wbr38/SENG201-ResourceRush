package seng201.team53.game;

import seng201.team53.game.map.MapLoader;
import seng201.team53.gui.GameController;
import seng201.team53.gui.GameWindow;

public class GameEnvironment {
    private final GameWindow gameWindow = new GameWindow();
    private final MapLoader mapLoader = new MapLoader();
    private final String name;
    private final int rounds;
    private boolean paused = true;

    public GameEnvironment(String name, int rounds)  {
        this.name = name;
        this.rounds = rounds;
    }

    public void init() throws Exception {
        gameWindow.start();

        GameController gameController = gameWindow.getGameController();
        gameController.init();
        mapLoader.init();

        // for testing purposes
        // todo - load all maps in init then draw them in GameRound
        var map = mapLoader.getMap("default");
        var gridPane = gameController.getGridPane();
        // todo - add startX, startY, endX, endY into map txt file or auto detect for start/end
        map.findPath(5, 0, 15, 14);
        map.draw(gridPane);
        map.drawPath(gameController.getOverlayCanvas());
    }

    public boolean isPaused() {
        return paused;
    }
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public int getRounds() {
        return rounds;
    }
}
