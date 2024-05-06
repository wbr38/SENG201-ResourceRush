package seng201.team53.items.towers;

import javafx.scene.image.ImageView;
import seng201.team53.game.Tickable;

public class Tower implements Tickable {
    private final TowerType type;
    private final ImageView imageView;
    private boolean broken = false;
    private int xpLevel = 0;

    protected Tower(TowerType type, ImageView imageView) {
        this.type = type;
        this.imageView = imageView;

        setBroken(false);
    }

    public TowerType getType() {
        return type;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
        imageView.setImage(broken ? type.getBrokenImage() : type.getImage());
    }

    public int getXpLevel() {
        return xpLevel;
    }

    public void incrementXpLevel(int amount) {
        this.xpLevel += amount;
    }

    @Override
    public void tick() {
        // add check to see if tower is broken
        // give resources to carts
    }
}
