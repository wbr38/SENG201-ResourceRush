package seng201.team53.game.map;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import seng201.team53.App;
import seng201.team53.items.towers.Tower;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Map {

    public static final int TILE_HEIGHT = 40;
    public static final int TILE_WIDTH = 40;
    private static final int[] X_DIRECTIONS = { -1, 0, 1, 0 };
    private static final int[] Y_DIRECTIONS = { 0, 1, 0, -1 };
    private final String name;
    private final Tile[][] tiles;
    private final Stack<Point> path = new Stack<>();
    private final ArrayList<Tower> towers = new ArrayList<>();

    private MapInteraction currentInteraction = MapInteraction.NONE;
    private Tower selectedTower; 

    public Map(String name, Tile[][] tiles) {
        this.name = name;
        this.tiles = tiles;
    }

    public String getName() {
        return name;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
    public Tile getTileAt(int tileX, int tileY) {
        // because we are using a 2D array to store tiles, we have to get the row first (y value)
        // then get the column (x value) hence tiles[tileY][tileX]
        if (tileX >= 0 && tileX < tiles.length && tileY >= 0 && tileY < tiles[tileX].length)
            return tiles[tileY][tileX];
        throw new RuntimeException("Tile does not exist at x=" + tileX + ", y=" + tileY);
    }
    public Tile getTileFromScreenPosition(int screenX, int screenY) {
        // screen x consists of 20 columns of width 40px each
        // screen y consists of 20 rows of height 40px each
        int tileX = Math.floorDiv(screenX, TILE_WIDTH);
        int tileY = Math.floorDiv(screenY, TILE_HEIGHT);
        return getTileAt(tileX, tileY);
    }

    public MapInteraction getCurrentInteraction() {
        return currentInteraction;
    }
    public void setInteraction(MapInteraction interaction) {
        currentInteraction = interaction;

        // When the user is interacting with the map, scale the tiles down slightly
        // (scuffed way to make a border/highlight on each tile)
        if (interaction != MapInteraction.NONE) {
            scaleTiles(0.95);
        } else {
            scaleTiles(1);
        }
    }

    public Tower getSelectedTower() {
        return selectedTower;
    }
    public void startPlacingTower(Tower tower) {
        this.setInteraction(MapInteraction.PLACE_TOWER);
        selectedTower = tower;
    }
    public void stopPlacingTower() {
        this.selectedTower = null;
    }
    public void placeTower(Tower tower, Tile tile) {
        var gameController = App.getApp().getGameEnvironment().getWindow().getController();
        var gridPane = gameController.getGridPane();

        ImageView imageView = tower.getImageView();
        Point point = tile.getPoint();
        gridPane.add(imageView, point.x, point.y);
        towers.add(tower);
        tile.setTower(tower);
        // add tower to both the tile and to the maps array list
        // makes it faster to loop through all towers
        // or to find a tower at a given x,y
    }

    public void drawPath(Canvas canvas) { // use for debugging the path finding algorithm
        var graphics = canvas.getGraphicsContext2D();
        for (int i = 0; i < path.size(); i++) {
            var point = path.get(i);
            int screenX = 40 * point.x;
            int screenY = 40 * point.y;
            if (i != path.size() - 1) { // not the last point in path
                var nextPoint = path.get(i + 1);
                if (point.y - nextPoint.y == 1) { // LEFT
                    graphics.fillRect(screenY - 20, screenX + 15, 40, 10);
                }
                if (nextPoint.y - point.y == 1) { // RIGHT
                    graphics.fillRect(screenY + 20, screenX + 15, 40, 10);
                }
                if (point.x - nextPoint.x == 1) { // UP
                    graphics.fillRect(screenY + 15, screenX - 20, 10, 40);
                }
                if (nextPoint.x - point.x == 1) { // DOWN
                    graphics.fillRect(screenY + 15, screenX + 20, 10, 40);
                }
            }
        }
    }

    public void findPath(int startX, int startY, int endX, int endY) {
        if (!path.isEmpty())
            throw new IllegalStateException("Map already has a path calculated.");
        int[][] discovered = new int[tiles.length][tiles[0].length];
        if (!depthFirstSearch(discovered, startX, startY, endX, endY))
            throw new RuntimeException("Invalid map, does not contain a path");
    }

    private boolean depthFirstSearch(int[][] discovered, int x, int y, int endX, int endY) {
        discovered[x][y] = 2;
        if (x == endX && y == endY) {
            path.push(new Point(x, y));
            return true;
        }
        for (int i = 0; i < X_DIRECTIONS.length; i++) {
            int newX = x + X_DIRECTIONS[i];
            int newY = y + Y_DIRECTIONS[i];
            if (!isValidCell(discovered, newX, newY))
                continue;
            path.push(new Point(x, y));
            if (depthFirstSearch(discovered, newX, newY, endX, endY))
                return true;
            path.pop();
        }
        return false;
    }

    private boolean isValidCell(int[][] discovered, int x, int y) {
        return x >= 0 && y >= 0 && x < tiles.length && y < tiles[x].length && tiles[x][y].isPath()
                && discovered[x][y] == 0;
    }

    private void scaleTiles(double value) {
        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                tile.getImageView().setScaleX(value);
                tile.getImageView().setScaleY(value);
            }
        }
    }
}
