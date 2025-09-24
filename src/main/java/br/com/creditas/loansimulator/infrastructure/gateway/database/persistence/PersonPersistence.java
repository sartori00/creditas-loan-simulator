package br.com.creditas.loansimulator.infrastructure.gateway.database.persistence;

import br.com.creditas.loansimulator.domain.model.Person;
import java.util.Optional;

public interface PersonPersistence {
  Optional<Person> findByDocument(String document);

  Person save(Person person);
}
