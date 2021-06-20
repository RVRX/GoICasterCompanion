package goistreamtoolredux;

/**
 * A file read in by the application contains invalid data, typically due to outside tampering.
 */
public class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}
