package seng201.team53.items;

import javafx.animation.PauseTransition;
import javafx.beans.property.*;
import javafx.util.Duration;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.state.CartState;
import seng201.team53.game.state.GameState;
import seng201.team53.items.upgrade.Upgradeable;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class Cart implements Upgradeable {
    private final int maxCapacity;
    private final float velocity;
    private final ResourceType resourceType;
    private final Property<CartState> cartStateProperty = new SimpleObjectProperty<>(CartState.WAITING);
    private final IntegerProperty currentCapacityProperty = new SimpleIntegerProperty();
    private final FloatProperty velocityModifier = new SimpleFloatProperty(1f);

    public Cart(int maxCapacity, float velocity, ResourceType acceptedResources, Duration spawnDelay) {
        this.maxCapacity = maxCapacity;
        this.velocity = velocity;
        this.resourceType = acceptedResources;

        // Wait for the round to start, then after waiting for spawnDelay, set the cart to start traversing the path.
        getGameEnvironment().getStateHandler().getGameStateProperty().addListener(($, oldState, newState) -> {
            if (newState == GameState.ROUND_ACTIVE && oldState == GameState.ROUND_NOT_STARTED) {
                PauseTransition pause = new PauseTransition(spawnDelay);
                pause.setOnFinished(event -> setCartState(CartState.TRAVERSING_PATH));
                pause.play();
            }
        });

        // Increase points when cart becomes full
        getCurrentCapacityProperty().addListener(($, oldCapacity, newCapacity) -> {
            if (newCapacity.intValue() == getMaxCapacity())
                GameEnvironment.getGameEnvironment().addPoints(10);
        });
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public IntegerProperty getCurrentCapacityProperty() {
        return currentCapacityProperty;
    }

    public int getCapacity() {
        return currentCapacityProperty.get();
    }

    public boolean isFull() {
        return this.getCapacity() >= this.getMaxCapacity();
    }

    private void addCapacity() {
        currentCapacityProperty.set(getCapacity() + 1);
    }

    public void fill() {
        currentCapacityProperty.set(maxCapacity);
    }

    public Property<CartState> getCartStateProperty() {
        return cartStateProperty;
    }

    public CartState getCartState() {
        return cartStateProperty.getValue();
    }

    public void setCartState(CartState cartState) {
        cartStateProperty.setValue(cartState);
    }

    public float getVelocity() {
        return velocity * getVelocityModifier();
    }

    public FloatProperty getVelocityModifierProperty() {
        return velocityModifier;
    }

    public float getVelocityModifier() {
        return velocityModifier.get();
    }

    public void decreaseVelocityModifier() {
        velocityModifier.set(getVelocityModifier() * 0.75f);
    }

    public void addResource(ResourceType generatedResourceType) {
        if (isFull())
            return;
        if (resourceType != generatedResourceType)
            return;
        addCapacity();
    }

    public ResourceType getResourceType() {
        return resourceType;
    }
}
