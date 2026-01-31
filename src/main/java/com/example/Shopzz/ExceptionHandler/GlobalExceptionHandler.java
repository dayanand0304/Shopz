package com.example.Shopzz.ExceptionHandler;

import com.example.Shopzz.CustomExceptions.Category.CategoryAlreadyExistsException;
import com.example.Shopzz.CustomExceptions.Category.CategoryNotFoundException;
import com.example.Shopzz.CustomExceptions.Products.ProductAlreadyExistsException;
import com.example.Shopzz.CustomExceptions.Products.ProductNotFoundException;
import com.example.Shopzz.CustomExceptions.Users.UserEmailAlreadyExistsException;
import com.example.Shopzz.CustomExceptions.Users.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //404: NOT FOUND
    @ExceptionHandler({
            UserNotFoundException.class,
            CategoryNotFoundException.class,
            ProductNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex,
                                                        HttpServletRequest request){
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    //409: CONFLICT
    @ExceptionHandler({
            UserEmailAlreadyExistsException.class,
            CategoryAlreadyExistsException.class,
            ProductAlreadyExistsException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex,
                                                        HttpServletRequest request){
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    //400: VALIDATION FAILURE
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest request){
        String message=ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Validation Failed");

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    //500: INTERNAL SERVER ERROR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected server error",
                request.getRequestURI()
        );
    }


    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status,
                                                            String message,
                                                            String path){
        ErrorResponse error=new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.name(),
                message,
                path
        );
        return ResponseEntity.status(status).body(error);
    }
}
