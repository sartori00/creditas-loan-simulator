package br.com.creditas.loansimulator.infrastructure.gateway.database.persistence.impl;

import br.com.creditas.loansimulator.domain.model.Person;
import br.com.creditas.loansimulator.infrastructure.gateway.database.entity.PersonEntity;
import br.com.creditas.loansimulator.infrastructure.gateway.database.persistence.PersonPersistence;
import br.com.creditas.loansimulator.infrastructure.gateway.database.repository.PersonEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonPersistenceImpl implements PersonPersistence {

    private final PersonEntityRepository personEntityRepository;

    @Override
    public Optional<Person> findByDocument(String document) {
        return personEntityRepository.findByDocument(document)
                .map(PersonEntity::toModel);
    }

    @Override
    public Person save(Person person) {
        Person saved = personEntityRepository.upsert(
                person.getDocument(),
                person.getBirthDay(),
                person.getEmail(),
                OffsetDateTime.now(),
                "username"
        ).toModel();
        personEntityRepository.flush();
        return saved;
    }
}
