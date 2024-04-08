package seng201.team53.items;

import seng201.team53.game.Tickable;

import java.util.EnumSet;

public class Cart implements Tickable {
    private final int maxCapacity;
    private final float velocity;
    private final EnumSet<ResourceType> acceptedResources;
    private int position;
    private int currentCapacity;

    public Cart(int maxCapacity, float velocity, EnumSet<ResourceType> acceptedResources) {
        this.maxCapacity = maxCapacity;
        this.velocity = velocity;
        this.acceptedResources = acceptedResources;
    }

    public int getMaxCapacity() {
        throw new UnsupportedOperationException("Unimplemented method 'getMaxCapacity'");
    }

    public int getCapacity() {
        throw new UnsupportedOperationException("Unimplemented method 'getCapacity'");
    }

    public Boolean isFull() {
        return this.getCapacity() >= this.getMaxCapacity();
    }

    public EnumSet<ResourceType> getAcceptedResources() {
        throw new UnsupportedOperationException("Unimplemented method 'getAcceptedResources'");
    }

    /**
     * @return The current position/progress of this cart on the track
     */
    public int getPosition() {
        throw new UnsupportedOperationException("Unimplemented method 'getPosition'");
    }

    @Override
    public void tick() {

    }
}
