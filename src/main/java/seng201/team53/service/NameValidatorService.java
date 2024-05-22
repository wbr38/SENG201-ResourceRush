package seng201.team53.service;

import java.util.regex.Pattern;

/**
 * This service validates a given name
 */
public class NameValidatorService {
    public static final Pattern VALID_NAME_REGEX = Pattern.compile("^[A-Za-z0-9]*$");
    public static final int MIN_NAME_LENGTH = 3;
    public static final int MAX_NAME_LENGTH = 15;

    /**
     * This method checks if the given name is within the character range and matches the valid name regex
     * @param name The name to be tested
     * @return true if the name is valid, false otherwise
     */
    public boolean isValid(String name) {
        if (name.length() < MIN_NAME_LENGTH)
            return false;
        if (name.length() > MAX_NAME_LENGTH)
            return false;
        return VALID_NAME_REGEX.matcher(name).matches();
    }
}
