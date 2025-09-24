package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.handler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.FieldError;

@Schema(name = "ErrorsValidateData")
public record ErrorsValidateDataResponseDTO(
    @Schema(example = "name") String field, @Schema(example = "name is required") String message) {
  public ErrorsValidateDataResponseDTO(FieldError error) {
    this(error.getField(), error.getDefaultMessage());
  }
}
