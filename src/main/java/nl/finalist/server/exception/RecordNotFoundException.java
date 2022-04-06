package nl.finalist.server.exception;

import java.io.Serializable;

public class RecordNotFoundException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public RecordNotFoundException() {
        super();
    }

    public RecordNotFoundException(String message) {
        super(message);
    }
}
