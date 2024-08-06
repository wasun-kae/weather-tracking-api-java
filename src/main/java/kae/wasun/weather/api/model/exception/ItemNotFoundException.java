package kae.wasun.weather.api.model.exception;

public class ItemNotFoundException extends Exception {

    public ItemNotFoundException() {
        super("Item Not Found");
    }
}
