package seng201.team53.game.map;

import javafx.scene.image.Image;

public class Tile {
    private final Image image;
    private final boolean buildable;
    private final boolean path;

    public Tile(Image texture, boolean buildable, boolean path) {
        this.image = texture;
        this.buildable = buildable;
        this.path = path;
    }

    public Image getImage() {
        return image;
    }
    public boolean isBuildable() {
        return buildable;
    }
    public boolean isPath() {
        return path;
    }
}
