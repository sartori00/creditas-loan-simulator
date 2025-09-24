package br.com.creditas.loansimulator.infrastructure.gateway.database.entity;

import br.com.creditas.loansimulator.domain.model.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@Entity(name = "PERSON")
@EntityListeners(AuditingEntityListener.class)
public class PersonEntity extends AuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID", updatable = false, unique = true, nullable = false)
  private UUID id;

  @Column(name = "DOCUMENT")
  private String document;

  @Column(name = "BIRTHDAY")
  private LocalDate birthDay;

  @Column(name = "EMAIL")
  private String email;

  public PersonEntity(Person person) {
    this.id = person.getId();
    this.document = person.getDocument();
    this.birthDay = person.getBirthDay();
    this.email = person.getEmail();
  }

  public Person toModel() {
    return new Person(id, document, birthDay, email);
  }
}
