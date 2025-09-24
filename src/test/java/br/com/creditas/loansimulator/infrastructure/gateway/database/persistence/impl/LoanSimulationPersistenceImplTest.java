package br.com.creditas.loansimulator.infrastructure.gateway.database.persistence.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.Person;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import br.com.creditas.loansimulator.infrastructure.gateway.database.entity.LoanSimulationEntity;
import br.com.creditas.loansimulator.infrastructure.gateway.database.repository.LoanSimulationEntityRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanSimulationPersistenceImplTest {

  @Mock private LoanSimulationEntityRepository repository;

  @InjectMocks private LoanSimulationPersistenceImpl loanSimulationPersistence;

  private LoanSimulation loanSimulation;
  private Person person;

  @BeforeEach
  void setUp() {
    var personId = UUID.randomUUID();
    person = new Person(personId, "12345678900", LocalDate.of(1990, 1, 1), "test@example.com");

    var loanSimulationId = UUID.randomUUID();
    loanSimulation =
        new LoanSimulation(
            loanSimulationId,
            person,
            Currency.BRL,
            new BigDecimal("10000.00"),
            new BigDecimal("10000.00"),
            12,
            new BigDecimal("900.00"));
    loanSimulation.setInstallmentAmount(new BigDecimal("900.00"));
    loanSimulation.setLoanAmountBRL(new BigDecimal("10000.00"));
  }

  @Test
  @DisplayName("Should save loan simulation successfully")
  void shouldSaveLoanSimulationSuccessfully() {
    loanSimulationPersistence.save(loanSimulation);

    ArgumentCaptor<LoanSimulationEntity> argumentCaptor =
        ArgumentCaptor.forClass(LoanSimulationEntity.class);
    verify(repository, times(1)).save(argumentCaptor.capture());

    var capturedEntity = argumentCaptor.getValue();
    assertNotNull(capturedEntity);
    assertEquals(loanSimulation.getId(), capturedEntity.getId());
    assertEquals(loanSimulation.getCurrency(), capturedEntity.getCurrency());
    assertEquals(loanSimulation.getLoanAmount(), capturedEntity.getLoanAmount());
    assertEquals(loanSimulation.getQtInstallments(), capturedEntity.getQtInstallments());
    assertEquals(loanSimulation.getInstallmentAmount(), capturedEntity.getInstallmentAmount());
    assertEquals(loanSimulation.getLoanAmountBRL(), capturedEntity.getLoanAmountBRL());
    assertEquals(loanSimulation.getTotalAmountToPay(), capturedEntity.getTotalAmountToPay());
    assertEquals(loanSimulation.getTotalInterest(), capturedEntity.getTotalInterest());
    assertNotNull(capturedEntity.getPerson());
    assertEquals(loanSimulation.getPerson().getId(), capturedEntity.getPerson().getId());
    assertEquals(
        loanSimulation.getPerson().getDocument(), capturedEntity.getPerson().getDocument());
  }

  @Test
  @DisplayName("Should save loan simulation with a new ID if not provided")
  void shouldSaveLoanSimulationWithNewIdIfNotProvided() {
    var newLoanSimulation =
        new LoanSimulation(
            null,
            person,
            Currency.USD,
            new BigDecimal("5000.00"),
            new BigDecimal("25000.00"),
            6,
            new BigDecimal("4500.00"));
    newLoanSimulation.setInstallmentAmount(new BigDecimal("4500.00"));
    newLoanSimulation.setLoanAmountBRL(new BigDecimal("25000.00"));

    loanSimulationPersistence.save(newLoanSimulation);

    ArgumentCaptor<LoanSimulationEntity> argumentCaptor =
        ArgumentCaptor.forClass(LoanSimulationEntity.class);
    verify(repository, times(1)).save(argumentCaptor.capture());

    var capturedEntity = argumentCaptor.getValue();
    assertNotNull(capturedEntity);
    assertEquals(newLoanSimulation.getCurrency(), capturedEntity.getCurrency());
    assertEquals(newLoanSimulation.getLoanAmount(), capturedEntity.getLoanAmount());
    assertEquals(newLoanSimulation.getQtInstallments(), capturedEntity.getQtInstallments());
  }
}
