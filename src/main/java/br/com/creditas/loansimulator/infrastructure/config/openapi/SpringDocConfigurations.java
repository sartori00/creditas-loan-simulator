package br.com.creditas.loansimulator.infrastructure.config.openapi;

import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.handler.dto.ProblemResponseDTO;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto.LoanSimulationResponseDto;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto.PersonResponseDto;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto.SimulateRequestedResponseDto;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Creditas Loan Simulator API")
                .version("v1")
                .description("API Rest for simulate loans at Creditas")
                .contact(new Contact().name("Rodrigo Sartori").email("93sartori@gmail.com")))
        .components(new Components().schemas(this.generateSchemas()));
  }

  @SuppressWarnings("rawtypes")
  private Map<String, Schema> generateSchemas() {
    final Map<String, Schema> schemaMap = new HashMap<>();

    Map<String, Schema> problemSchema =
        ModelConverters.getInstance().read(ProblemResponseDTO.class);
    Map<String, Schema> loanSimulationResponseDto =
        ModelConverters.getInstance().read(LoanSimulationResponseDto.class);
    Map<String, Schema> personResponseDto =
        ModelConverters.getInstance().read(PersonResponseDto.class);
    Map<String, Schema> simulateRequestedResponseDto =
        ModelConverters.getInstance().read(SimulateRequestedResponseDto.class);

    schemaMap.putAll(problemSchema);
    schemaMap.putAll(loanSimulationResponseDto);
    schemaMap.putAll(personResponseDto);
    schemaMap.putAll(simulateRequestedResponseDto);
    schemaMap.put(
        "ErrorsValidateDataList",
        new ArraySchema().items(new Schema<>().$ref("#/components/schemas/ErrorsValidateData")));

    return schemaMap;
  }
}
