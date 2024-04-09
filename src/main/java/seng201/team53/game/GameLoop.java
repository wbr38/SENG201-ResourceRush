package seng201.team53.game;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import seng201.team53.App;

public class GameLoop extends AnimationTimer {
    public static final int TICKS_PER_SECOND = 5;
    public static final long MS_BETWEEN_TICKS = 1000 / TICKS_PER_SECOND;
    private long lastTickTime = -1;
    private long lifetimeTicks = 0;

    @Override
    public void handle(long now) {
        if (lastTickTime != -1) {
            if (System.currentTimeMillis() - lastTickTime < MS_BETWEEN_TICKS)
                return;
        }
        tick();
        render();
        System.out.println("tick " + ++lifetimeTicks);
        lastTickTime = System.currentTimeMillis();
    }

    public void tick() {
        App.getApp().getGameEnvironment().getRound().tick();
    }
    public void render() {
        var canvas = App.getApp().getGameEnvironment().getWindow().getController().getOverlayCanvas();
        var graphics = canvas.getGraphicsContext2D();
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        App.getApp().getGameEnvironment().getRound().render(graphics);
    }
}
