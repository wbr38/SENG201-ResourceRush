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
import seng201.team53.game.items.Purchasable;
import seng201.team53.game.items.ResourceType;
import seng201.team53.game.items.towers.Tower;
import seng201.team53.game.items.towers.TowerType;
import seng201.team53.game.items.upgrade.UpgradeItem;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for loading the tile templates, game maps, and images
 */
public class AssetLoader {
    private final Map<Integer, TileTemplate> tileTemplates = new HashMap<>();
    private final Map<TowerType, Image> towerImages = new HashMap<>();
    private final Map<TowerType, Image> brokenTowerImages = new HashMap<>();
    private final Map<UpgradeItem, Image> upgradeItemImages = new HashMap<>();
    private final JSONParser jsonParser = new JSONParser();
    private Image emptyCart;
    private Image quarryCartImage;
    private Image stoneCartImage;
    private Image windCartImage;
    private Image woodCartImage;
    private Image fullCartImage;

    /**
     * Initializes the MapLoader by loading the tiles and images
     */
    public void init() {
        try {
            loadTiles();
            loadTowerImages();
            loadUpgradeItemImages();
            loadCartImages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a map from a JSON file
     * @param name The name of the map
     * @param path The path to the JSON file resource
     * @param  mapBackgroundPane The background map pane
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
     * Retrieves the image associated with a purchasable item
     * @param item The purchasable item for which the image is to be retrieved
     * @return The image of the specified item
     */
    public Image getItemImage(Purchasable item) {
        if (item instanceof TowerType towerType)
            return getTowerTypeImage(towerType, false);
        if (item instanceof UpgradeItem upgradeItem)
            return getUpgradeItemImages(upgradeItem);
        return null;
    }

    /**
     * Retrieves the image for a specific type of tower, with an option for a broken state
     * @param towerType The type of the tower for which the image is to be retrieved
     * @param broken true if the broken image should be returned
     * @return The image of the tower
     */
    public Image getTowerTypeImage(TowerType towerType, boolean broken) {
        return (broken ? brokenTowerImages : towerImages).get(towerType);
    }

    /**
     * Retrieves the image of the cart, either full or empty
     * @param full true if the full cart image should be returned
     * @return The image of the full or empty shopping cart
     */
    public Image getCartImage(ResourceType resourceType, boolean full) {
        if (full)
            return fullCartImage;
        return switch (resourceType) {
            case WOOD -> woodCartImage;
            case STONE -> stoneCartImage;
            case ORE -> quarryCartImage;
            case ENERGY -> windCartImage;
            default -> emptyCart;
        };
    }

    /**
     * Retrieves the image of an upgrade item
     * @param upgradeItem The type of upgrade item for which the image is to be retrieved
     * @return The image of the upgrade item
     */
    public Image getUpgradeItemImages(UpgradeItem upgradeItem) {
        return upgradeItemImages.get(upgradeItem);
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
     * Loads predefined tiles where id 0 represents a buildable tile, id 1 represents a path tile and 2 represents a non
     * buildable and non path tile
     * @throws IOException If an I/O error occurs
     */
    private void loadTiles() throws IOException {
        tileTemplates.put(0, new TileTemplate(true, false));
        tileTemplates.put(1, new TileTemplate(false, true));
        tileTemplates.put(2, new TileTemplate(false, false));
    }

    /**
     * Loads and stores the image for each tower type into a map
     * The intact and broken images for each tower are loaded
     */
    private void loadTowerImages() {
        towerImages.put(Tower.Type.LUMBER_MILL, readImage("/assets/items/wood_tower.png"));
        towerImages.put(Tower.Type.MINE, readImage("/assets/items/stone_tower.png"));
        towerImages.put(Tower.Type.QUARRY, readImage("/assets/items/quarry_tower.png"));
        towerImages.put(Tower.Type.WINDMILL, readImage("/assets/items/wind_turbine_tower.png"));
        brokenTowerImages.put(Tower.Type.LUMBER_MILL, readImage("/assets/items/wood_tower_broken.png"));
        brokenTowerImages.put(Tower.Type.MINE, readImage("/assets/items/stone_tower_broken.png"));
        brokenTowerImages.put(Tower.Type.QUARRY, readImage("/assets/items/quarry_tower.png"));
        brokenTowerImages.put(Tower.Type.WINDMILL, readImage("/assets/items/quarry_tower_broken.png"));
    }

    /**
     * Loads and stores the image for each item upgrade into a map
     */
    private void loadUpgradeItemImages() {
        upgradeItemImages.put((UpgradeItem)UpgradeItem.Type.REPAIR_TOWER, readImage("/assets/items/repair_tower.png"));
        upgradeItemImages.put((UpgradeItem)UpgradeItem.Type.TEMP_FASTER_TOWER_RELOAD, readImage("/assets/items/faster_reload.png"));
        upgradeItemImages.put((UpgradeItem)UpgradeItem.Type.TEMP_SLOWER_CART, readImage("/assets/items/slower_cart.png"));
        upgradeItemImages.put((UpgradeItem)UpgradeItem.Type.FILL_CART, readImage("/assets/items/cart_full.png"));
    }

    /**
     * Loads the cart images
     * @throws IOException If an I/O error occurs
     */
    private void loadCartImages() throws IOException {
        emptyCart = readImage("/assets/items/cart.png");
        quarryCartImage = readImage("/assets/items/cart_quarry.png");
        stoneCartImage = readImage("/assets/items/cart_stone.png");
        windCartImage = readImage("/assets/items/cart_wind.png");
        woodCartImage = readImage("/assets/items/cart_wood.png");
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
