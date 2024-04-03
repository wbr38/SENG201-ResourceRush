package seng201.team53.items;

import java.util.EnumSet;

public class Cart {

    private int maxCapacity;
    private int currentCapacity;
    private EnumSet<ResourceType> acceptedResources;
    private float velocity;
    private int position;

    public Cart() {
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
}
