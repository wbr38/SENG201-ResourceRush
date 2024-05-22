package seng201.team53.items;

/**
 * Enum representing the types of resources
 */
public enum ResourceType {
    /**
     * Represents the wood resource type
     */
    WOOD("Wood"),

    /**
     * Represents the stone resource type
     */
    STONE("Stone"),

    /**
     * Represents the ore resource type
     */
    ORE("Ore"),

    /**
     * Represents the energy resource type
     */
    ENERGY("Energy");

    private String name;

    /**
     * Constructs a new resource type with a given name
     * @param name The name
     */
    ResourceType(String name) {
        this.name = name;
    }

    /**
     * Retrieves the resource name
     * @return The name
     */
    public String getName() {
        return name;
    }
}