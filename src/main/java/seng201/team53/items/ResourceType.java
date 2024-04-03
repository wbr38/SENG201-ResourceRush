package seng201.team53.items;

public enum ResourceType {

    WOOD("Wood"),
    STONE("Stone"),
    ORE("Ore"),
    ENERGY("Energy"),
    FOOD("Food"),
    ;

    // TODO: Maybe add a sprite filepath here?
    private String name;

    ResourceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}