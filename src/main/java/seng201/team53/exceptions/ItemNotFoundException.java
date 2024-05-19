package seng201.team53.exceptions;

/**
 * Unchecked exception that indicates an item was not found.
 * This class extends the RuntimeException, meaning it's an unchecked exception.
 */
public class ItemNotFoundException extends RuntimeException {
    /**
     * Constructs a new TileNotFoundException with the specified detail message.
     * @param message the detail message pertaining to this exception.
     */
    public ItemNotFoundException(String message) {
        super(message);
    }
}