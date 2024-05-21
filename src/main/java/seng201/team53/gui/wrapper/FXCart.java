package seng201.team53.gui.wrapper;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import seng201.team53.game.state.CartState;
import seng201.team53.game.state.GameState;
import seng201.team53.items.Cart;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class FXCart {
    private final Cart cart;
    private final Pane wrapper;
    private final ImageView imageView;
    private final Label capacityLabel;
    private final PathTransition pathTransition;
    private final ChangeListener<Number> cartCapacityListener;
    private final ChangeListener<CartState> cartStateListener;
    private final ChangeListener<Number> cartVelocityModifierListener;
    private final ChangeListener<GameState> gameStateListener;

    public FXCart(Cart cart, Pane wrapper, ImageView imageView, Label capacityLabel) {
        this.cart = cart;
        this.wrapper = wrapper;
        this.imageView = imageView;
        this.capacityLabel = capacityLabel;

        var map = getGameEnvironment().getMap();
        pathTransition = new PathTransition();
        pathTransition.setNode(wrapper);
        pathTransition.setDuration(map.calculatePathDuration(cart.getVelocity()));
        pathTransition.setPath(map.getPolylinePath());
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setOnFinished(event -> cart.setCartState(CartState.COMPLETE_PATH));

        cartCapacityListener = ($, oldCapacity, newCapacity) ->
                onCapacityUpdate(newCapacity.intValue());
        cartStateListener = ($, oldState, newState) ->
                onCartStateUpdate(newState);
        gameStateListener = ($, oldState, newState) ->
                onGameStateUpdate(newState);
        cartVelocityModifierListener = ($, oldModifier, newModifier) ->
                onCartVelocityModifierChange(newModifier.floatValue());
        cart.getCurrentCapacityProperty().addListener(cartCapacityListener);
        cart.getCartStateProperty().addListener(cartStateListener);
        cart.getVelocityModifierProperty().addListener(cartVelocityModifierListener);
        getGameEnvironment().getStateHandler().getGameStateProperty().addListener(gameStateListener);
    }

    public ImageView getImageView() {
        return imageView;
    }

    private void onCapacityUpdate(int capacity) {
        capacityLabel.setText(capacity + "/" + cart.getMaxCapacity());
        if (capacity == cart.getMaxCapacity()) {
            var fullCartImage = getGameEnvironment().getAssetLoader().getCartImage(cart.getResourceType(), true);
            imageView.setImage(fullCartImage);
        }
    }
    private void onCartStateUpdate(CartState cartState) {
        switch (cartState) {
            case TRAVERSING_PATH -> pathTransition.play();
            case COMPLETE_PATH -> {
                var overlay = getGameEnvironment().getController().getOverlay();
                overlay.getChildren().remove(wrapper);
                pathTransition.stop();
                cart.getCurrentCapacityProperty().removeListener(cartCapacityListener);
                cart.getCartStateProperty().removeListener(cartStateListener);
                cart.getVelocityModifierProperty().removeListener(cartVelocityModifierListener);
                getGameEnvironment().getStateHandler().getGameStateProperty().removeListener(gameStateListener);
            }
        }
    }
    private void onCartVelocityModifierChange(float modifier) {
        var totalTime = pathTransition.getDuration();
        var elapsedTime = pathTransition.getCurrentTime();
        double percentageCompleted = elapsedTime.toMillis() / totalTime.toMillis();
        var newDuration = getGameEnvironment().getMap().calculatePathDuration(cart.getVelocity());
        pathTransition.setDuration(newDuration);
        pathTransition.stop();
        pathTransition.jumpTo(newDuration.multiply(percentageCompleted));
        //pathTransition.pause();
        //pathTransition.jumpTo(elapsedTime.multiply(ratioCompleted));
    }
    private void onGameStateUpdate(GameState gameState) {
        if (cart.getCartState() != CartState.TRAVERSING_PATH)
            return;
        if (gameState == GameState.ROUND_PAUSE)
            pathTransition.pause();
        if (gameState == GameState.ROUND_ACTIVE)
            pathTransition.play();
    }
}
