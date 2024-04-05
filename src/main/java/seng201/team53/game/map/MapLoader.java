package seng201.team53.game.map;

import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MapLoader {
    private final HashMap<String, Map> maps = new HashMap<>();
    private final HashMap<Integer, Image> assets = new HashMap<>();

    public void init() {
        loadImage(0, "/assets/grass_tile.png");
        loadImage(1, "/assets/path_tile.png");
        loadMap("default", "/assets/maps/map_one.txt");
    }

    public void loadMap(String name, String path) {
        var map = new Map(name);
        try (var resource = getClass().getResourceAsStream(path)) {
            if (resource == null)
                throw new RuntimeException("Could not read resource '" + path + "'.");
            var inputStreamReader = new InputStreamReader(resource);
            var bufferedReader = new BufferedReader(inputStreamReader);
            int row = 0;
            for (var line : bufferedReader.lines().toList()) {
                var split = line.split(", ");
                for (int column = 0; column != split.length; column++) {
                    var imageId = Integer.parseInt(split[column]);
                    var image = assets.get(imageId);
                    if (image == null)
                        throw new RuntimeException("Missing asset for id '" + imageId + "' from map '" + name + "'.");
                    var tile = new Tile(image, true, imageId == 1); // todo - some how add an option to make the tile buildable or not
                    map.getTiles()[row][column] = tile;
                }
                row++;
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        maps.put(name.toLowerCase(), map);
    }
    public void loadImage(int mapValue, String path) {
        try (var resource = getClass().getResourceAsStream(path)) {
            if (resource == null)
                throw new RuntimeException("Could not read resource '" + path + "'.");
            assets.put(mapValue, new Image(resource));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Map getMap(String name) {
        return maps.get(name.toLowerCase());
    }
}
