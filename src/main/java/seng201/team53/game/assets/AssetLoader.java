package seng201.team53.game.assets;

import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import seng201.team53.App;
import seng201.team53.game.map.Map;
import seng201.team53.game.map.Tile;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * This class is responsible for loading the tile templates, game maps, and cart image
 */
public class AssetLoader {
    private final HashMap<Integer, TileTemplate> tileTemplates = new HashMap<>();
    private final JSONParser jsonParser = new JSONParser();
    private Image cartImage;

    /**
     * Initializes the MapLoader by loading the tiles
     */
    public void init() {
        try {
            loadTiles();
            loadCartImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a map from a JSON file
     * 
     * @param name The name of the map
     * @param path The path to the JSON file resource
     * @return The loaded map
     */
    public seng201.team53.game.map.Map loadMap(String name, String path) {
        var json = (JSONObject)readJsonResource(path);
        var startPosition = (JSONObject)json.get("start_position");
        var startPositionX = (int)(long)startPosition.get("y");
        var startPositionY = (int)(long)startPosition.get("x");
        var endPosition = (JSONObject)json.get("end_position");
        var endPositionX = (int)(long)endPosition.get("y");
        var endPositionY = (int)(long)endPosition.get("x");
        var mapMatrix = (JSONArray)json.get("map_matrix");
        var gameController = App.getApp().getGameEnvironment().getWindow().getController();
        var gridPane = gameController.getGridPane();
        var tiles = readMapMatrix(mapMatrix, gridPane);
        return new seng201.team53.game.map.Map(name, tiles, startPositionX, startPositionY, endPositionX, endPositionY);
    }

    public Image getCartImage() {
        return cartImage;
    }

    /**
     * Loads a list of tile templates from a JSON file
     * 
     * @throws IOException If an I/O error occurs
     */
    private void loadTiles() throws IOException {
        var objects = (JSONArray)readJsonResource("/assets/tiles/tiles.json");
        for (var object : objects) {
            var properties = (JSONObject)object;
            var tileId = (int)(long)properties.get("tile_id");
            var buildable = (boolean)properties.get("buildable");
            var path = (boolean)properties.get("path");
            var imagePath = (String)properties.get("image_path");
            var image = readImage(imagePath);
            tileTemplates.put(tileId, new TileTemplate(buildable, path, image));
        }
    }

    /**
     * Loads the cart image
     * 
     * @throws IOException If an I/O error occurs
     */
    private void loadCartImage() throws IOException {
        cartImage = readImage("/assets/cart.png");
    }

    /**
     * Reads the map matrix from a JSON array
     * 
     * @param mapMatrix The JSON array holding the map matrix
     * @param gridPane  The grid pane which displays the maps tiles
     * @return The map matrix represented by tiles
     */
    private Tile[][] readMapMatrix(JSONArray mapMatrix, GridPane gridPane) {
        int lastRowSize = -1;
        var tiles = new Tile[mapMatrix.size()][];
        for (int y = 0; y < mapMatrix.size(); y++) {
            var innerArray = (JSONArray)mapMatrix.get(y);
            // ensure every row has same amount of columns otherwise we get errors later
            if (lastRowSize == -1) {
                lastRowSize = innerArray.size();
            } else if (lastRowSize != innerArray.size()) {
                throw new RuntimeException("Map matrix contains rows with different sizes");
            }
            tiles[y] = new Tile[innerArray.size()];
            for (int x = 0; x < innerArray.size(); x++) {
                var tileId = (int)(long)innerArray.get(x);
                var tileTemplate = tileTemplates.get(tileId);
                if (tileTemplate == null)
                    throw new RuntimeException("Missing tile template for id '" + tileId + "'");

                var tile = tileTemplate.createTile(new Point(x, y));
                var imageView = tile.getImageView();
                tiles[y][x] = tile;
                imageView.setFitHeight(seng201.team53.game.map.Map.TILE_HEIGHT);
                imageView.setFitWidth(Map.TILE_WIDTH);
                gridPane.add(imageView, x, y); // idk why it has to be backwards
            }
        }
        return tiles;
    }

    /**
     * Reads a JSON resource from a given path
     * 
     * @param path The path to the JSON resource
     * @return The parsed JSON object
     */
    private Object readJsonResource(String path) {
        try (var resource = getClass().getResourceAsStream(path)) {
            if (resource == null)
                throw new RuntimeException("Could not read resource '" + path + "'.");
            var inputStreamReader = new InputStreamReader(resource);
            return jsonParser.parse(inputStreamReader);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads an image resource from a given path
     * 
     * @param path The path to the image resource
     * @return The loaded image
     * @throws IOException If an I/O error occurs
     */
    private Image readImage(String path) throws IOException {
        try (var resource = getClass().getResourceAsStream(path)) {
            if (resource == null)
                throw new RuntimeException("Could not read resource '" + path + "'.");
            return new Image(resource);
        }
    }
}
