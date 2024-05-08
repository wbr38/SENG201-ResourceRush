package seng201.team53.items.towers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng201.team53.game.assets.AssetLoader;
import seng201.team53.game.map.Map;
import seng201.team53.items.Purchasable;
import seng201.team53.items.ResourceType;

import java.time.Duration;

public enum TowerType implements Purchasable {
    LUMBER_MILL("Lumber Mill",
            "A Lumber Mill produces wood",
            ResourceType.WOOD,
            "/assets/items/wood_tower.png",
            "/assets/items/wood_tower_broken.png",
            100,
            50,
            1,
            Duration.ofSeconds(1)),
    MINE("Mine",
            "A Mine produces ores",
            ResourceType.STONE,
            "/assets/items/wood_tower.png",
            "/assets/items/wood_tower_broken.png",
            120,
            50,
            1,
            Duration.ofSeconds(1)),
    QUARRY("Quarry",
            "A Quarry produces stone",
            ResourceType.ORE,
            "/assets/items/wood_tower.png",
            "/assets/items/wood_tower_broken.png",
            150,
            50,
            1,
            Duration.ofSeconds(1)),
    WIND_MILL("Windmill",
            "A wind mill produces energy",
            ResourceType.ENERGY,
            "/assets/items/wood_tower.png",
            "/assets/items/wood_tower_broken.png",
            200,
            50,
            1,
            Duration.ofSeconds(1));

    private final String name;
    private final String description;
    private final ResourceType resourceType;
    private final Image image;
    private final Image brokenImage;
    private final int costPrice;
    private final int sellPrice;
    private final int resourceAmount;
    private final Duration reloadSpeed;

    TowerType(String name, String description, ResourceType resourceType, String imagePath, String brokenImagePath, int costPrice, int sellPrice, int resourceAmount, Duration reloadSpeed) {
        this.name = name;
        this.description = description;
        this.resourceType = resourceType;
        this.costPrice = costPrice;
        this.sellPrice = sellPrice;
        this.resourceAmount = resourceAmount;
        this.reloadSpeed = reloadSpeed;
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
        return sellPrice;
    }

    @Override
    public boolean isSellable() {
        return true;
    }

    public Tower create() {
        var imageView = new ImageView();
        imageView.setFitHeight(Map.TILE_HEIGHT);
        imageView.setFitWidth(Map.TILE_WIDTH);
        imageView.setImage(this.image);
        return new Tower(this, imageView);
    }
}
