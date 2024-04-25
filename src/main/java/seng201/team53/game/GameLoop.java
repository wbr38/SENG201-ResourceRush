package seng201.team53.game;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
    public static final int TICKS_PER_SECOND = 20;
    public static final long MS_BETWEEN_TICKS = 1000 / TICKS_PER_SECOND;
    private final Tickable function;
    private long lastTickTime = -1;

    public GameLoop(Tickable function) {
        this.function = function;
    }

    @Override
    public void handle(long now) {
        if (lastTickTime != -1) {
            if (System.currentTimeMillis() - lastTickTime < MS_BETWEEN_TICKS)
                return;
        }
        function.tick();
        lastTickTime = System.currentTimeMillis();
    }
}
