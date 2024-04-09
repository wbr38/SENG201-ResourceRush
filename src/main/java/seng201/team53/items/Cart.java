package seng201.team53.items;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import seng201.team53.App;
import seng201.team53.game.Tickable;
import seng201.team53.game.map.Map;

import java.util.EnumSet;

public class Cart implements Tickable {
    private final int maxCapacity;
    private final float velocity;
    private final EnumSet<ResourceType> acceptedResources;
    private final int spawnAfterTicks; // so we can spawn them after each other - don't all render on top of each other
    private int position;
    private int currentCapacity;
    private int lifetimeTicks = 0;
    private ImageView imageView;

    public Cart(int maxCapacity, float velocity, EnumSet<ResourceType> acceptedResources, int spawnAfterTicks) {
        this.maxCapacity = maxCapacity;
        this.velocity = velocity;
        this.acceptedResources = acceptedResources;
        this.spawnAfterTicks = spawnAfterTicks;
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
        if (spawnAfterTicks == lifetimeTicks) {
            imageView = new ImageView(App.getApp().getGameEnvironment().getMapLoader().getCartImage());
            imageView.setFitHeight(Map.TILE_HEIGHT);
            imageView.setFitWidth(Map.TILE_WIDTH);
        }
        lifetimeTicks++;
    }
    @Override
    public void render() {

    }
}
