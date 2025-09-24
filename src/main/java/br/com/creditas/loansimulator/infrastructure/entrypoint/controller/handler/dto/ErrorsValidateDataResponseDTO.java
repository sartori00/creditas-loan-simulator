package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.handler.dto;

import org.springframework.validation.FieldError;

public record ErrorsValidateDataResponseDTO(String field, String message) {
  public ErrorsValidateDataResponseDTO(FieldError error) {
    this(error.getField(), error.getDefaultMessage());
  }
}
