package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.handler.dto;

import java.time.LocalDateTime;

public record ProblemResponseDTO(String message, LocalDateTime dateTime) {
  public ProblemResponseDTO(Exception ex) {
    this(ex.getMessage(), LocalDateTime.now());
  }
}
