package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.handler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name = "ProblemResponseDto")
public record ProblemResponseDTO(
    @Schema(example = "Error Message") String message,
    @Schema(example = "2025-09-23T23:44:51.792653300") String dateTime) {
  public ProblemResponseDTO(Exception ex) {
    this(ex.getMessage(), LocalDateTime.now().toString());
  }
}
