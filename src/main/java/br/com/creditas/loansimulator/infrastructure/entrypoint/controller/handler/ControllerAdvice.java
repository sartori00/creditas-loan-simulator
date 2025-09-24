package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.handler;

import br.com.creditas.loansimulator.application.exceptions.BusinessException;
import br.com.creditas.loansimulator.application.exceptions.UnsupportedAgeException;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.handler.dto.ErrorsValidateDataResponseDTO;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.handler.dto.ProblemResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UnsupportedAgeException.class)
  public ResponseEntity<ProblemResponseDTO> unsupportedAgeException(UnsupportedAgeException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProblemResponseDTO(ex));
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ProblemResponseDTO> businessException(BusinessException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProblemResponseDTO(ex));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ProblemResponseDTO> illegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProblemResponseDTO(ex));
  }

  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ex.getFieldErrors().stream().map(ErrorsValidateDataResponseDTO::new).toList());
  }

  @Override
  public ResponseEntity<Object> handleHttpMessageNotReadable(
      @NonNull HttpMessageNotReadableException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ProblemResponseDTO(ex));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemResponseDTO> handle500Error(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ProblemResponseDTO(ex));
  }
}
