package seng201.team53.exceptions;

/**
 * Unchecked exception that indicates a tile was not found
 */
public class TileNotFoundException extends RuntimeException {
    /**
     * Constructs a new TileNotFoundException with the specified detail message.
     * @param message the detail message pertaining to this exception.
     */
    public TileNotFoundException(String message) {
        super(message);
    }
}