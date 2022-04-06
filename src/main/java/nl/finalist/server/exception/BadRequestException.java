package nl.finalist.server.exception;

import java.io.Serializable;

public class BadRequestException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }
}
