package dev.carlosmoises.projeto.enferm.controller;

import dev.carlosmoises.projeto.enferm.DTO.GlobalExceptionHandlerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<GlobalExceptionHandlerDTO>> exceptionMessage(MethodArgumentNotValidException methodArgumentNotValidException) {
        var errorsFormated = methodArgumentNotValidException.getFieldErrors().stream().map((error) ->
                new GlobalExceptionHandlerDTO(
                        error.getField(),
                        error.getDefaultMessage()
                )
        ).toList();

        return ResponseEntity.badRequest().body(errorsFormated);
    }
}
