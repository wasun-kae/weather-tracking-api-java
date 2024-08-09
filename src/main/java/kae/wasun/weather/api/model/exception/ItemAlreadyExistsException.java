package kae.wasun.weather.api.model.exception;

public class ItemAlreadyExistsException extends Exception {

    public ItemAlreadyExistsException() {
        super("Item Already Exists");
    }
}
