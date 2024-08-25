package kae.wasun.weather.api.controller;

import kae.wasun.weather.api.model.dto.ErrorDto;
import kae.wasun.weather.api.model.exception.ItemAlreadyExistsException;
import kae.wasun.weather.api.model.exception.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ExceptionHandleController {

    @ExceptionHandler({
            ItemNotFoundException.class,
            NoResourceFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDto> handleItemNotFound(Exception e) {
        var errorDto = this.convertToErrorDto(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(ItemAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorDto> handleItemAlreadyExists(Exception e) {
        var errorDto = this.convertToErrorDto(e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDto> handleBadRequest() {
        var errorDto = this.convertToErrorDto("Invalid Format");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDto> handleInternalServerError() {
        var errorDto = this.convertToErrorDto("Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }

    private ErrorDto convertToErrorDto(Exception e) {
        return this.convertToErrorDto(e.getMessage());
    }

    private ErrorDto convertToErrorDto(String message) {
        return ErrorDto.builder()
                .message(message)
                .build();
    }
}
