package br.com.creditas.loansimulator.infrastructure.event.subscriber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.Person;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import br.com.creditas.loansimulator.infrastructure.event.NewLoanCalculatedObservable;
import br.com.creditas.loansimulator.infrastructure.gateway.database.persistence.LoanSimulationPersistence;
import br.com.creditas.loansimulator.infrastructure.gateway.database.persistence.PersonPersistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaveOnDbSubscriberTest {

  @Mock private PersonPersistence personPersistence;

  @Mock private LoanSimulationPersistence loanSimulationPersistence;

  @InjectMocks private SaveOnDbSubscriber saveOnDbSubscriber;

  private Person person;
  private LoanSimulation loanSimulation;
  private NewLoanCalculatedObservable event;

  @BeforeEach
  void setUp() {
    person =
        new Person(UUID.randomUUID(), "12345678900", LocalDate.of(1990, 1, 1), "test@example.com");

    loanSimulation = new LoanSimulation(Currency.BRL, new BigDecimal("10000.00"), 12, person);
    loanSimulation.setInstallmentAmount(new BigDecimal("900.00"));
    loanSimulation.setLoanAmountBRL(new BigDecimal("10000.00"));

    event = new NewLoanCalculatedObservable(this, loanSimulation);
  }

  @Test
  @DisplayName("Should save loan simulation when person already exists in the database")
  void shouldSaveLoanSimulationWhenPersonAlreadyExistsInTheDatabase() {
    var existingPerson =
        new Person(
            UUID.randomUUID(), person.getDocument(), person.getBirthDay(), person.getEmail());
    when(personPersistence.findByDocument(person.getDocument()))
        .thenReturn(Optional.of(existingPerson));

    saveOnDbSubscriber.onApplicationEvent(event);

    verify(personPersistence, times(1)).findByDocument(person.getDocument());
    verify(personPersistence, never()).save(any(Person.class));
    verify(loanSimulationPersistence, times(1)).save(loanSimulation);
    assertEquals(loanSimulation.getPerson().getId(), existingPerson.getId());
  }

  @Test
  @DisplayName("Should save person and loan simulation when person does not exist in the database")
  void shouldSavePersonAndLoanSimulationWhenPersonDoesNotExistInTheDatabase() {
    var savedPerson =
        new Person(
            UUID.randomUUID(), person.getDocument(), person.getBirthDay(), person.getEmail());
    when(personPersistence.findByDocument(person.getDocument())).thenReturn(Optional.empty());
    when(personPersistence.save(any(Person.class))).thenReturn(savedPerson);

    saveOnDbSubscriber.onApplicationEvent(event);

    verify(personPersistence, times(1)).findByDocument(person.getDocument());
    verify(personPersistence, times(1)).save(person);
    verify(loanSimulationPersistence, times(1)).save(loanSimulation);
    assertEquals(loanSimulation.getPerson().getId(), savedPerson.getId());
  }

  @Test
  @DisplayName("Should return true for supportsAsyncExecution")
  void shouldReturnTrueForSupportsAsyncExecution() {
    assertTrue(saveOnDbSubscriber.supportsAsyncExecution());
  }
}
