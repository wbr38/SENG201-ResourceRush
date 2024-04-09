package seng201.team53.game;

import javafx.animation.AnimationTimer;
import seng201.team53.App;

public class GameLoop extends AnimationTimer implements Tickable {
    public static final int TICKS_PER_SECOND = 20;
    public static final long MS_BETWEEN_TICKS = 1000 / TICKS_PER_SECOND;
    private long lastTickTime = -1;

    @Override
    public void handle(long now) {
        if (lastTickTime != -1) {
            if (System.currentTimeMillis() - lastTickTime < MS_BETWEEN_TICKS)
                return;
        }
        tick();
        lastTickTime = System.currentTimeMillis();
    }

    public void tick() {
        App.getApp().getGameEnvironment().getRound().tick();
    }
}
