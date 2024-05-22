package seng201.team53.game.map;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;
import seng201.team53.exceptions.TileNotFoundException;
import seng201.team53.game.items.towers.Tower;
import seng201.team53.service.PathFinderService;

import java.util.Collection;

/**
 * This class represents a map in the game. It stores information about the map grid, tiles and placed towers
 * A map consists of a 2D array of tiles, where each tile represents a specific location on the map
 */
public class GameMap {
    public static final int TILE_HEIGHT = 40;
    public static final int TILE_WIDTH = 40;
    private final String name;
    private final Tile[][] tiles;
    private final Polyline polylinePath;
    private final MapProperty<Tower, Tile> towersProperty = new SimpleMapProperty<>(FXCollections.observableHashMap());
    private final int pathLength;
    private MapInteraction interaction = MapInteraction.NONE;

    /**
     * Constructs a new Map with the specified properties
     * @param name The name of the map
     * @param tiles The matrix of map tiles
     * @param startX The paths starting grid x coordinate
     * @param startY The paths starting grid y coordinate
     * @param endX The paths ending grid x co-ordinate
     * @param endY The paths ending grid y co-ordinate
     */
    public GameMap(String name, Tile[][] tiles, int startX, int startY, int endX, int endY) {
        this.name = name;
        this.tiles = tiles;

        var pathFinderService = new PathFinderService();
        pathFinderService.findPath(tiles, startX, startY, endX, endY);
        this.polylinePath = pathFinderService.generatePathPolyline();
        this.pathLength = pathFinderService.calculatePathLength();
    }

    /**
     * @return The name of the map
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the tile object at the specified coordinates on the map grid
     * @param tileX The X coordinate of the tile on the grid (zero-based indexing)
     * @param tileY The Y coordinate of the tile on the grid (zero-based indexing)
     * @return The tile object at the given coordinates, or null if the coordinates are outside the map bounds
     * @throws TileNotFoundException If the requested tile coordinates are outside the valid range of the map
     */
    public Tile getTileAt(int tileX, int tileY) throws TileNotFoundException {
        if (tileX >= 0 && tileX < tiles.length && tileY >= 0 && tileY < tiles[tileX].length)
            return tiles[tileY][tileX];
        throw new TileNotFoundException("Tile does not exist at x=" + tileX + ", y=" + tileY);
    }

    /**
     * Retrieves the tile object at the specified coordinates on the map grid
     * @param screenX The X coordinate of the screen (in pixels)
     * @param screenY The Y coordinate of the screen (in pixels)
     * @return The tile object at the given coordinates, or null if the coordinates are outside the map bounds
     */
    public Tile getTileFromScreenPosition(int screenX, int screenY) throws TileNotFoundException {
        int tileX = Math.floorDiv(screenX, TILE_WIDTH);
        int tileY = Math.floorDiv(screenY, TILE_HEIGHT);
        return getTileAt(tileX, tileY);
    }

    /**
     * @return The polyline version of the complete path
     */
    public Polyline getPolylinePath() {
        return polylinePath;
    }

    /**
     * Calculates the duration it takes to travel the path at a given velocity
     * @param velocity the speed in tiles per second which the path should be travelled at
     * @return the duration object representing the time it will take to travel the path
     */
    public Duration calculatePathDuration(float velocity) {
        float duration = pathLength / velocity;
        return Duration.seconds(duration);
    }

    /**
     * Retrieves the towers property that maps towers to their tiles
     * This property is observable, meaning it can be watched for changes
     * @return The observable MapProperty of towers to tiles
     */
    public MapProperty<Tower, Tile> getTowersProperty() {
        return towersProperty;
    }

    /**
     * @return Retrieves the collect of towers currently placed on the map
     */
    public Collection<Tower> getTowers() {
        return getTowersProperty().keySet();
    }

    /**
     * Adds a tower to the map
     * @param tower The tower object to be placed on the map
     * @param tile The tile object where the tower is to be placed
     */
    public void addTower(Tower tower, Tile tile) {
        towersProperty.put(tower, tile);
        tile.setTower(tower);
    }

    /**
     * Removes a tower from the map
     * @param tower The tower object to be removed from the map
     */
    public void removeTower(Tower tower) {
        Tile tile = towersProperty.get(tower);
        towersProperty.remove(tower);
        tile.setTower(null);
    }

    /**
     * Retrieves the current map interaction state
     * @return The map interaction state
     */
    public MapInteraction getInteraction() {
        return interaction;
    }

    /**
     * Sets the current map interaction state
     * @param interaction The new map interaction state
     */
    public void setInteraction(MapInteraction interaction) {
        this.interaction = interaction;
    }
}
