package seng201.team53.items.towers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng201.team53.game.Tickable;
import seng201.team53.game.map.Map;
import seng201.team53.items.Purchasable;
import seng201.team53.items.ResourceType;

public abstract class Tower implements Purchasable, Tickable {
    private String name;
    private ImageView imageView;
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
        this.resourceType = resourceType;
        this.resourceAmount = 0;
        this.xpLevel = 0;

        this.imageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        this.imageView.setFitWidth(Map.TILE_WIDTH);
        this.imageView.setFitHeight(Map.TILE_HEIGHT);
    }

    @Override
    public String getName() {
        return name;
    }

    public ImageView getImageView() {
        return imageView;
    }

    @Override
    public void tick() {

    }
}
