package seng201.team53.game;

import javafx.animation.AnimationTimer;
import seng201.team53.game.state.GameState;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class GameLoop extends AnimationTimer {
    public static final int TICKS_PER_SECOND = 10;
    public static final long MS_BETWEEN_TICKS = 1000 / TICKS_PER_SECOND;
    private final Tickable function;
    private int roundCompleteTicks;
    private long lastTickTime = -1;
    private int lifetimeTicks = 0;

    public GameLoop(Tickable function) {
        this.function = function;
    }

    @Override
    public void handle(long now) {
        if (lastTickTime != -1) {
            if (System.currentTimeMillis() - lastTickTime < MS_BETWEEN_TICKS)
                return;
        }
        if (roundCompleteTicks == lifetimeTicks) {
            getGameEnvironment().getStateHandler().setState(GameState.ROUND_COMPLETE);
            stop();
            return;
        }
        function.tick(lifetimeTicks);
        lastTickTime = System.currentTimeMillis();
        lifetimeTicks++;
    }

    public void setRoundCompleteTicks(int roundCompleteTicks) {
        this.roundCompleteTicks = roundCompleteTicks;
    }
}
