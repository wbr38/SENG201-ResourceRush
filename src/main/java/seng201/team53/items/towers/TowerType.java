package seng201.team53.items.towers;

import java.time.Duration;

import javafx.scene.image.Image;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.assets.AssetLoader;
import seng201.team53.items.Purchasable;
import seng201.team53.items.ResourceType;

public class TowerType extends Purchasable<Tower> {

    protected final String name;
    protected final String description;
    protected final ResourceType resourceType;
    protected final String imagePath;
    protected final String brokenImagepath;
    protected final int costPrice;
    protected final int resourceAmount;
    protected final Duration reloadSpeed;

    private final Image image;
    private final Image brokenImage;

    TowerType(String name, String description, ResourceType resourceType, String imagePath, String brokenImagePath, int costPrice,
              int resourceAmount, Duration reloadSpeed) {
        this.name = name;
        this.description = description;
        this.resourceType = resourceType;
        this.costPrice = costPrice;
        this.resourceAmount = resourceAmount;
        this.reloadSpeed = reloadSpeed;

        this.imagePath = imagePath;
        this.brokenImagepath = brokenImagePath;

        this.image = AssetLoader.readImage(imagePath);
        this.brokenImage = AssetLoader.readImage(brokenImagePath);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Image getImage() {
        return image;
    }

    public Image getBrokenImage() {
        return brokenImage;
    }

    public Duration getReloadSpeed() {
        return reloadSpeed;
    }

    @Override
    public int getCostPrice() {
        return costPrice;
    }

    @Override
    public int getSellPrice() {
        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        int sellPrice = (int)Math.round(costPrice * difficulty.getSellPriceModifier());
        return sellPrice;
    }

    @Override
    public boolean isSellable() {
        return true;
    }

    public Tower create() {
        return new Tower(this);
    }
}
