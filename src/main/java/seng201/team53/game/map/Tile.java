package seng201.team53.game.map;

import seng201.team53.items.towers.Tower;

public class Tile {
    private final boolean buildable;
    private final boolean path;
    private final int x;
    private final int y;
    private Tower tower;

    public Tile(boolean buildable, boolean path, int x, int y) {
        this.buildable = buildable;
        this.path = path;
        this.x = x;
        this.y = y;
    }

    /**
     * @return Whether a Tower can be placed on this tile.
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
     * @return If the tower occupying this tile (if there is one) can be moved.
     */
    public boolean canMoveTower() {
        Tower tower = this.getTower();
        return (
            tower != null
            && !tower.isBroken()
        );
    }

    /**
     * @return Whether items can be placed onto this tile
     */
    public boolean isBuildable() {
        return buildable & !path;
    }

    public boolean isPath() {
        return path;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        if (!buildable)
            throw new IllegalStateException("Tile is marked as a path tile or not buildable");
        this.tower = tower;
    }
}
