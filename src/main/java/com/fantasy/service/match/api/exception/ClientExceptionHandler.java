package com.fantasy.service.match.api.exception;

import com.fantasy.service.match.dao.exception.EntityConstraintException;
import com.fantasy.service.match.dao.exception.EntityNotFoundException;
import com.fantasy.service.match.service.exception.ServiceClientException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.io.Serializable;
import java.time.LocalDateTime;

@ControllerAdvice
public class ClientExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException ex, WebRequest request) throws Exception {
        String detailMessage = ex.getMessage();
        if (ex.getFieldError() != null) {
            FieldError fieldError = ex.getFieldError();
            detailMessage = String.format("Invalid value for field %s", fieldError.getField());
        }
        ErrorResponse response = createErrorResponse(request, "Invalid property", detailMessage);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = {EntityConstraintException.class, EntityNotFoundException.class, ServiceClientException.class})
    public ResponseEntity<Object> handleConstraintException(RuntimeException ex, WebRequest request) throws Exception {
        ErrorResponse response = createErrorResponse(request, "Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(response);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidFormat(HttpMessageNotReadableException ex, WebRequest request) throws Exception {
        ErrorResponse response = createErrorResponse(request, "Invalid format", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(response);
    }

    private ErrorResponse createErrorResponse(WebRequest request, String message, String detailMessage) {
        ErrorResponse response = new ErrorResponse();
        response.status = HttpStatus.BAD_REQUEST;
        response.message = message;
        response.detailMessage = detailMessage;
        response.path = ((ServletWebRequest) request).getRequest().getServletPath();
        response.timestamp = LocalDateTime.now();
        return response;
    }


    @Data
    static class ErrorResponse implements Serializable {
        LocalDateTime timestamp;
        HttpStatus status;
        String message;
        String detailMessage;
        String path;
    }

}
