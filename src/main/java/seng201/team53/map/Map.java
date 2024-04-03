package seng201.team53.map;

import java.util.ArrayList;

public class Map {

    public final int height = 256;
    public final int width = 256;
    public Tile[][] tiles;
    public ArrayList<Tile> track;

    Map() {

    }

    public Tile getTileAt(Position pos) {
        // TODO: Error checking
        return this.tiles[pos.x][pos.y];
    }

    // TODO Methods:
    // Initialise map tiles
    // Place object (tower) on map
    // Remove object from map
}
