package seng201.team53.game.map;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.util.Stack;

public class Map {
    private static final int[] X_DIRECTIONS = {-1, 0, 1, 0};
    private static final int[] Y_DIRECTIONS = {0, 1, 0, -1};
    private final String name;
    private final Tile[][] tiles = new Tile[16][20];
    private final Stack<Point> path = new Stack<>();

    public Map(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public Tile[][] getTiles() {
        return tiles;
    }
    public Tile getTileFromScreenPosition(int screenX, int screenY) {
        // screen x consists of 20 columns of width 40px each
        // screen y consists of 20 rows of height 40px each
        int tileX = Math.floorDiv(screenX, 40);
        int tileY = Math.floorDiv(screenY, 40);
        if (tileX >= 0 && tileX < tiles.length && tileY >= 0 && tileY < tiles[tileX].length)
            return tiles[tileX][tileY];
        throw new RuntimeException("Click out of bounds"); // shouldn't happen but use for testing purposes
    }

    public void draw(GridPane gridPane) {
        for (int column = 0; column < tiles.length; column++) {
            for (int row = 0; row < tiles[column].length; row++) {
                var tile = tiles[column][row];
                var image = tile.getImage();
                var imageView = new ImageView(image);
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
                gridPane.add(imageView, row, column);
            }
        }
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
        return x >= 0 && y >= 0 && x < tiles.length  && y < tiles[x].length && tiles[x][y].isPath() && discovered[x][y] == 0;
    }
}
