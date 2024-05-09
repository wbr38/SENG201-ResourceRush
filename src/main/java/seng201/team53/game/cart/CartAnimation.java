package seng201.team53.game.cart;

import seng201.team53.game.Tickable;
import seng201.team53.game.round.GameRound;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class CartAnimation implements Tickable {
    @Override
    public void tick() {
        var round = getGameEnvironment().getRound();

    }
}
