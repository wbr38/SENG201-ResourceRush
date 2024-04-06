package seng201.team53.game.map;

import java.awt.Point;
import javafx.scene.image.ImageView;

public class Tile {
    private final ImageView imageView;
    private final boolean buildable;
    private final boolean path;
    private final Point point;

    public Tile(ImageView imageView, boolean buildable, boolean path, Point point) {
        this.imageView = imageView;
        this.buildable = buildable;
        this.path = path;
        this.point = point;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public boolean isBuildable() {
        return buildable;
    }

    public boolean isPath() {
        return path;
    }

    public Point getPoint() {
        return point;
    }
}
