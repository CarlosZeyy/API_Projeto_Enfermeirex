package dev.carlosmoises.projeto.enferm.controller;

import dev.carlosmoises.projeto.enferm.DTO.GlobalExceptionHandlerDTO;
import dev.carlosmoises.projeto.enferm.config.TokenExpiredException;
import org.springframework.dao.DataIntegrityViolationException;
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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseMessage> handleBusinessLogicException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new ResponseMessage(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseMessage> handleIntegrityViolation() {
        return ResponseEntity.badRequest().body(new ResponseMessage("Não é possivel excluir esse paciente, pois ele já possui agendamentos vinculados ao sistema."));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ResponseMessage> handleTokenExpired() {
        return ResponseEntity.status(401).body(new ResponseMessage("Token de acesso expirado. Faça login novamente."));
    }
};
