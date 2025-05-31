package com.desafio.tecnico.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler global de exceções que centraliza o tratamento de erros da aplicação.
 * Converte exceções em respostas HTTP apropriadas com mensagens informativas.
 *
 * Testes existentes:
 * - Não há testes específicos para o GlobalExceptionHandler
 * - O comportamento é testado parcial e indiretamente via DeviceControllerIntegrationTest
 * - O tratamento de DeviceNotFoundException é testado quando se busca um ID inexistente
 * - O tratamento de InvalidOperationException é testado ao tentar deletar dispositivos em uso
 *
 * Possíveis melhorias nos testes:
 * - Implementar testes unitários específicos para cada método handler usando MockMvc
 * - Testar o formato e conteúdo das respostas de erro (status HTTP, corpo, cabeçalhos)
 * - Verificar o comportamento com diferentes tipos de exceções e parâmetros
 * - Testar handling de validação (MethodArgumentNotValidException) com diferentes erros de validação
 * - Adicionar testes para verificar o formato do timestamp e outros campos da resposta
 *
 * Possíveis melhorias:
 * - Adicionar mais detalhes às respostas de erro (códigos de erro, links para documentação)
 * - Implementar logging detalhado das exceções para fins de monitoramento
 * - Adicionar suporte a internacionalização de mensagens de erro
 * - Implementar mecanismo para correlacionar erros com request IDs para facilitar o troubleshooting
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeviceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDeviceNotFoundException(DeviceNotFoundException ex) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidOperationException(InvalidOperationException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGlobalException(Exception ex) {
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro interno no servidor: " + ex.getMessage(),
                LocalDateTime.now()
        );
    }

    public static class ErrorResponse {
        private int status;
        private String message;
        private LocalDateTime timestamp;

        public ErrorResponse(int status, String message, LocalDateTime timestamp) {
            this.status = status;
            this.message = message;
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }
}
