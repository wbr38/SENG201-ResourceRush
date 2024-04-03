package seng201.team53.map;

public class Tile {

    private String name;
    private Position position;

    /** Can objects (towers) be placed on this tile? */
    private Boolean buildable;

    Tile(String name, Boolean buildable, Position pos) {
        this.name = name;
        this.buildable = buildable;
    }

    public String getName() {
        return name;
    }

    public Boolean buildable() {
        return buildable;
    }

    public Position getPosition() {
        return position;
    }
}
