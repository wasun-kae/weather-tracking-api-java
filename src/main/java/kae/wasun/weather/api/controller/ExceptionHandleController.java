package kae.wasun.weather.api.controller;

import kae.wasun.weather.api.model.dto.ErrorDto;
import kae.wasun.weather.api.model.exception.ItemAlreadyExists;
import kae.wasun.weather.api.model.exception.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandleController {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorDto> handleItemNotFound(ItemNotFoundException exception) {
        var errorDto = ErrorDto.builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(ItemAlreadyExists.class)
    public ResponseEntity<ErrorDto> handleItemAlreadyExists(ItemAlreadyExists exception) {
        var errorDto = ErrorDto.builder()
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleBadRequest() {
        var errorDto = ErrorDto.builder()
                .message("Invalid Format")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }
}
