package seng201.team53.game.assets;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import seng201.team53.game.map.GameMap;
import seng201.team53.game.map.Tile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for loading the tile templates, game maps, and images
 */
public class AssetLoader {
    private final Map<Integer, TileTemplate> tileTemplates = new HashMap<>();
    private final JSONParser jsonParser = new JSONParser();
    private Image cartImage;
    private Image fullCartImage;

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
     * @param name The name of the map
     * @param path The path to the JSON file resource
     * @return The loaded map
     */
    public GameMap loadMap(String name, String path, Pane mapBackgroundPane) {
        var json = (JSONObject)readJsonResource(path);
        var backgroundImage = readImage((String)json.get("background"));
        var startPosition = (JSONObject)json.get("start_position");
        var startPositionX = (int)(long)startPosition.get("y");
        var startPositionY = (int)(long)startPosition.get("x");
        var endPosition = (JSONObject)json.get("end_position");
        var endPositionX = (int)(long)endPosition.get("y");
        var endPositionY = (int)(long)endPosition.get("x");
        var mapMatrix = (JSONArray)json.get("map_matrix");
        var tiles = readMapMatrix(mapMatrix);
        var background = new ImageView(backgroundImage);
        mapBackgroundPane.getChildren().clear();
        background.setFitWidth(mapBackgroundPane.getPrefWidth());
        background.setFitHeight(mapBackgroundPane.getPrefHeight());
        mapBackgroundPane.getChildren().add(background);
        return new GameMap(name, tiles, startPositionX, startPositionY, endPositionX, endPositionY);
    }

    /**
     * Retrieves the image of the cart
     * @return the image representing the cart
     */
    public Image getCartImage() {
        return cartImage;
    }

    /**
     * Retrieves the image of the full cart
     * @return the image representing the full cart
     */
    public Image getFullCartImage() {
        return fullCartImage;
    }

    /**
     * Reads an image resource from a given path
     * @param path The path to the image resource
     * @return The loaded image
     */
    public static Image readImage(String path) {
        try (var resource = AssetLoader.class.getResourceAsStream(path)) {
            if (resource == null)
                throw new RuntimeException("Could not read resource '" + path + "'.");
            return new Image(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a list of tile templates from a JSON file
     * @throws IOException If an I/O error occurs
     */
    private void loadTiles() throws IOException {
        tileTemplates.put(0, new TileTemplate(true, false));
        tileTemplates.put(1, new TileTemplate(false, true));
        tileTemplates.put(2, new TileTemplate(false, false));
    }

    /**
     * Loads the cart image
     * @throws IOException If an I/O error occurs
     */
    private void loadCartImage() throws IOException {
        cartImage = readImage("/assets/items/cart.png");
        fullCartImage = readImage("/assets/items/cart_full.png");
    }

    /**
     * Reads the map matrix from a JSON array
     * @param mapMatrix The JSON array holding the map matrix
     * @return The map matrix represented by tiles
     */
    private Tile[][] readMapMatrix(JSONArray mapMatrix) {
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

                var tile = tileTemplate.createTile(x, y);
                tiles[y][x] = tile;
            }
        }
        return tiles;
    }

    /**
     * Reads a JSON resource from a given path
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
}
