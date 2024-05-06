package seng201.team53.game.event.type;

import seng201.team53.game.map.Map;
import seng201.team53.game.round.GameRound;

public interface RandomEvent {
    boolean isAvailable();
    void apply();
}
