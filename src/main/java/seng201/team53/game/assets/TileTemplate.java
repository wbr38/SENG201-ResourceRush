package seng201.team53.game.assets;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng201.team53.game.map.Tile;

/**
 * This class represents a template for a tile on a map. It stores information about the tile's properties before
 * the actual map is created.
 */
public class TileTemplate {
    private final boolean buildable;
    private final boolean path;
    private final Image image;

    /**
     * Constructs a new TileTemplate with the specified properties.
     *
     * @param buildable True if a building can be constructed on this tile, false otherwise.
     * @param path True if this tile is considered a path tile, false otherwise.
     * @param image The image associated with this tile.
     */
    public TileTemplate(boolean buildable, boolean path, Image image) {
        this.buildable = buildable;
        this.path = path;
        this.image = image;
    }

    /**
     * Creates a new Tile object based on this template's information.
     *
     * @param x The X coordinate of the tile on the grid (zero-based indexing)
     * @param y The Y coordinate of the tile on the grid (zero-based indexing)
     * @return A new Tile object with the properties defined in this template.
     */
    public Tile createTile(int x, int y) {
        return new Tile(new ImageView(image),
                buildable,
                path,
                x,
                y);
    }
}
