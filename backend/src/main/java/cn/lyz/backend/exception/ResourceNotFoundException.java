package cn.lyz.backend.exception;

/**
 * Thrown when a requested resource (e.g. User) cannot be found.
 * Maps to HTTP 404 in the global exception handler.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
