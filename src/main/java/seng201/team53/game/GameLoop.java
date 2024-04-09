package seng201.team53.game;

import javafx.animation.AnimationTimer;
import seng201.team53.App;

public class GameLoop extends AnimationTimer {
    public static final int TICKS_PER_SECOND = 10;
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
        App.getApp().getGameEnvironment().getRound().render();
    }
}
