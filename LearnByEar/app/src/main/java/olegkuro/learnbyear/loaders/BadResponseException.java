package olegkuro.learnbyear.loaders;

/**
 * Created by Roman on 21/01/2017.
 */

public class BadResponseException extends Exception {

    public BadResponseException(Throwable cause) {
        super(cause);
    }

    public BadResponseException(String message) {
        super(message);
    }
}
