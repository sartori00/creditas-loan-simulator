package br.com.creditas.loansimulator.infrastructure.gateway.database.persistence.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.creditas.loansimulator.domain.model.Person;
import br.com.creditas.loansimulator.infrastructure.gateway.database.entity.PersonEntity;
import br.com.creditas.loansimulator.infrastructure.gateway.database.repository.PersonEntityRepository;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
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
class PersonPersistenceImplTest {

  @Mock private PersonEntityRepository personEntityRepository;

  @InjectMocks private PersonPersistenceImpl personPersistence;

  private String document;
  private LocalDate birthDay;
  private String email;
  private Person person;
  private PersonEntity personEntity;
  private UUID personId;

  @BeforeEach
  void setUp() {
    document = "12345678900";
    birthDay = LocalDate.of(1990, 1, 1);
    email = "test@example.com";
    personId = UUID.randomUUID();
    person = new Person(personId, document, birthDay, email);
    personEntity = new PersonEntity(person);
  }

  @Test
  @DisplayName("Should find person by document successfully")
  void shouldFindPersonByDocumentSuccessfully() {
    when(personEntityRepository.findByDocument(document)).thenReturn(Optional.of(personEntity));

    Optional<Person> result = personPersistence.findByDocument(document);

    assertTrue(result.isPresent());
    assertEquals(person.getDocument(), result.get().getDocument());
    assertEquals(person.getEmail(), result.get().getEmail());
    verify(personEntityRepository, times(1)).findByDocument(document);
  }

  @Test
  @DisplayName("Should return empty optional when person not found by document")
  void shouldReturnEmptyOptionalWhenPersonNotFoundByDocument() {
    when(personEntityRepository.findByDocument(document)).thenReturn(Optional.empty());

    Optional<Person> result = personPersistence.findByDocument(document);

    assertFalse(result.isPresent());
    verify(personEntityRepository, times(1)).findByDocument(document);
  }

  @Test
  @DisplayName("Should save a new person successfully")
  void shouldSaveNewPersonSuccessfully() {
    when(personEntityRepository.upsert(
            eq(document), eq(birthDay), eq(email), any(OffsetDateTime.class), eq("username")))
        .thenReturn(personEntity);

    var savedPerson = personPersistence.save(person);

    assertNotNull(savedPerson);
    assertEquals(person.getDocument(), savedPerson.getDocument());
    assertEquals(person.getEmail(), savedPerson.getEmail());
    assertEquals(person.getBirthDay(), savedPerson.getBirthDay());
    verify(personEntityRepository, times(1))
        .upsert(eq(document), eq(birthDay), eq(email), any(OffsetDateTime.class), eq("username"));
    verify(personEntityRepository, times(1)).flush();
  }

  @Test
  @DisplayName("Should update an existing person successfully")
  void shouldUpdateExistingPersonSuccessfully() {
    var updatedPersonData = new Person(document, LocalDate.of(1995, 5, 10), "updated@example.com");
    var updatedPersonEntity =
        new PersonEntity(
            new Person(
                personId,
                updatedPersonData.getDocument(),
                updatedPersonData.getBirthDay(),
                updatedPersonData.getEmail()));

    when(personEntityRepository.upsert(
            eq(updatedPersonData.getDocument()),
            eq(updatedPersonData.getBirthDay()),
            eq(updatedPersonData.getEmail()),
            any(OffsetDateTime.class),
            eq("username")))
        .thenReturn(updatedPersonEntity);

    var result = personPersistence.save(updatedPersonData);

    assertNotNull(result);
    assertEquals(updatedPersonData.getDocument(), result.getDocument());
    assertEquals(updatedPersonData.getEmail(), result.getEmail());
    assertEquals(updatedPersonData.getBirthDay(), result.getBirthDay());

    verify(personEntityRepository, times(1))
        .upsert(
            eq(updatedPersonData.getDocument()),
            eq(updatedPersonData.getBirthDay()),
            eq(updatedPersonData.getEmail()),
            any(OffsetDateTime.class),
            eq("username"));
    verify(personEntityRepository, times(1)).flush();
  }

  @Test
  @DisplayName("Should pass correct updatedAt and updatedBy to upsert method")
  void shouldPassCorrectUpdatedAtAndUpdatedByToUpsertMethod() {
    ArgumentCaptor<OffsetDateTime> updatedAtCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);
    ArgumentCaptor<String> updatedByCaptor = ArgumentCaptor.forClass(String.class);

    when(personEntityRepository.upsert(
            anyString(),
            any(LocalDate.class),
            anyString(),
            updatedAtCaptor.capture(),
            updatedByCaptor.capture()))
        .thenReturn(personEntity);

    personPersistence.save(person);

    assertNotNull(updatedAtCaptor.getValue());
    assertEquals("username", updatedByCaptor.getValue());
    verify(personEntityRepository, times(1)).flush();
  }
}
