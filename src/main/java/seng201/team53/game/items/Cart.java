package seng201.team53.game.items;

import javafx.animation.PauseTransition;
import javafx.beans.property.*;
import javafx.util.Duration;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.items.upgrade.Upgradeable;
import seng201.team53.game.state.CartState;
import seng201.team53.game.state.GameState;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

/**
 * Represents a cart. Each cart has a determined max capacity, velocity and resource type. The state of the cart is kept
 * track of in this class along with the current capacity and velocity modifier.
 */
public class Cart implements Upgradeable {
    private final int maxCapacity;
    private final float velocity;
    private final ResourceType resourceType;
    private final Property<CartState> cartStateProperty = new SimpleObjectProperty<>(CartState.WAITING);
    private final IntegerProperty currentCapacityProperty = new SimpleIntegerProperty();
    private final FloatProperty velocityModifier = new SimpleFloatProperty(1f);

    /**
     * Construct a new cart with the given parameters.
     * A listener for the game state property will be added to wait for the game round to start, then after waiting
     * for the spawnDelay time, set the cart to start traversing the path.
     * @param maxCapacity The max capacity of the cart
     * @param velocity The velocity of the cart
     * @param acceptedResource The resource that the cart accepts
     * @param spawnDelay The delay time until the cart spawns and starts collecting resources
     */
    public Cart(int maxCapacity, float velocity, ResourceType acceptedResource, Duration spawnDelay) {
        this.maxCapacity = maxCapacity;
        this.velocity = velocity;
        this.resourceType = acceptedResource;

        getGameEnvironment().getStateHandler().getGameStateProperty().addListener(($, oldState, newState) -> {
            if (newState == GameState.ROUND_ACTIVE && oldState == GameState.ROUND_NOT_STARTED) {
                PauseTransition pause = new PauseTransition(spawnDelay);
                pause.setOnFinished(event -> setCartState(CartState.TRAVERSING_PATH));
                pause.play();
            }
        });
    }

    /**
     * Retrieves the max capacity of the cart. When the capacity is at this max capacity, the cart is full
     * @return The max capacity of the cart
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Retrieves the current capacity property.
     * This property is observable, meaning it can be watched for changes
     * @return The current capacity property
     */
    public IntegerProperty getCurrentCapacityProperty() {
        return currentCapacityProperty;
    }

    /**
     * Retrieves the current capacity
     * @return The current capacity
     */
    public int getCapacity() {
        return currentCapacityProperty.get();
    }

    /**
     * Determines if the cart is full.
     * The cart will be will when the capacity is equal to the maximum capacity
     * @return true if the cart is full, false otherwise
     */
    public boolean isFull() {
        return this.getCapacity() >= this.getMaxCapacity();
    }

    /**
     * Increments the capacity by 1
     */
    private void addCapacity() {
        currentCapacityProperty.set(getCapacity() + 1);
        if (isFull())
            GameEnvironment.getGameEnvironment().addPoints(10);
    }

    /**
     * Fill the cart immediately. Use by the fill cart upgrade
     */
    public void fill() {
        currentCapacityProperty.set(maxCapacity);
    }

    /**
     * Retrieves the current cart state property.
     * This property is observable, meaning it can be watched for changes
     * @return The current cart state property
     */
    public Property<CartState> getCartStateProperty() {
        return cartStateProperty;
    }

    /**
     * Retrieves the current cart state
     * @return The current cart state
     */
    public CartState getCartState() {
        return cartStateProperty.getValue();
    }

    /**
     * Sets the current cart state
     * @param cartState The new state the cart is in
     */
    public void setCartState(CartState cartState) {
        cartStateProperty.setValue(cartState);
    }

    /**
     * Retrieves the cart velocity modifier property.
     * This property is observable, meaning it can be watched for changes
     * @return The current cart velocity property
     */
    public FloatProperty getVelocityModifierProperty() {
        return velocityModifier;
    }

    /**
     * Calculates the velocity of the cart taking into account the velocity modifier
     * @return The calculated velocity of the cart
     */
    public float getVelocity() {
        return velocity * getVelocityModifier();
    }

    /**
     * Retrieves the velocity modifier
     * @return The velocity modifier
     */
    public float getVelocityModifier() {
        return velocityModifier.get();
    }

    /**
     * Decreases the velocity modifier by 25%. This will make the cart move slower.
     */
    public void decreaseVelocityModifier() {
        velocityModifier.set(getVelocityModifier() * 0.75f);
    }

    /**
     * Attempt to add a resource to the cart. If the cart is not full and the given resource type is equal to the carts
     * accept resource, 1 will be added to the capacity
     * @param generatedResourceType The generated resource type
     */
    public void addResource(ResourceType generatedResourceType) {
        if (isFull())
            return;
        if (resourceType != generatedResourceType)
            return;
        addCapacity();
    }

    /**
     * Retrieves the resource type accepted by the cart
     * @return The accepted resource type
     */
    public ResourceType getResourceType() {
        return resourceType;
    }
}
