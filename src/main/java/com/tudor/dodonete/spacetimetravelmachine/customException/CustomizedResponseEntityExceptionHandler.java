package com.tudor.dodonete.spacetimetravelmachine.customException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
@ApiIgnore
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        Map<String, Object> responseModel = getModel(Exception.class.toString(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return handleExceptionInternal(ex, responseModel, new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleAllUserNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> responseModel = getModel(ResourceNotFoundException.class.toString(),
                new Date(),
                ex.getMessage(),
                "No valid handler was found for the requested path");
        return handleExceptionInternal(ex, responseModel, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(CollisionException.class)
    public final ResponseEntity<Object> handleCollisionException(CollisionException ex, WebRequest request) {
        Map<String, Object> responseModel = getModel(CollisionException.class.toString(),
                new Date(),
                ex.getMessage(),
                "The current exception is thrown in order to stop a possible SQL Error");
        return handleExceptionInternal(ex, responseModel,
                new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> responseModel = getModel(MethodArgumentNotValidException.class.toString(),
                new Date(),
                "Validation failed",
                ex.getBindingResult().toString());

        return handleExceptionInternal(ex, responseModel, headers, HttpStatus.BAD_REQUEST, request);
    }

    private Map<String, Object> getModel(String exception, Date date, String message, String details) {
        Map<String, Object> responseModel = new HashMap<>();
        responseModel.put("exception", exception);
        responseModel.put("date", date);
        responseModel.put("message", message);
        responseModel.put("details", details);
        return responseModel;
    }
}
