package seng201.team53.items.towers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng201.team53.game.map.Map;
import seng201.team53.items.Purchasable;
import seng201.team53.items.ResourceType;

public abstract class Tower implements Purchasable {
    
    private String name;
    private Image image;
    public ResourceType resourceType;
    public int resourceAmount;
    public float reloadSpeed;
    public int xpLevel;

    Tower(
        String name,
        String imagePath,
        ResourceType resourceType
    ) {
        this.name = name;
        this.image = new Image(getClass().getResourceAsStream(imagePath));
        this.resourceType = resourceType;
        this.resourceAmount = 0;
        this.xpLevel = 0;
    }

    public abstract void getUpgrades();
    public abstract String getSpriteFilePath();

    public ImageView getImageView() {
        var imageView = new ImageView(image);
        imageView.setFitWidth(Map.TILE_WIDTH);
        imageView.setFitHeight(Map.TILE_HEIGHT);
        return imageView;
    }

    @Override
    public String getName() {
        return name;
    }
}
