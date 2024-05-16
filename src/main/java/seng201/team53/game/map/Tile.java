package seng201.team53.game.map;

import seng201.team53.items.towers.Tower;

/**
 * Represents a tile on the game map which stores properties relating to build-ability, location and if it is a
 * path tile
 */
public class Tile {
    private final boolean buildable;
    private final boolean path;
    private final int x;
    private final int y;
    private Tower tower;

    /**
     * Constructs a new tile with the specified properties
     * @param buildable If the tile can have a tower built on it
     * @param path If the tile is a part of the path
     * @param x The x-coordinate of the tile on the map
     * @param y The y-coordinate of the tile on the map
     */
    public Tile(boolean buildable, boolean path, int x, int y) {
        this.buildable = buildable;
        this.path = path;
        this.x = x;
        this.y = y;
    }

    /**
     * Determines if a tower can be placed on this tile
     * @return true if a tower can be placed, false otherwise
     */
    public boolean canPlaceTower() {
        Tower tower = this.getTower();
        return (
            this.isBuildable()
            && !this.isPath()
            && tower == null
        );
    }

    /**
     * Determines if a tower occupying this tile can be moved
     * @return true If the tower can be moved, false otherwise
     */
    public boolean canMoveTower() {
        Tower tower = this.getTower();
        return (
            tower != null
            && !tower.isBroken()
        );
    }

    /**
     * Check if towers can be placed onto this tile
     * @return true if the tile is buildable and not a path tile, false otherwise
     */
    public boolean isBuildable() {
        return buildable & !path;
    }

    /**
     * Check if this tile is a part of the path
     * @return true if the tile is a part of the path, false otherwise
     */
    public boolean isPath() {
        return path;
    }

    /**
     * Retrieves the x-coordinate of this tile
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of this tile
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Retrieves the tower placed on this tile
     * @return The tower on this tile, or null if no tower is present
     */
    public Tower getTower() {
        return tower;
    }

    /**
     * Sets a tower on this tile
     * @param tower The tower to be placed on this tile
     */
    public void setTower(Tower tower) {
        if (!buildable)
            throw new IllegalStateException("Tile is marked as a path tile or not buildable");
        this.tower = tower;
    }
}
