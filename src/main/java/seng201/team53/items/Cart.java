package seng201.team53.items;

import javafx.beans.property.*;
import seng201.team53.game.Tickable;
import seng201.team53.game.state.CartState;
import seng201.team53.items.upgrade.Upgradeable;

import java.util.EnumSet;

public class Cart implements Tickable, Upgradeable {
    private final int maxCapacity;
    private final float velocity;
    private final EnumSet<ResourceType> acceptedResources;
    private final int spawnAfterTicks; // so we can spawn them after each other - don't all render on top of each other
    private final Property<CartState> cartStateProperty = new SimpleObjectProperty<>(CartState.WAITING);
    private final IntegerProperty currentCapacityProperty = new SimpleIntegerProperty();
    private final FloatProperty velocityModifier = new SimpleFloatProperty(1f);

    public Cart(int maxCapacity, float velocity, EnumSet<ResourceType> acceptedResources, int spawnAfterTicks) {
        this.maxCapacity = maxCapacity;
        this.velocity = velocity;
        this.acceptedResources = acceptedResources;
        this.spawnAfterTicks = spawnAfterTicks;
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

    public void addCapacity() {
        currentCapacityProperty.set(getCapacity() + 1);
    }

    public void fill() {
        currentCapacityProperty.set(maxCapacity);
    }

    public int getSpawnAfterTicks() {
        return spawnAfterTicks;
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

    public void addVelocityModifier() {
        velocityModifier.set(getVelocityModifier() * 0.75f);
    }

    public void addResource(ResourceType generatedResourceType) {
        if (isFull())
            return;
        if (!acceptedResources.contains(generatedResourceType))
            return;
        addCapacity();
    }

    @Override
    public void tick(int lifetime) {
        if (getCartState() == CartState.WAITING) {
            if (spawnAfterTicks == lifetime) {
                setCartState(CartState.TRAVERSING_PATH);
            }
        }
    }
}
