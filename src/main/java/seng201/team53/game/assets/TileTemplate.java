package seng201.team53.game.assets;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng201.team53.game.map.Tile;

import java.awt.*;

// I have no idea what to call this class lol
public class TileTemplate {
    private final boolean buildable;
    private final boolean path;
    private final Image image;

    public TileTemplate(boolean buildable, boolean path, Image image) {
        this.buildable = buildable;
        this.path = path;
        this.image = image;
    }

    public Tile createTile(Point point) {
        return new Tile(new ImageView(image),
            buildable,
            path,
            point);
    }
}
