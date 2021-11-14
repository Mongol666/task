package users;

import entity.AbstractEntity;
import credentials.Credentials;
import lombok.*;
import role.Role;

import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class Person extends AbstractEntity {
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate dateOfBirth;
    private Credentials credentials;
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Person person = (Person) o;
        return firstName.equals(person.firstName) && lastName.equals(person.lastName) && patronymic.equals(person.patronymic) && dateOfBirth.equals(person.dateOfBirth) && credentials.equals(person.credentials) && role == person.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, patronymic, dateOfBirth, credentials, role);
    }
}