package seng201.team53.gui.wrapper;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import seng201.team53.game.state.CartState;
import seng201.team53.game.state.GameState;
import seng201.team53.game.items.Cart;
import seng201.team53.game.map.GameMap;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

import java.util.List;

/**
 * Represents a graphical wrapper for a cart.
 * This class contains an instance of a cart and all the javafx elements which allows for the cart's graphics.
 * Change listeners are stored so when the cart is removed, the listeners can also be removed
 */
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

    /**
     * Constructs a new FX cart instance.
     * This constructor setups up the path transition for the cart which allows for the cart to traverse
     * a predefined path. Change listeners are created and added for the carts capacity, state and velocity modifier
     * properties as well as a game state listener
     * @param cart The cart the class is wrapping
     * @param wrapper The pane which encapsulates the carts graphics
     * @param imageView The image view of the cart
     * @param capacityLabel The capacity label of the cart
     */
    public FXCart(Cart cart, Pane wrapper, ImageView imageView, Label capacityLabel) {
        this.cart = cart;
        this.wrapper = wrapper;
        this.imageView = imageView;
        this.capacityLabel = capacityLabel;

        GameMap map = getGameEnvironment().getMap();
        pathTransition = new PathTransition();
        pathTransition.setNode(wrapper);
        pathTransition.setDuration(map.calculatePathDuration(cart.getVelocity()));
        pathTransition.setPath(map.getPolylinePath());
        pathTransition.setInterpolator(Interpolator.LINEAR);

        pathTransition.setOnFinished(event -> {
            cart.setCartState(CartState.COMPLETE_PATH);
            List<Cart> carts = getGameEnvironment().getRound().getCarts();
            boolean allCartsFinished = carts.stream().allMatch(x -> x.getCartState() == CartState.COMPLETE_PATH);
            if (allCartsFinished) {
                getGameEnvironment().addPoints(20);
                getGameEnvironment().getStateHandler().setState(GameState.ROUND_COMPLETE);
            }
        });

        cartCapacityListener = ($, oldCapacity, newCapacity) -> onCapacityUpdate(newCapacity.intValue());
        cartStateListener = ($, oldState, newState) -> onCartStateUpdate(newState);
        gameStateListener = ($, oldState, newState) -> onGameStateUpdate(newState);
        cartVelocityModifierListener = ($, oldModifier, newModifier) -> onCartVelocityModifierChange();
        cart.getCurrentCapacityProperty().addListener(cartCapacityListener);
        cart.getCartStateProperty().addListener(cartStateListener);
        cart.getVelocityModifierProperty().addListener(cartVelocityModifierListener);
        getGameEnvironment().getStateHandler().getGameStateProperty().addListener(gameStateListener);
    }

    /**
     * Retrieves the carts image view
     * @return The carts image view
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Updates the capacity label associated with this
     * @param capacity The current capacity of the cart
     */
    private void onCapacityUpdate(int capacity) {
        capacityLabel.setText(capacity + "/" + cart.getMaxCapacity());
        if (capacity == cart.getMaxCapacity()) {
            Image fullCartImage = getGameEnvironment().getAssetLoader().getCartImage(cart.getResourceType(), true);
            imageView.setImage(fullCartImage);
        }
    }

    /**
     * This method handles when the carts state gets updated.
     * When the cart state is updated to TRAVERSING_PATH, it will play the path transition.
     * When the cart state is updated to COMPLETE_PATH, the path transition stops, graphical elements associated to
     * this cart is removed and listeners are removed
     * @param cartState The new cart state
     */
    private void onCartStateUpdate(CartState cartState) {
        switch (cartState) {
            case TRAVERSING_PATH -> pathTransition.play();
            case COMPLETE_PATH -> {
                Pane overlay = getGameEnvironment().getController().getOverlay();
                overlay.getChildren().remove(wrapper);
                pathTransition.stop();
                cart.getCurrentCapacityProperty().removeListener(cartCapacityListener);
                cart.getCartStateProperty().removeListener(cartStateListener);
                cart.getVelocityModifierProperty().removeListener(cartVelocityModifierListener);
                getGameEnvironment().getStateHandler().getGameStateProperty().removeListener(gameStateListener);
            }
        }
    }

    /**
     * This method handles when the carts velocity modifier gets updated.
     * The time elapsed and new duration is calculated. The transition must be stopped and jump to the previous
     * position it was at before the duration was changed.
     */
    private void onCartVelocityModifierChange() {
        Duration totalTime = pathTransition.getDuration();
        Duration elapsedTime = pathTransition.getCurrentTime();
        double percentageCompleted = elapsedTime.toMillis() / totalTime.toMillis();
        Duration newDuration = getGameEnvironment().getMap().calculatePathDuration(cart.getVelocity());
        pathTransition.setDuration(newDuration);
        pathTransition.stop();
        pathTransition.jumpTo(newDuration.multiply(percentageCompleted));
    }

    /**
     * This method handles when the game state is updated. The cart must be currently traversing the path.
     * If the new game state is ROUND_PAUSE, the cart will stop the path transition.
     * If the new game state is ROUND_ACTIVE, the cart will play the path transition
     * @param gameState The new game state
     */
    private void onGameStateUpdate(GameState gameState) {
        if (cart.getCartState() != CartState.TRAVERSING_PATH)
            return;
        if (gameState == GameState.ROUND_PAUSE)
            pathTransition.pause();
        if (gameState == GameState.ROUND_ACTIVE)
            pathTransition.play();
    }
}
