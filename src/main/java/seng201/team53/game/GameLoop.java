package seng201.team53.game;

import javafx.animation.AnimationTimer;
import seng201.team53.App;

public class GameLoop extends AnimationTimer {
    @Override
    public void handle(long now) {
        if (App.getApp().getGameEnvironment().isPaused())
            return;
        tick();
    }

    public void tick() {

    }
}
