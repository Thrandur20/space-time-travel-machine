package com.tudor.dodonete.spacetimetravelmachine.customException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class CollisionException extends RuntimeException {
    public CollisionException(String message) {
        super(message);
    }
}
