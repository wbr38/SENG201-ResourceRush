package seng201.team53.items;

import javafx.animation.PathTransition;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng201.team53.game.Tickable;

import java.util.EnumSet;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class Cart implements Tickable {
    private final int maxCapacity;
    private final float velocity;
    private final EnumSet<ResourceType> acceptedResources;
    private final int spawnAfterTicks; // so we can spawn them after each other - don't all render on top of each other
    private final Image image;
    private int currentCapacity;
    private int lifetimeTicks = 0;
    private PathTransition pathTransition;
    private Label capacityLabel;
    private ImageView imageView;
    private boolean completedPath = false;

    public Cart(int maxCapacity, float velocity, EnumSet<ResourceType> acceptedResources, int spawnAfterTicks, Image image) {
        this.maxCapacity = maxCapacity;
        this.velocity = velocity;
        this.acceptedResources = acceptedResources;
        this.spawnAfterTicks = spawnAfterTicks;
        this.image = image;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCapacity() {
        return currentCapacity;
    }

    public boolean isFull() {
        return this.getCapacity() >= this.getMaxCapacity();
    }

    public float getVelocity() {
        return velocity;
    }

    public EnumSet<ResourceType> getAcceptedResources() {
        return acceptedResources;
    }

    public int getSpawnAfterTicks() {
        return spawnAfterTicks;
    }

    public int getLifetimeTicks() {
        return lifetimeTicks;
    }

    public void setCompletedPath(boolean completedPath) {
        this.completedPath = completedPath;
    }

    public PathTransition getPathTransition() {
        return pathTransition;
    }
    public void setPathTransition(PathTransition pathTransition) {
        this.pathTransition = pathTransition;
    }

    public Image getImage() {
        return image;
    }

    public boolean isCompletedPath() {
        return completedPath;
    }

    public void addResource(ResourceType generatedResourceType) {
        if (currentCapacity == maxCapacity)
            return;
        if (!acceptedResources.contains(generatedResourceType))
            return;
        currentCapacity++;
        update();
    }

    public void setCapacityLabel(Label capacityLabel) {
        this.capacityLabel = capacityLabel;
    }
    public void update() {
        if (capacityLabel == null || imageView == null)
            return;
        capacityLabel.setText(currentCapacity + "/" + maxCapacity);
        if (isFull()) {
            var fullCartImage = getGameEnvironment().getAssetLoader().getFullCartImage();
            imageView.setImage(fullCartImage);
        }
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void tick() {
        lifetimeTicks++;
    }
}
