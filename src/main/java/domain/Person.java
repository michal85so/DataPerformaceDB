package domain;

import lombok.*;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
@Document(collection = "person")
public class Person {

    @org.springframework.data.annotation.Id
    private String id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String nickname;

    @NonNull
    private String password;

    private String phoneNumber;

    private String email;

    private String nip;

    private String pesel;

    @NonNull
    private Type type;

    public enum Type {
        Client,
        Worker;
    }

    @PersistenceConstructor
    public Person(String firstName,
                  String lastName,
                  String nickname,
                  String password,
                  String phoneNumber,
                  String email,
                  String nip,
                  String pesel,
                  Type type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nip = nip;
        this.pesel = pesel;
        this.type = type;
    }
}
