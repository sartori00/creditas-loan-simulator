package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.openapi;

import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.handler.dto.ErrorsValidateDataResponseDTO;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto.LoanSimulationRequestDto;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto.LoanSimulationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;

@Tag(name = "Loan")
public interface LoanControllerOpenApi {

  @Operation(summary = "Calculate a Loan for single simulate")
  @ApiResponse(
      responseCode = "200",
      description = "Ok Response",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(ref = "LoanSimulationResponseDto")))
  @ApiResponse(
      responseCode = "400",
      description = "Bad Request Response",
      content =
          @Content(
              mediaType = "application/json",
              array =
                  @ArraySchema(
                      schema = @Schema(implementation = ErrorsValidateDataResponseDTO.class))))
  @ApiResponse(
      responseCode = "404",
      description = "Not Found Response",
      content =
          @Content(mediaType = "application/json", schema = @Schema(ref = "ProblemResponseDto")))
  @ApiResponse(
      responseCode = "500",
      description = "Internal Server Error Response",
      content =
          @Content(mediaType = "application/json", schema = @Schema(ref = "ProblemResponseDto")))
  ResponseEntity<LoanSimulationResponseDto> simulateALoan(@Valid LoanSimulationRequestDto dto);

  @Operation(summary = "Calculate a Loan for multiple simulates")
  @ApiResponse(
      responseCode = "200",
      description = "Ok Response",
      content =
          @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(ref = "LoanSimulationResponseDto"))))
  @ApiResponse(
      responseCode = "400",
      description = "Bad Request Response",
      content =
          @Content(
              mediaType = "application/json",
              array =
                  @ArraySchema(
                      schema = @Schema(implementation = ErrorsValidateDataResponseDTO.class))))
  @ApiResponse(
      responseCode = "404",
      description = "Not Found Response",
      content =
          @Content(mediaType = "application/json", schema = @Schema(ref = "ProblemResponseDto")))
  @ApiResponse(
      responseCode = "500",
      description = "Internal Server Error Response",
      content =
          @Content(mediaType = "application/json", schema = @Schema(ref = "ProblemResponseDto")))
  CompletableFuture<ResponseEntity<List<LoanSimulationResponseDto>>> simulateMultipleLoans(
      @Valid List<LoanSimulationRequestDto> dto);
}
